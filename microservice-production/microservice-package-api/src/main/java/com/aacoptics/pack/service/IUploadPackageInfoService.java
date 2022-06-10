package com.aacoptics.pack.service;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.pack.entity.dto.QtPackageParam;
import com.aacoptics.pack.entity.form.CustomerShipmentInfoForm;

public interface IUploadPackageInfoService {

    QtPackageParam getQtPackageInfo(CustomerShipmentInfoForm customerShipmentInfoForm);

    Result uploadPackageInfo(CustomerShipmentInfoForm customerShipmentInfoForm);
}
