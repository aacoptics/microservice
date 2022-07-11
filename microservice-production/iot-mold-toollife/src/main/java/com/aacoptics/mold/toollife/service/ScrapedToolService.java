package com.aacoptics.mold.toollife.service;

import com.aacoptics.mold.toollife.entity.ScrapedTool;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface ScrapedToolService extends IService<ScrapedTool> {
    List<ScrapedTool> getScrapedList(String startTime);
}