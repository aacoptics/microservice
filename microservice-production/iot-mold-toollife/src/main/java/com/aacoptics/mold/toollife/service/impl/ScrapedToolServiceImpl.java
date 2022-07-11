package com.aacoptics.mold.toollife.service.impl;

import com.aacoptics.mold.toollife.dao.ScrapedToolMapper;
import com.aacoptics.mold.toollife.entity.ScrapedTool;
import com.aacoptics.mold.toollife.service.ScrapedToolService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@DS("moldMes")
public class ScrapedToolServiceImpl extends ServiceImpl<ScrapedToolMapper, ScrapedTool> implements ScrapedToolService {
    @Autowired
    ScrapedToolMapper scrapedToolMapper;

    @Override
    public List<ScrapedTool> getScrapedList(String startTime) {
        return scrapedToolMapper.getScrapedList(startTime);
    }
}