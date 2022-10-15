package com.aacoptics.jacob.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aacoptics.common.core.vo.Result;
import com.aacoptics.jacob.entity.vo.FeishuVoiceFileInfo;
import com.aacoptics.jacob.entity.vo.SpeakerVoiceFileInfo;
import com.aacoptics.jacob.entity.vo.VoiceFileInfo;
import com.aacoptics.jacob.provider.OkHttpCli;
import com.aacoptics.jacob.service.FeishuService;
import com.aacoptics.jacob.service.VoiceService;
import com.aacoptics.jacob.util.FileUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

@RestController
@RequestMapping("/voice")
@Api("voice")
@Slf4j
public class VoiceServiceController {
    @Resource
    VoiceService voiceService;

    @Resource
    FeishuService feishuService;

    @Resource
    OkHttpCli okHttpCli;

    @ApiOperation(value = "下载文字转化的语音信息", notes = "下载文字转化的语音信息")
    @ApiImplicitParam(name = "voiceFileInfo", value = "消息", required = true, dataType = "VoiceFileInfo")
    @PostMapping(value = "/download")
    public Result downloadVoiceFile(@RequestBody @Valid VoiceFileInfo voiceFileInfo, HttpServletResponse resp) {
        String fileName = String.valueOf(Calendar.getInstance().getTimeInMillis());
        String filePath = voiceService.generateVoiceFile(fileName, voiceFileInfo);
        if (filePath == null)
            return Result.fail("文字转语音失败！");
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return Result.fail("文件不存在");
            }
            FileUtils.responseTo(file, resp);
            file.delete();
            return Result.success("文件下载成功");
        } catch (Exception e) {
            return Result.fail("文件[" + fileName + "]下载错误");
        }
    }

    @ApiOperation(value = "发送语音至扬声器", notes = "发送语音至扬声器")
    @ApiImplicitParam(name = "speakerVoiceFileInfo", value = "消息", required = true, dataType = "SpeakerVoiceFileInfo")
    @PostMapping(value = "/sendToSpeaker")
    public Result sendToSpeaker(@RequestBody @Valid SpeakerVoiceFileInfo speakerVoiceFileInfo) {
        String fileName = String.valueOf(Calendar.getInstance().getTimeInMillis());
        String filePath = voiceService.generateVoiceFile(fileName, speakerVoiceFileInfo);
        if (filePath == null)
            return Result.fail("文字转语音失败！");
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return Result.fail("文件不存在");
            }
            String outFileName = String.valueOf(Calendar.getInstance().getTimeInMillis());
            String outFilePath = filePath.replace(fileName, outFileName);
            String outFileUrl = voiceService.formatVoiceFile(file, outFilePath);
            JSONObject object = JSONUtil.createObj()
                    .set("type", "req")
                    .set("name", "songs_queue_append")
                    .set("sn", speakerVoiceFileInfo.getSpeakerSn());
            JSONObject paramsObject = JSONUtil.createObj()
                    .set("tid", "localMusicPlay")
                    .set("vol", 100);
            JSONArray urlArray = JSONUtil.createArray();
            urlArray.add(JSONUtil.createObj()
                    .set("name", outFileName + ".mp3")
                    .set("uri", outFileUrl));
            paramsObject.set("urls", urlArray);
            object.set("params", paramsObject);
            Result result = okHttpCli.doPostJsonSpeaker(StrUtil.format("http://{}:{}", speakerVoiceFileInfo.getSpeakerIp(), speakerVoiceFileInfo.getSpeakerPort()), object);
            file.delete();
            return result;
        } catch (Exception e) {
            return Result.fail("文件[" + fileName + "]发送至扬声器失败");
        }
    }

    @ApiOperation(value = "发送飞书语音消息", notes = "发送飞书语音消息")
    @ApiImplicitParam(name = "voiceFileInfo", value = "消息", required = true, dataType = "VoiceFileInfo")
    @PostMapping(value = "/sendFeishuVoice")
    public Result sendFeishuVoiceMsg(@RequestBody @Valid FeishuVoiceFileInfo feishuVoiceFileInfo) {
        VoiceFileInfo voiceFileInfo = new FeishuVoiceFileInfo();
        BeanUtils.copyProperties(feishuVoiceFileInfo, voiceFileInfo);
        String fileName = String.valueOf(Calendar.getInstance().getTimeInMillis());
        String filePath = voiceService.generateVoiceFile(fileName, voiceFileInfo);
        if (filePath == null)
            return Result.fail("文字转语音失败！");
        String chatId = feishuService.fetchChatIdByRobot(feishuVoiceFileInfo.getGroupName());
        if (StrUtil.isBlank(chatId))
            return Result.fail("找不到该群！");
        try {
            String key = feishuService.fetchUploadFileKey(FeishuService.FILE_TYPE_OPUS, filePath, 0);
            if (feishuService.sendMessage(FeishuService.RECEIVE_ID_TYPE_CHAT_ID, chatId, FeishuService.MSG_TYPE_AUDIO, JSONUtil.createObj().set("file_key", key))) {
                File file = new File(filePath);
                if (file.exists())
                    file.delete();
                return Result.success();
            } else
                return Result.fail();
        } catch (IOException err) {
            return Result.fail(err);
        }
    }
}