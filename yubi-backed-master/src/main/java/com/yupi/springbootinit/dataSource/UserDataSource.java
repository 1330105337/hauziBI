package com.yupi.springbootinit.dataSource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.model.dto.user.UserQueryRequest;
import com.yupi.springbootinit.model.vo.UserVO;
import com.yupi.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;

@Service
@Slf4j
public class UserDataSource implements DataSource<UserVO>{

    @Resource
    private UserService userService;
    @Override
    public Page<UserVO> doSearch(String searchText, long pageNum, long pageSize) {

        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setCurrent(pageNum);
        userQueryRequest.setPageSize(pageSize);
        userQueryRequest.setUserName(searchText);
        Page<UserVO> userVOPage = userService.lisUserVoByPage(userQueryRequest);
        return userVOPage;
    }
}
