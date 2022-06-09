package com.aacoptics.pack.service;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.pack.entity.dto.QtPackageParam;

public interface IUploadPackageInfoService {

    QtPackageParam getQtPackageInfo(String customer, String orderNo, String asnNo, String emsNo);

    Result uploadPackageInfo(QtPackageParam qtPackageParam);
}
