import request from '@/utils/request'

export function getByFloor(floor) {
    return request({
        url: '/lens-fanuc-sz/fanucDashboard/getByFloor',
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
        url: '/lens-fanuc-sz/fanucDashboard/getStatusCount',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function selectEquips() {
    return request({
        url: '/lens-fanuc-sz/fanucDashboard/selectEquips',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function getDetailInfo(machineName) {
    return request({
        url: '/lens-fanuc-sz/fanucDashboard/getDetailInfo',
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
        url: '/lens-fanuc-sz/fanucDashboard/getCondData',
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
        url: '/lens-fanuc-sz/fanucDashboard/getMonitData',
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
        url: '/lens-fanuc-sz/fanucDashboard/getAlarmData',
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
        url: '/lens-fanuc-sz/fanucDashboard/getAllCycleList',
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

export function getLastMouthEnergy() {
    return request({
        url: '/lens-fanuc-sz/fanucDigital/getLastMouthEnergy',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function getCurrentEnergy() {
    return request({
        url: '/lens-fanuc-sz/fanucDigital/getCurrentEnergy',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}
