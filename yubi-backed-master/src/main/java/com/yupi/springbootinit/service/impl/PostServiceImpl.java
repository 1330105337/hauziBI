package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.service.PostService;
import com.yupi.springbootinit.mapper.PostMapper;
import org.springframework.stereotype.Service;

/**
* @author 86187
* @description 针对表【post】的数据库操作Service实现
* @createDate 2023-06-09 16:51:35
*/
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
    implements PostService{

}




