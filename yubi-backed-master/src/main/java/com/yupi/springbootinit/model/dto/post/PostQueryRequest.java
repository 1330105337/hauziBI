package com.yupi.springbootinit.model.dto.post;


import com.yupi.springbootinit.common.PageRequest;
import lombok.Data;



@Data
public class PostQueryRequest extends PageRequest {

    private Long id;

    /**
     * 问题名称
     */
    private String name;

    /**
     * 问题内容
     */
    private String question;


    /**
     * 创建用户 id
     */
    private Long userId;


}
