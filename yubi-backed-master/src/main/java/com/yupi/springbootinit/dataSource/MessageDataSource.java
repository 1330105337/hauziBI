//package com.yupi.springbootinit.dataSource;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.yupi.springbootinit.model.dto.message.MessageQueryRequest;
//import com.yupi.springbootinit.model.entity.Message;
//import com.yupi.springbootinit.model.vo.MessageVO;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//@Service
//@Slf4j
//public class MessageDataSource implements DataSource<MessageVO>{
//
//    @Resource
//    private MessageService messageService;
//    @Override
//    public Page<MessageVO> doSearch(String searchText, long pageNum, long pageSize ) {
//        MessageQueryRequest messageQueryRequest = new MessageQueryRequest();
//        messageQueryRequest.setSearchText(searchText);
//        messageQueryRequest.setCurrent(pageNum);
//        messageQueryRequest.setPageSize(pageSize);
//        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = servletRequestAttributes.getRequest();
//        log.info(request.toString());
//        Page<Message> messagePage = messageService.searchFromEs(messageQueryRequest);
//        return messageService.getMessageVOPage(messagePage,request);
//    }
//}
