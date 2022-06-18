package com.aacoptics.sep.controller;

import com.aacoptics.sep.entity.form.QueryForm;
import com.aacoptics.sep.service.SepClientService;
import com.aacoptics.common.core.vo.Result;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sep")
@Api("sep")
@Slf4j
public class SepController {
    @Resource
    SepClientService sepClientService;

    @ApiOperation(value = "修改Clinet组", notes = "修改Clinet组")
    @PostMapping("/changeGroup")
    public Result changeGroup(@RequestBody QueryForm queryForm) {
        return sepClientService.ChangeGroup(queryForm);
    }
}