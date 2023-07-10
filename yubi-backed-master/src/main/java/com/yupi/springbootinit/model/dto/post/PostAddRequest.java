package com.yupi.springbootinit.model.dto.post;


import lombok.Data;


@Data
public class PostAddRequest {
    /**
     * 问题名称
     */
    private String name;

    /**
     * 问题内容
     */
    private String question;

}
