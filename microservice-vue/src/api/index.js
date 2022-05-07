import request from '../utils/request';
import {getExpireTime, getRefreshTime, setRefreshTime} from "@/utils/auth";

export const fetchData = query => {
    return request({
        url: './table.json',
        method: 'get',
        params: query
    });
};

export const saveRefreshTime = () => {
    let nowTime = new Date();
    let lastRefreshTime = getRefreshTime() ? new Date(getRefreshTime()) : new Date(-1);
    let expireTime = new Date(Date.parse(getExpireTime()))

    let refreshCount = 120; //滑动系数
    if (lastRefreshTime >= nowTime) {
        lastRefreshTime = nowTime > expireTime ? nowTime : expireTime;
        lastRefreshTime.setMinutes(lastRefreshTime.getMinutes() + refreshCount);
        setRefreshTime(lastRefreshTime);
    } else {
        setRefreshTime(new Date(-1));
    }
};
