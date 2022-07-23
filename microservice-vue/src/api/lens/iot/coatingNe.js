import request from '@/utils/request'

export function getStatus() {
    return request({
        url: '/coating-xny/coatingDashboard/getStatus',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function getAlarmInfo() {
    return request({
        url: '/coating-xny/coatingDashboard/getAlarmInfo',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function getCoatingStatusCount() {
    return request({
        url: '/coating-xny/coatingDashboard/getStatusCount',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}
