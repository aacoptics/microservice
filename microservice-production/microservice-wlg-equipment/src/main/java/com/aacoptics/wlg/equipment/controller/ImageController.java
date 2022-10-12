package com.aacoptics.wlg.equipment.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.wlg.equipment.entity.po.Image;
import com.aacoptics.wlg.equipment.exception.BusinessException;
import com.aacoptics.wlg.equipment.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;

@RestController
@RequestMapping("/image")
@Api("image")
@Slf4j
public class ImageController {

    @Autowired
    ImageService imageService;


    @ApiOperation(value = "新增图片", notes = "新增一个图片信息")
    @ApiImplicitParam(name = "imageForm", value = "新增图片form表单", required = true, dataType = "ImageForm")
    @PostMapping
    public Result add(@RequestParam("file") MultipartFile imgFile, HttpServletRequest request) {

        InputStream inputStream = null;

        Image image = null;
        try {
            String originalFilename = imgFile.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

            inputStream = imgFile.getInputStream();
            byte[] pictureData = new byte[(int) imgFile.getSize()];
            inputStream.read(pictureData);


            InputStream imageInputStream = imgFile.getInputStream();
            BufferedImage bufferedImage = ImageIO.read(imageInputStream);
            imageInputStream.close();
            int width = bufferedImage.getWidth(); //宽
            int height = bufferedImage.getHeight(); //高

            image = new Image();
            image.setFileName(originalFilename);
            image.setFileExtension(fileExtension);
            image.setImage(pictureData);
            image.setWidth(width);
            image.setHeight(height);



        } catch (IOException e) {
            log.error("上传图片异常", e);
            throw new BusinessException("上传图片异常，" + e.getLocalizedMessage());
        }
        imageService.add(image);
        try {
            inputStream.close();
        } catch (IOException e) {
            log.error("关闭输入流异常", e);
        }
        return Result.success(image);
    }

    @ApiOperation(value = "删除图片", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(paramType = "path", name = "id", value = "图片ID", example = "0", required = true, dataType = "Long")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Long id) {
        return Result.success(imageService.delete(id));
    }

    @ApiOperation(value = "修改图片", notes = "修改指定图片信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "图片ID", required = true, example = "0", dataType = "Long"),
            @ApiImplicitParam(name = "imageForm", value = "图片实体", required = true, dataType = "ImageForm")
    })
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Long id, @RequestParam("file") MultipartFile imgFile, HttpServletRequest request) {
        InputStream inputStream = null;
        Image image = null;
        try {
            String originalFilename = imgFile.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

            inputStream = imgFile.getInputStream();
            byte[] pictureData = new byte[(int) imgFile.getSize()];
            inputStream.read(pictureData);


            InputStream imageInputStream = imgFile.getInputStream();
            BufferedImage bufferedImage = ImageIO.read(imageInputStream);
            imageInputStream.close();


            int width = bufferedImage.getWidth(); //宽
            int height = bufferedImage.getHeight(); //高

            image = new Image();
            image.setId(id);
            image.setFileExtension(fileExtension);
            image.setFileName(originalFilename);
            image.setImage(pictureData);
            image.setWidth(width);
            image.setHeight(height);

        } catch (IOException e) {
            log.error("上传图片异常", e);
            throw new BusinessException("上传图片异常，" + e.getLocalizedMessage());
        }
        imageService.update(image);

        try {
            inputStream.close();
        } catch (IOException e) {
            log.error("关闭输入流异常", e);
        }
        return Result.success(image);
    }

    @SneakyThrows
    @ApiOperation(value = "获取图片", notes = "获取指定图片信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "图片ID", example = "0", required = true, dataType = "Long")
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable Long id) {
        log.debug("get with id:{}", id);
        Image image = imageService.get(id);
        if(image == null)
        {
            throw new BusinessException("图片不存在");
        }
        return Result.success(image);
    }

}