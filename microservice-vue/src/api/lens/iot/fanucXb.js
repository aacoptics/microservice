import request from '@/utils/request'

export function getByFloor(floor) {
    return request({
        url: '/fanuc-xb/fanucDashboard/getByFloor',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            floor: floor
        }
    })
}

export function getFanucStatusCount() {
    return request({
        url: '/fanuc-xb/fanucDashboard/getStatusCount',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function selectEquips() {
    return request({
        url: '/fanuc-xb/fanucDashboard/selectEquips',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function getDetailInfo(machineName) {
    return request({
        url: '/fanuc-xb/fanucDashboard/getDetailInfo',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            machineName: machineName
        }
    })
}

export function getCondData(startTime, endTime, machineName) {
    return request({
        url: '/fanuc-xb/fanucDashboard/getCondData',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: {
            startTime: startTime,
            endTime: endTime,
            machineName: machineName
        }
    })
}

export function getMonitData(startTime, endTime, machineName) {
    return request({
        url: '/fanuc-xb/fanucDashboard/getMonitData',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: {
            startTime: startTime,
            endTime: endTime,
            machineName: machineName
        }
    })
}

export function getAlarmData(startTime, endTime, machineName) {
    return request({
        url: '/fanuc-xb/fanucDashboard/getAlarmData',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: {
            startTime: startTime,
            endTime: endTime,
            machineName: machineName
        }
    })
}

export function getAllCycleList(startTime, endTime) {
    return request({
        url: '/fanuc-xb/fanucDashboard/getAllCycleList',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: {
            startTime: startTime,
            endTime: endTime
        }
    })
}
