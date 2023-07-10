//package com.yupi.springbootinit.service;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.yupi.springbootinit.model.dto.message.MessageQueryRequest;
//import com.yupi.springbootinit.model.entity.Message;
//import com.baomidou.mybatisplus.extension.service.IService;
//import com.yupi.springbootinit.model.vo.MessageVO;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
//* @author 86187
//* @description 针对表【message(文章)】的数据库操作Service
//* @createDate 2023-06-11 13:52:43
//*/
//public interface MessageService extends IService<Message> {
//
//
//    /**
//     * 校验
//     *
//     * @param message
//     * @param add
//     */
////    void validMessage(Message message, boolean add);
//
//    /**
//     * 获取查询条件
//     *
//     * @param postQueryRequest
//     * @return
//     */
//    QueryWrapper<Message> getQueryWrapper(MessageQueryRequest postQueryRequest);
//
//    /**
//     * 从 ES 查询
//     *
//     * @param postQueryRequest
//     * @return
//     */
//    Page<Message> searchFromEs(MessageQueryRequest postQueryRequest);
//
//    /**
//     * 获取帖子封装
//     *
//     * @param message
//     * @param request
//     * @return
//     */
//    MessageVO getMessageVO(Message message, HttpServletRequest request);
//
//    /**
//     * 分页获取帖子封装
//     *
//     * @param postPage
//     * @param request
//     * @return
//     */
//    Page<MessageVO> getMessageVOPage(Page<Message> postPage, HttpServletRequest request);
//
//    /**
//     * 分页查询帖子
//     * @param postQueryRequest
//     * @param request
//     * @return
//     */
//    Page<MessageVO> listMessageVOByPage(MessageQueryRequest postQueryRequest, HttpServletRequest request);
//
//}
