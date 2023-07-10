package com.yupi.springbootinit.service;

import com.yupi.springbootinit.model.entity.Chart;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86187
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2023-05-29 22:10:26
*/
public interface ChartService extends IService<Chart> {
    void handleChartUpdateError(Long chartId, String execMessage);

}
