package com.aacoptics.feishu.photo.service;


import com.aacoptics.feishu.photo.entity.po.EmployeePhoto;

import java.util.List;

public interface EmployeePhotoService {


    List<EmployeePhoto> getPhotoByCode(String code);

    List<EmployeePhoto> listPhotoNeedToUpload();
}
