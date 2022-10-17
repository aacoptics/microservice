package com.aacoptics.hikvision.api.service;


import com.aacoptics.hikvision.api.entity.param.DoorEventParam;
import com.aacoptics.hikvision.api.entity.vo.DoorEventDetail;
import com.aacoptics.hikvision.api.entity.vo.HikvisionApiPage;
import com.aacoptics.hikvision.api.entity.vo.HikvisionApiResult;


public interface HikvisionApiService {
    HikvisionApiPage<DoorEventDetail> getDoorEvents(DoorEventParam doorEventParam);
}
