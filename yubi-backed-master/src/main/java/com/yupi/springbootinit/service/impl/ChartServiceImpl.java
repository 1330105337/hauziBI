package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.model.entity.Chart;
import com.yupi.springbootinit.service.ChartService;
import com.yupi.springbootinit.mapper.ChartMapper;
import org.springframework.stereotype.Service;

/**
* @author 86187
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2023-05-29 22:10:26
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService{

    @Override
    public void handleChartUpdateError(Long chartId, String execMessage) {

            Chart updateChartResult = new Chart();
            updateChartResult.setId(chartId);
            updateChartResult.setStatus("failed");
            updateChartResult.setExecMessage("execMessage");
            boolean updateResult = this.updateById(updateChartResult);
            if (!updateResult){
                log.error("更新图表失败"+chartId+","+execMessage);
            }
        }
    }





