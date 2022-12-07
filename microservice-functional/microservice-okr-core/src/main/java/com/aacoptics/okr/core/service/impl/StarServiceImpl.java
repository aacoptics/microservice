package com.aacoptics.okr.core.service.impl;

import com.aacoptics.okr.core.entity.po.Star;
import com.aacoptics.okr.core.mapper.StarMapper;
import com.aacoptics.okr.core.service.StarService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Kaizhi Xuan
 * created on 2022/12/7 15:21
 */
@Service
@Slf4j
public class StarServiceImpl extends ServiceImpl<StarMapper, Star> implements StarService {

    @Override
    public boolean isStar(String userName, String starUserName) {
        return this.count(new LambdaQueryWrapper<Star>().eq(Star::getUserName, userName).eq(Star::getStartUserName, starUserName)) > 0;
    }

    @Override
    public boolean deleteOrCreate(String userName, String starUserName) {
        if (this.isStar(userName, starUserName)) {
            return this.remove(new LambdaQueryWrapper<Star>().eq(Star::getUserName, userName).eq(Star::getStartUserName, starUserName));
        } else {
            return this.save(Star.builder().userName(userName).startUserName(starUserName).build());
        }
    }

    @Override
    public List<String> employeeNoToStarUser(String employeeNo) {
        return this.list(new LambdaQueryWrapper<Star>().eq(Star::getUserName, employeeNo).orderByDesc(Star::getCreatedTime).select(Star::getStartUserName))
                .stream().map(Star::getStartUserName).collect(Collectors.toList());
    }
}
