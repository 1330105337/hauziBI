package com.yupi.springbootinit.model.dto.post;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件上传请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class GenResponseByAiRequest implements Serializable {
    /**
     * 问题名称
     */
    private String name;

    /**
     * 问题内容
     */
    private String question;
    private static final long serialVersionUID = 1L;
}