package com.yupi.springbootinit.model.dto.post;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;

@Data
public class PostEditRequest {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
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
     * 生成的回答
     */
    private String genResult;


}
