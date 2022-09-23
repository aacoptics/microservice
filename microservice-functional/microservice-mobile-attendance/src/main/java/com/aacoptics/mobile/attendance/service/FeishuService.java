package com.aacoptics.mobile.attendance.service;

import com.aacoptics.mobile.attendance.entity.po.FeishuUser;

import java.io.IOException;

/**
 * @author Kaizhi Xuan
 * created on 2022/6/25 9:17
 */
public interface FeishuService {

    String EMPLOYEE_TYPE_USER_ID = "employee_id";

    String EMPLOYEE_TYPE_EMPLOYEE_NO = "employee_no";

    FeishuUser getFeishuUser(String employeeNo);

    String fetchAccessToken();

    String fetchUploadFileKey(String accessToken, String fileName, byte[] fileContent) throws IOException;

    boolean updateUserPhoto(String employeeNo, byte[] fileContent) throws IOException;


    void uploadUserPhotoFromSAP(int idFlag);
}
