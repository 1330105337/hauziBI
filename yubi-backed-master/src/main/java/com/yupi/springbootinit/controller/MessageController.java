//package com.yupi.springbootinit.controller;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.google.gson.Gson;
//
//import java.util.List;
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//
//import com.yupi.springbootinit.annotation.AuthCheck;
//import com.yupi.springbootinit.common.BaseResponse;
//import com.yupi.springbootinit.common.DeleteRequest;
//import com.yupi.springbootinit.common.ErrorCode;
//import com.yupi.springbootinit.common.ResultUtils;
//import com.yupi.springbootinit.constant.UserConstant;
//import com.yupi.springbootinit.exception.BusinessException;
//import com.yupi.springbootinit.exception.ThrowUtils;
//import com.yupi.springbootinit.model.dto.message.MessageQueryRequest;
//import com.yupi.springbootinit.model.entity.Message;
//import com.yupi.springbootinit.model.entity.User;
//import com.yupi.springbootinit.model.vo.MessageVO;
//import com.yupi.springbootinit.service.MessageService;
//import com.yupi.springbootinit.service.UserService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.BeanUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * 帖子接口
// *
// * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
// * @from <a href="https://yupi.icu">编程导航知识星球</a>
// */
//@RestController
//@RequestMapping("/message")
//@Slf4j
//public class MessageController {
//
//    @Resource
//    private MessageService messageService;
//
//    @Resource
//    private UserService userService;
//
//    private final static Gson GSON = new Gson();
//
//
//
//    /**
//     * 分页获取列表（封装类）
//     *
//     * @param postQueryRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/list/page/vo")
//    public BaseResponse<Page<MessageVO>> listMessageVOByPage(@RequestBody MessageQueryRequest postQueryRequest,
//                                                       HttpServletRequest request) {
//        long current = postQueryRequest.getCurrent();
//        long size = postQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        Page<MessageVO> postVOPage = messageService.listMessageVOByPage(postQueryRequest, request);
//        return ResultUtils.success(postVOPage);
//    }
//
//    /**
//     * 分页获取当前用户创建的资源列表
//     *
//     * @param messageQueryRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/my/list/page/vo")
//    public BaseResponse<Page<MessageVO>> listMyMessageVOByPage(@RequestBody MessageQueryRequest messageQueryRequest,
//                                                            HttpServletRequest request) {
//        if (messageQueryRequest == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        User loginUser = userService.getLoginUser(request);
//         messageQueryRequest.setUserId(loginUser.getId());
//        long current = messageQueryRequest.getCurrent();
//        long size = messageQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        Page<Message> messagePage = messageService.page(new Page<>(current, size),
//            messageService.getQueryWrapper(messageQueryRequest));
//        return ResultUtils.success(messageService.getMessageVOPage(messagePage, request));
//    }
//
//    // endregion
//
//    /**
//     * 分页搜索（从 ES 查询，封装类）
//     *
//     * @param messageQueryRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/search/page/vo")
//    public BaseResponse<Page<MessageVO>> searchPostVOByPage(@RequestBody MessageQueryRequest messageQueryRequest,
//                                                         HttpServletRequest request) {
//        long size = messageQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        Page<Message> postPage = messageService.searchFromEs(messageQueryRequest);
//        return ResultUtils.success(messageService.getMessageVOPage(postPage, request));
//    }
//
////    /**
////     * 编辑（用户）
////     *
////     * @param postEditRequest
////     * @param request
////     * @return
////     */
////    @PostMapping("/edit")
////    public BaseResponse<Boolean> editPost(@RequestBody PostEditRequest postEditRequest, HttpServletRequest request) {
////        if (postEditRequest == null || postEditRequest.getId() <= 0) {
////            throw new BusinessException(ErrorCode.PARAMS_ERROR);
////        }
////   Message post = new Post();
////        BeanUtils.copyProperties(postEditRequest, post);
////        List<String> tags = postEditRequest.getTags();
////        if (tags != null) {
////        Message.setTags(GSON.toJson(tags));
////        }
////        // 参数校验
////    MessageService.validPost(post, false);
////        User loginUser = userService.getLoginUser(request);
////        long id = postEditRequest.getId();
////        // 判断是否存在
////   Message oldPost = postService.getById(id);
////        ThrowUtils.throwIf(oldPost == null, ErrorCode.NOT_FOUND_ERROR);
////        // 仅本人或管理员可编辑
////        if (!oldPost.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
////            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
////        }
////        boolean result = postService.updateById(post);
////        return ResultUtils.success(result);
////    }
//
//}
