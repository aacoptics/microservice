package com.aacoptic.sep.controller;

import com.aacoptic.sep.entity.form.QueryForm;
import com.aacoptic.sep.service.SepClientService;
import com.aacoptics.common.core.vo.Result;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

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

    @ApiOperation(value = "修改Clinet组", notes = "修改Clinet组")
    @PostMapping("/changeToDefault")
    public Result changeToDefault(@RequestBody QueryForm queryForm) {
        queryForm.setGroup("default");
        return sepClientService.ChangeGroup(queryForm);
    }
}