package com.yupi.springbootinit.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.springbootinit.model.dto.picture.PictureQueryRequest;
import com.yupi.springbootinit.model.entity.Message;
import com.yupi.springbootinit.model.entity.Picture;

/**
* @author 86187
* @description 针对表【message(文章)】的数据库操作Service
* @createDate 2023-06-11 13:52:43
*/
public interface PictureService  {

    Page<Picture> searchPicture(PictureQueryRequest pictureQueryRequest, long pageNum, long pageSize);
}
