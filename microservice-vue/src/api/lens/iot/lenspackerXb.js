import request from '@/utils/request'

export function getAlarmDetail(startTime, endTime, machineName) {
    return request({
        url: '/lenspacker-xb/lenspackerDashboard/getAlarmDetail',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            startTime: startTime,
            endTime: endTime,
            machineName: machineName
        }
    })
}

export function getAlarmCount(startTime, endTime, machineName) {
    return request({
        url: '/lenspacker-xb/lenspackerDashboard/getAlarmCount',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            startTime: startTime,
            endTime: endTime,
            machineName: machineName
        }

    })
}

export function getMachineList() {
    return request({
        url: '/lenspacker-xb/lenspackerDashboard/getMachineList',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function getMachineCapacity(startTime, endTime) {
    return request({
        url: '/lenspacker-xb/lenspackerDashboard/getMachineCapacity',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            startTime: startTime,
            endTime: endTime
        }
    })
}

export function getMachineStatus() {
    return request({
        url: '/lenspacker-xb/lenspackerDashboard/getAllStatus',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function getMachineAlarms() {
    return request({
        url: '/lenspacker-xb/lenspackerDashboard/getCurrentAlarm',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function getLensPackerStatusCount() {
    return request({
        url: '/lenspacker-xb/lenspackerDashboard/getStatusCount',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}
