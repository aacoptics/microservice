import request from '@/utils/request'

export function getFloorPlanMachineInfo(startNumber, endNumber) {
    return request({
        url: '/czech-iot/floorPlan/getMachineInfoByFloor',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            startNumber: startNumber,
            endNumber: endNumber
        }
    })
}

export function getAllFloorPlanMachineInfo() {
    return request({
        url: '/czech-iot/floorPlan/getAllMachineInfo',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function getMachineInfoByFloorNumber(floorNumber) {
    return request({
        url: '/czech-iot/floorPlan/getMachineInfoByFloorNumber',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            floorNumber: floorNumber
        }
    })
}

export function getMachineInfoByMachineNumber(machineNumber) {
    return request({
        url: '/czech-iot/floorPlan/getMachineInfoByMachineNumber',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            machineNumber: machineNumber
        }
    })
}

export function getSpindleTemperature(startTime, endTime, machineNumber) {
    return request({
        url: '/czech-iot/floorPlan/getSpindleTemperature',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            startTime: startTime,
            endTime: endTime,
            machineNumber: machineNumber
        }
    })
}

export function getAirTemperature(startTime, endTime, machineNumber) {
    return request({
        url: '/czech-iot/floorPlan/getAirTemperature',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            startTime: startTime,
            endTime: endTime,
            machineNumber: machineNumber
        }
    })
}

export function getBearingTemperature(startTime, endTime, machineNumber) {
    return request({
        url: '/czech-iot/floorPlan/getBearingTemperature',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            startTime: startTime,
            endTime: endTime,
            machineNumber: machineNumber
        }
    })
}

export function getMotorTemperature(startTime, endTime, machineNumber) {
    return request({
        url: '/czech-iot/floorPlan/getMotorTemperature',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            startTime: startTime,
            endTime: endTime,
            machineNumber: machineNumber
        }
    })
}

export function getStatusInfoByMachineNumber(machineNumber) {
    return request({
        url: '/czech-iot/floorPlan/getStatusInfoByMachineNumber',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            machineNumber: machineNumber
        }
    })
}

export function saveRemark(machineNumber, content) {
    return request({
        url: '/czech-iot/floorPlan/saveRemark',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            machineNumber: machineNumber,
            content: content
        }
    })
}

export function updateRemark(machineRemark, newContent) {
    return request({
        url: '/czech-iot/floorPlan/updateRemark',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: machineRemark,
        params: {
            newContent: newContent
        }
    })
}

export function deleteRemark(machineRemark) {
    return request({
        url: '/czech-iot/floorPlan/deleteRemark',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: machineRemark
    })
}

export function getRemarkByMachineNumber(machineNumber) {
    return request({
        url: '/czech-iot/floorPlan/getRemarkByMachineNumber',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            machineNumber: machineNumber
        }
    })
}