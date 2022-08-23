package com.aacoptics.mobile.attendance.service;

import com.aacoptics.mobile.attendance.entity.po.EmployeePhoto;

import java.util.List;

public interface EmployeePhotoService {


    List<EmployeePhoto> getPhotoByCode(String code);

    List<EmployeePhoto> listPhotoNeedToUpload();
}
