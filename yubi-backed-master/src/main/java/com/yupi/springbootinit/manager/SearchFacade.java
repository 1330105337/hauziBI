//package com.yupi.springbootinit.manager;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.yupi.springbootinit.common.ErrorCode;
//import com.yupi.springbootinit.dataSource.DataSource;
//import com.yupi.springbootinit.dataSource.MessageDataSource;
//import com.yupi.springbootinit.dataSource.PictureDataSource;
//import com.yupi.springbootinit.dataSource.UserDataSource;
//import com.yupi.springbootinit.exception.BusinessException;
//import com.yupi.springbootinit.exception.ThrowUtils;
//import com.yupi.springbootinit.model.dto.message.MessageQueryRequest;
//import com.yupi.springbootinit.model.dto.search.SearchRequest;
//import com.yupi.springbootinit.model.dto.user.UserQueryRequest;
//import com.yupi.springbootinit.model.entity.Picture;
//import com.yupi.springbootinit.model.enums.SearchTypeEnum;
//import com.yupi.springbootinit.model.vo.MessageVO;
//import com.yupi.springbootinit.model.vo.SearchVO;
//import com.yupi.springbootinit.model.vo.UserVO;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.poi.ss.formula.functions.T;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.CompletableFuture;
//@Component
//@Slf4j
//public class SearchFacade {
//
//    @Resource
//    private UserDataSource userDataSource;
//    @Resource
//    private MessageDataSource messageDataSource;
//    @Resource
//    private PictureDataSource pictureDataSource;
//
//    public SearchVO searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
//        String searchText = searchRequest.getSearchText();
//        long current = searchRequest.getCurrent();
//        long pageSize = searchRequest.getPageSize();
//        String type = searchRequest.getType();
//        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);
//        ThrowUtils.throwIf(StringUtils.isBlank(type), ErrorCode.PARAMS_ERROR);
//        //如果没有类型，将全部查出
//        if (searchTypeEnum==null){
//            CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(() -> {
//                UserQueryRequest userQueryRequest = new UserQueryRequest();
//                userQueryRequest.setUserName(searchText);
//                Page<UserVO> userVOPage = userDataSource.doSearch(searchText, current, pageSize);
//                return userVOPage;
//            });
//
//            CompletableFuture<Page<MessageVO>> messageTask = CompletableFuture.supplyAsync(() -> {
//                MessageQueryRequest messageQueryRequest = new MessageQueryRequest();
//                messageQueryRequest.setSearchText(searchText);
//                Page<MessageVO> messageVOPage = messageDataSource.doSearch(searchText, current, pageSize);
//                return messageVOPage;
//            });
//
//            CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(() -> {
//                Page<Picture> picturePage = pictureDataSource.doSearch(searchText, 1, 10);
//                return picturePage;
//            });
//
//            CompletableFuture.allOf(userTask, messageTask, pictureTask).join();
//            try {
//                Page<UserVO> userVOPage = userTask.get();
//                Page<MessageVO> messageVOPage = messageTask.get();
//                Page<Picture> picturePage = pictureTask.get();
//                SearchVO searchVO = new SearchVO();
//                searchVO.setUserList(userVOPage.getRecords());
//                searchVO.setMessageList(messageVOPage.getRecords());
//                searchVO.setPictureList(picturePage.getRecords());
//                return searchVO;
//            } catch (Exception e) {
//                log.error("查询异常", e);
//                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询异常");
//            }
//        }else {
//            //传入的type是啥就返回啥,将传入的类型为key，值为类型对应的方法，根据传入的type获取对应的值，然后得到对应的page进行返回
//            Map<String, DataSource<T>> typeDataSourcesMap = new HashMap() {{
//                put(SearchTypeEnum.MESSAGE.getValue(), messageDataSource);
//                put(SearchTypeEnum.USER.getValue(), userDataSource);
//                put(SearchTypeEnum.PICTURE.getValue(), pictureDataSource);
//            }};
//            SearchVO searchVO = new SearchVO();
//            DataSource<?> dataSource = typeDataSourcesMap.get(type);
//            Page<?> page = dataSource.doSearch(searchText, current, pageSize);
//            searchVO.setDataList(page.getRecords());
//            return searchVO;
//        }
//
//    }
//}
