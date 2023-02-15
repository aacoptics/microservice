import request from '@/utils/request'




export function getAnalysisData(queryForm) {
    return request({
        url: '/lens-fanuc-ne/fanucDashboard/getAnalysisData',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: queryForm
    })
}

export function getCycleNosByTime(queryForm) {
    return request({
        url: '/lens-fanuc-ne/fanucWaveData/getCycleNosByTime',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: queryForm
    })
}

export function getWaveDataByCycleNo(queryForm) {
    return request({
        url: '/lens-fanuc-ne/fanucWaveData/getWaveDataByCycleNo',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: queryForm
    })
}
