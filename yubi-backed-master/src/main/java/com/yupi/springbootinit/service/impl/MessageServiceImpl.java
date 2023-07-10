//package com.yupi.springbootinit.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.google.gson.Gson;
//import com.yupi.springbootinit.constant.CommonConstant;
//import com.yupi.springbootinit.model.dto.message.MessageQueryRequest;
//import com.yupi.springbootinit.model.dto.message.PostEsDTO;
//import com.yupi.springbootinit.model.entity.Message;
//import com.yupi.springbootinit.model.entity.User;
//import com.yupi.springbootinit.model.vo.MessageVO;
//import com.yupi.springbootinit.service.MessageService;
//import com.yupi.springbootinit.mapper.MessageMapper;
//import com.yupi.springbootinit.service.UserService;
//import com.yupi.springbootinit.utils.SqlUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang3.ObjectUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.sort.SortBuilder;
//import org.elasticsearch.search.sort.SortBuilders;
//import org.elasticsearch.search.sort.SortOrder;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
//import org.springframework.data.elasticsearch.core.SearchHit;
//import org.springframework.data.elasticsearch.core.SearchHits;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
//* @author 86187
//* @description 针对表【message(文章)】的数据库操作Service实现
//* @createDate 2023-06-11 13:52:43
//*/
//@Service
//@Slf4j
//public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
//    implements MessageService{
//    private final static Gson GSON = new Gson();
//
//    @Resource
//    private UserService userService;
//    @Resource
//    private ElasticsearchRestTemplate elasticsearchRestTemplate;
//
//
//
//
//    /**
//     * 获取查询包装类
//     *
//     * @param messageQueryRequest
//     * @return
//     */
//    @Override
//    public QueryWrapper<Message> getQueryWrapper(MessageQueryRequest messageQueryRequest) {
//        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
//        if (messageQueryRequest == null) {
//            return queryWrapper;
//        }
//        String searchText = messageQueryRequest.getSearchText();
//        String sortField = messageQueryRequest.getSortField();
//        String sortOrder = messageQueryRequest.getSortOrder();
//        Long id = messageQueryRequest.getId();
//        String title = messageQueryRequest.getTitle();
//        String content = messageQueryRequest.getContent();
//        List<String> tagList = messageQueryRequest.getTags();
//        Long userId = messageQueryRequest.getUserId();
//        Long notId = messageQueryRequest.getNotId();
//        // 拼接查询条件
//        if (StringUtils.isNotBlank(searchText)) {
//            queryWrapper.like("title", searchText).or().like("content", searchText);
//        }
//        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
//        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
//        if (CollectionUtils.isNotEmpty(tagList)) {
//            for (String tag : tagList) {
//                queryWrapper.like("tags", "\"" + tag + "\"");
//            }
//        }
//        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
//        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
//        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
//        queryWrapper.eq("isDelete", false);
//        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
//                sortField);
//        return queryWrapper;
//    }
//
//    @Override
//    public Page<Message> searchFromEs(MessageQueryRequest postQueryRequest) {
//        Long id = postQueryRequest.getId();
//        Long notId = postQueryRequest.getNotId();
//        String searchText = postQueryRequest.getSearchText();
//        String title = postQueryRequest.getTitle();
//        String content = postQueryRequest.getContent();
//        List<String> tagList = postQueryRequest.getTags();
//        List<String> orTagList = postQueryRequest.getOrTags();
//        Long userId = postQueryRequest.getUserId();
//        // es 起始页为 0
//        long current = postQueryRequest.getCurrent() - 1;
//        long pageSize = postQueryRequest.getPageSize();
//        String sortField = postQueryRequest.getSortField();
//        String sortOrder = postQueryRequest.getSortOrder();
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        // 过滤
//        boolQueryBuilder.filter(QueryBuilders.termQuery("isDelete", 0));
//        if (id != null) {
//            boolQueryBuilder.filter(QueryBuilders.termQuery("id", id));
//        }
//        if (notId != null) {
//            boolQueryBuilder.mustNot(QueryBuilders.termQuery("id", notId));
//        }
//        if (userId != null) {
//            boolQueryBuilder.filter(QueryBuilders.termQuery("userId", userId));
//        }
//        // 必须包含所有标签
//        if (CollectionUtils.isNotEmpty(tagList)) {
//            for (String tag : tagList) {
//                boolQueryBuilder.filter(QueryBuilders.termQuery("tags", tag));
//            }
//        }
//        // 包含任何一个标签即可
//        if (CollectionUtils.isNotEmpty(orTagList)) {
//            BoolQueryBuilder orTagBoolQueryBuilder = QueryBuilders.boolQuery();
//            for (String tag : orTagList) {
//                orTagBoolQueryBuilder.should(QueryBuilders.termQuery("tags", tag));
//            }
//            orTagBoolQueryBuilder.minimumShouldMatch(1);
//            boolQueryBuilder.filter(orTagBoolQueryBuilder);
//        }
//        // 按关键词检索
//        if (StringUtils.isNotBlank(searchText)) {
//            boolQueryBuilder.should(QueryBuilders.matchQuery("title", searchText));
//            boolQueryBuilder.should(QueryBuilders.matchQuery("content", searchText));
//            boolQueryBuilder.minimumShouldMatch(1);
//        }
//        // 按标题检索
//        if (StringUtils.isNotBlank(title)) {
//            boolQueryBuilder.should(QueryBuilders.matchQuery("title", title));
//            boolQueryBuilder.minimumShouldMatch(1);
//        }
//        // 按内容检索
//        if (StringUtils.isNotBlank(content)) {
//            boolQueryBuilder.should(QueryBuilders.matchQuery("content", content));
//            boolQueryBuilder.minimumShouldMatch(1);
//        }
//        // 排序
//        SortBuilder<?> sortBuilder = SortBuilders.scoreSort();
//        if (StringUtils.isNotBlank(sortField)) {
//            sortBuilder = SortBuilders.fieldSort(sortField);
//            sortBuilder.order(CommonConstant.SORT_ORDER_ASC.equals(sortOrder) ? SortOrder.ASC : SortOrder.DESC);
//        }
//        // 分页
//        PageRequest pageRequest = PageRequest.of((int) current, (int) pageSize);
//        // 构造查询
//        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder)
//                .withPageable(pageRequest).withSorts(sortBuilder).build();
//        SearchHits<PostEsDTO> searchHits = elasticsearchRestTemplate.search(searchQuery, PostEsDTO.class);
//        Page<Message> page = new Page<>();
//        page.setTotal(searchHits.getTotalHits());
//        List<Message> resourceList = new ArrayList<>();
//        // 查出结果后，从 db 获取最新动态数据（比如点赞数）
//        if (searchHits.hasSearchHits()) {
//            List<SearchHit<PostEsDTO>> searchHitList = searchHits.getSearchHits();
//            List<Long> postIdList = searchHitList.stream().map(searchHit -> searchHit.getContent().getId())
//                    .collect(Collectors.toList());
//            // 从数据库中取出更完整的数据
//            List<Message> postList = baseMapper.selectBatchIds(postIdList);
//            if (postList != null) {
//                Map<Long, List<Message>> idPostMap = postList.stream().collect(Collectors.groupingBy(Message::getId));
//                postIdList.forEach(postId -> {
//                    if (idPostMap.containsKey(postId)) {
//                        resourceList.add(idPostMap.get(postId).get(0));
//                    } else {
//                        // 从 es 清空 db 已物理删除的数据
//                        String delete = elasticsearchRestTemplate.delete(String.valueOf(postId), PostEsDTO.class);
//                        log.info("delete post {}", delete);
//                    }
//                });
//            }
//        }
//        page.setRecords(resourceList);
//        return page;
//    }
//
//    @Override
//    public MessageVO getMessageVO(Message message, HttpServletRequest request) {
//        return null;
//    }
//
////    @Override
////    public PostVO getPostVO(Post post, HttpServletRequest request) {
////        PostVO postVO = PostVO.objToVo(post);
////        long postId = post.getId();
////        // 1. 关联查询用户信息
////        Long userId = post.getUserId();
////        User user = null;
////        if (userId != null && userId > 0) {
////            user = userService.getById(userId);
////        }
////        UserVO userVO = userService.getUserVO(user);
////        postVO.setUser(userVO);
////        // 2. 已登录，获取用户点赞、收藏状态
////        User loginUser = userService.getLoginUserPermitNull(request);
////        if (loginUser != null) {
////            // 获取点赞
////            QueryWrapper<PostThumb> postThumbQueryWrapper = new QueryWrapper<>();
////            postThumbQueryWrapper.in("postId", postId);
////            postThumbQueryWrapper.eq("userId", loginUser.getId());
////            PostThumb postThumb = postThumbMapper.selectOne(postThumbQueryWrapper);
////            postVO.setHasThumb(postThumb != null);
////            // 获取收藏
////            QueryWrapper<PostFavour> postFavourQueryWrapper = new QueryWrapper<>();
////            postFavourQueryWrapper.in("postId", postId);
////            postFavourQueryWrapper.eq("userId", loginUser.getId());
////            PostFavour postFavour = postFavourMapper.selectOne(postFavourQueryWrapper);
////            postVO.setHasFavour(postFavour != null);
////        }
////        return postVO;
////    }
//
//    @Override
//    public Page<MessageVO> getMessageVOPage(Page<Message> postPage, HttpServletRequest request) {
//        List<Message> postList = postPage.getRecords();
//        Page<MessageVO> messageVOPage = new Page<>(postPage.getCurrent(), postPage.getSize(), postPage.getTotal());
//        if (CollectionUtils.isEmpty(postList)) {
//            return messageVOPage;
//        }
//        // 1. 关联查询用户信息
//        Set<Long> userIdSet = postList.stream().map(Message::getUserId).collect(Collectors.toSet());
//        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
//                .collect(Collectors.groupingBy(User::getId));
//        // 2. 已登录，获取用户点赞、收藏状态
//        Map<Long, Boolean> postIdHasThumbMap = new HashMap<>();
//        Map<Long, Boolean> postIdHasFavourMap = new HashMap<>();
//        User loginUser = userService.getLoginUserPermitNull(request);
////        if (loginUser != null) {
////            Set<Long> postIdSet = postList.stream().map(Message::getId).collect(Collectors.toSet());
////            loginUser = userService.getLoginUser(request);
////            // 获取点赞
////            QueryWrapper<PostThumb> postThumbQueryWrapper = new QueryWrapper<>();
////            postThumbQueryWrapper.in("postId", postIdSet);
////            postThumbQueryWrapper.eq("userId", loginUser.getId());
////            List<PostThumb> postPostThumbList = postThumbMapper.selectList(postThumbQueryWrapper);
////            postPostThumbList.forEach(postPostThumb -> postIdHasThumbMap.put(postPostThumb.getPostId(), true));
////            // 获取收藏
////            QueryWrapper<PostFavour> postFavourQueryWrapper = new QueryWrapper<>();
////            postFavourQueryWrapper.in("postId", postIdSet);
////            postFavourQueryWrapper.eq("userId", loginUser.getId());
////            List<PostFavour> postFavourList = postFavourMapper.selectList(postFavourQueryWrapper);
////            postFavourList.forEach(postFavour -> postIdHasFavourMap.put(postFavour.getPostId(), true));
////        }
//        // 填充信息
//        List<MessageVO> postVOList = postList.stream().map(post -> {
//            MessageVO postVO = MessageVO.objToVo(post);
//            Long userId = post.getUserId();
//            User user = null;
//            if (userIdUserListMap.containsKey(userId)) {
//                user = userIdUserListMap.get(userId).get(0);
//            }
//            postVO.setUser(userService.getUserVO(user));
//            postVO.setHasThumb(postIdHasThumbMap.getOrDefault(post.getId(), false));
//            postVO.setHasFavour(postIdHasFavourMap.getOrDefault(post.getId(), false));
//            return postVO;
//        }).collect(Collectors.toList());
//        messageVOPage.setRecords(postVOList);
//        return messageVOPage;
//    }
//
//    @Override
//    public Page<MessageVO> listMessageVOByPage(MessageQueryRequest postQueryRequest, HttpServletRequest request) {
//        long current = postQueryRequest.getCurrent();
//        long pageSize = postQueryRequest.getPageSize();
//        Page<Message> postPage = this.page(new Page<>(current, pageSize),
//                this.getQueryWrapper(postQueryRequest));
//        return this.getMessageVOPage(postPage, request);
//    }
//}
//
//
//
//
