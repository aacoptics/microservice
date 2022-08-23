package com.aacoptics.mobile.attendance.config;

import com.aacoptics.mobile.attendance.entity.po.EmployeePhoto;
import com.aacoptics.mobile.attendance.service.EmployeePhotoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class FeishuAppKeyConfigTest {

    @Resource
    FeishuAppKeyConfig feishuAppKeyConfig;

    @Resource
    EmployeePhotoService employeePhotoService;

    @Test
    void test() throws NoSuchAlgorithmException {
        String test = calculateSignature("test", "Test", "WKoD2hpSrNBx7V380V65OcHfNkH5V6TU", "{\"encrypt\":\"p39GXdBNQ93ocP8xVKVf13+6w5V9T8PfAyzipOtYtY5wXciFh/mJxLcYEilnZsbwneA24+xk6vQ9HtFr0fLjS8LFxYhoNK+CwMTDf0OiWMoYoVQXPPXOHZYKQ8N4Y1zs9BtF59iqfcVKyvOM0FTnAxfcTEQg4wHfTEOX9vAaaXzw8jTB93wPiZ4OP9zOQZd3h4LmE9bh5AxCy85p8mDiR/bqtNfZ8dsh6tVG5INnC1tLaljHHe/9/9ARWk90Q/Rw9PPhvuxI3Q1t+DuwMDJjNW7rStn/ArXAcGu+ihEvR3ZtPY9wwW3yz3Je9pB20rDI5vgn+kuVxHvTYOC+mukciIZLgIfp9aBjxryFY8P5B/g3aq0Ae5wlouu0vTl5g4mp6yVbCJ/rHmZVrHTfujxO+AjCos6YoN6NVsALZ+5r7vuD5oNFaEC7R5O7/A0hXFXxpYRm9t1Ql9oyoZX5Tz52VqykbsX1JWzc5hkco7lwLtTQ16doxocAE1zC/eJ4WOUCjqJt/jOLuPfSGUCpdmmLMnG9RcoSiYAiGfbffI5sX1jOQ3EKnrGd1yxR6vGDj7OFpww+53lXZnxbMLfKYhITW3i6fWlxLcs+ovSstWMQfReA8VhnvZbkZvOTxKzumLJynDTnIQMLsclCbqI3RJQ7PyAiQ2QBSj1VI0krRNtkfSDIFlUazUiTi3E0kx9MQBoW4JVbMDGC4/ArFY3oeoGSL2n448lZ7XK3aKaeZh7qj8g=\"}");
        String asd = "";
    }

    @Test
    void test1() throws IOException {
        List<EmployeePhoto> test = employeePhotoService.listPhotoNeedToUpload();
        byte[] res = test.get(0).getPhoto();
        OutputStream sos = Files.newOutputStream(Paths.get("d:\\Documents and Settings\\60054916\\Desktop\\test\\test1.jpg"));
        sos.write(res, 0, res.length);
        sos.flush();
        sos.close();

    }

    public String calculateSignature(String timestamp, String nonce, String encryptKey, String bodyString) throws NoSuchAlgorithmException {
        MessageDigest alg = MessageDigest.getInstance("SHA-256");
        return Hex.encodeHexString(alg.digest((timestamp + nonce + encryptKey + bodyString).getBytes()));
    }
}