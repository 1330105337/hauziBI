package com.yupi.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.DeleteRequest;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.constant.CommonConstant;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.manager.AiManager;
import com.yupi.springbootinit.manager.RedisLimiterManager;
import com.yupi.springbootinit.model.dto.post.GenResponseByAiRequest;
import com.yupi.springbootinit.model.dto.post.PostEditRequest;
import com.yupi.springbootinit.model.dto.post.PostQueryRequest;
import com.yupi.springbootinit.model.entity.Chart;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.model.entity.User;
import com.yupi.springbootinit.model.vo.BiResponse;
import com.yupi.springbootinit.model.vo.PostResponse;
import com.yupi.springbootinit.service.PostService;
import com.yupi.springbootinit.service.UserService;
import com.yupi.springbootinit.utils.ExcelUtils;
import com.yupi.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 帖子接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/post")
@Slf4j
public class PostController {

    @Resource
    private AiManager aiManager;
    @Resource
    private PostService postService;

    @Resource
    private UserService userService;

    @Resource
    private RedisLimiterManager redisLimiterManager;


    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    private final static Gson GSON = new Gson();

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deletePost(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Post oldPost = postService.getById(id);
        ThrowUtils.throwIf(oldPost == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldPost.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = postService.removeById(id);
        return ResultUtils.success(b);
    }


    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<Post> getPostVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Post post = postService.getById(id);
        if (post == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(post);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param postQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<Post>> listPostVOByPage(@RequestBody PostQueryRequest postQueryRequest,
                                                       HttpServletRequest request) {
        long current = postQueryRequest.getCurrent();
        long size = postQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Post> postPage = postService.page(new Page<>(current, size),
                getQueryWrapper(postQueryRequest));
        return ResultUtils.success(postPage);
    }

    private QueryWrapper<Post> getQueryWrapper(PostQueryRequest postQueryRequest) {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        Long id = postQueryRequest.getId();
        Long userId = postQueryRequest.getUserId();
        String sortField = postQueryRequest.getSortField();
        String sortOrder = postQueryRequest.getSortOrder();
        String question = postQueryRequest.getQuestion();
        String name = postQueryRequest.getName();

        queryWrapper.eq(id != null && id > 0, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.eq(StringUtils.isNotBlank(question), "question", question);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param postQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<Post>> listMyPostVOByPage(@RequestBody PostQueryRequest postQueryRequest,
                                                         HttpServletRequest request) {
        if (postQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        postQueryRequest.setUserId(loginUser.getId());
        long current = postQueryRequest.getCurrent();
        long size = postQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Post> postPage = postService.page(new Page<>(current, size),
               getQueryWrapper(postQueryRequest));
        return ResultUtils.success(postPage);
    }

    // endregion


    /**
     * 编辑（用户）
     *
     * @param postEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editPost(@RequestBody PostEditRequest postEditRequest, HttpServletRequest request) {
        if (postEditRequest == null || postEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Post post = new Post();
        BeanUtils.copyProperties(postEditRequest, post);
        User loginUser = userService.getLoginUser(request);
        long id = postEditRequest.getId();
        // 判断是否存在
        Post oldPost = postService.getById(id);
        ThrowUtils.throwIf(oldPost == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldPost.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = postService.updateById(post);
        return ResultUtils.success(result);
    }

    @PostMapping("/gen")
    public BaseResponse<PostResponse> genPostByAi( GenResponseByAiRequest genResponseByAiRequest, HttpServletRequest request) {
        String name = genResponseByAiRequest.getName();
        String question = genResponseByAiRequest.getQuestion();
        ThrowUtils.throwIf(StringUtils.isBlank(question), ErrorCode.NOT_FOUND_ERROR, "目标为空");
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.NOT_FOUND_ERROR, "名称过长");


        User loginUser = userService.getLoginUser(request);
        // 限流判断，每个用户一个限流器
        redisLimiterManager.doRateLimit("genChartByAi_" + loginUser.getId());
        long modeId = 1659171950288818178L;
        StringBuilder userInput = new StringBuilder();
        userInput.append("请教一个关于").append(name);
        String questionName = name;
        if (StringUtils.isNotBlank(question)) {
            questionName += "的问题，问题是" + question;
        }
        userInput.append(questionName);

        String genResult = aiManager.doChat(modeId, userInput.toString());
        Post post = new Post();
        post.setName(name);
        post.setGenResult(genResult);
        post.setUserId(loginUser.getId());
        post.setStatus("succeed");
        post.setQuestion(question);

        boolean save = postService.save(post);
        ThrowUtils.throwIf(!save, ErrorCode.SYSTEM_ERROR, "问题表保存失败");
        PostResponse postResponse = new PostResponse();
        postResponse.setPostId(post.getId());
        postResponse.setGenResult(genResult);
        return ResultUtils.success(postResponse);

    }

    /**
     * 问答异步
     * @param genResponseByAiRequest
     * @param request
     * @return
     */
    @PostMapping("/gen/async")
    public BaseResponse<PostResponse> genPostByAiAsync( GenResponseByAiRequest genResponseByAiRequest, HttpServletRequest request) {
        String name = genResponseByAiRequest.getName();
        String question = genResponseByAiRequest.getQuestion();
        ThrowUtils.throwIf(StringUtils.isBlank(question), ErrorCode.NOT_FOUND_ERROR, "目标为空");
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.NOT_FOUND_ERROR, "名称过长");

        User loginUser = userService.getLoginUser(request);
        // 限流判断，每个用户一个限流器
        redisLimiterManager.doRateLimit("genChartByAi_" + loginUser.getId());
        long modeId = 1659171950288818178L;
        // 分析需求：
        // 分析网站用户的增长情况
        // 原始数据：
        // 日期,用户数
        // 1号,10
        // 2号,20
        // 3号,30
        //用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("请教一个关于").append(name);
        String questionName = name;
        if (StringUtils.isNotBlank(question)) {
            questionName += "的问题，问题是" + question;
        }
        userInput.append(questionName);


        Post post = new Post();
        post.setName(name);
        post.setQuestion(question);
        post.setStatus("wait");
        post.setUserId(loginUser.getId());
        boolean saveResult = postService.save(post);
        ThrowUtils.throwIf(!saveResult,ErrorCode.SYSTEM_ERROR,"问题保存失败");

        CompletableFuture.runAsync(() -> {
            Post postResult = new Post();
            postResult.setId(post.getId());
            postResult.setStatus("running");
            boolean b = postService.updateById(postResult);
            if (!b){
                handleChartUpdateError(post.getId(),"更新问题表状态失败");
                return ;
            }
            String genResult = aiManager.doChat(modeId, userInput.toString());
            Post updatePostResult = new Post();
            updatePostResult.setId(post.getId());
            updatePostResult.setGenResult(genResult);
            updatePostResult.setStatus("succeed");
            log.info(genResult);
            boolean update = postService.updateById(updatePostResult);
            if (!update){
                handleChartUpdateError(post.getId(),"生成回答失败");
            }
        },threadPoolExecutor);
        PostResponse postResponse = new PostResponse();
        postResponse.setPostId(post.getId());
        return ResultUtils.success(postResponse);
    }

    private void handleChartUpdateError(Long postId, String execMessage) {
        Post updatePostResult = new Post();
        updatePostResult.setId(postId);
        updatePostResult.setStatus("failed");
        updatePostResult.setExecMessage("execMessage");
        boolean updateResult = postService.updateById(updatePostResult);
        if (!updateResult){
            log.error("更新图表失败"+postId+","+execMessage);
        }
    }
}
