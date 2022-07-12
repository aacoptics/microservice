import request from '@/utils/request'

export function uploadExcel(param) {
    const formData = new FormData()
    formData.append('file', param.file)
    return request({
        url: '/mold-toolLife/toolLife/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function getByMonitorNo(monitorNo) {
    return request({
        url: '/mold-toolLife/toolLife/getByMonitorNo',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            monitorNo: monitorNo
        }
    })
}

export function getByMonitorNoAndMachineNo(monitorNo, machineNo) {
    return request({
        url: '/mold-toolLife/toolLife/getMachineNoByMonitorNo',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            monitorNo: monitorNo,
            machineNo: machineNo
        }
    })
}

export function updateToolInfo(param) {
    return request({
        url: '/mold-toolLife/toolLife/updateToolInfo',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: param
    })
}

export function getMachineList() {
    return request({
        url: '/mold-toolLife/toolLife/allMachine',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function getAbnormalType() {
    return request({
        url: '/mold-toolLife/toolLife/getAbnormalType',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function getMatInfoList() {
    return request({
        url: '/mold-toolLife/toolLife/allMatInfo',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function getToolMaintainStatus(param) {
    return request({
        url: '/mold-toolLife/toolLife/getToolMaintainStatus',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: param
    })
}

export function addAbnormalReason(param) {
    return request({
        url: '/mold-toolLife/toolLife/addAbnormalReason',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: param
    })
}

export function confirmAbnormalReason(param) {
    return request({
        url: '/mold-toolLife/toolLife/confirmAbnormalReason',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: param
    })
}

export function getLastDayTotalTime(startTime) {
    return request({
        url: '/mold-toolLife/toolLife/getLastDayOee',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            startTime: startTime
        }
    })
}

export function getToolHisList(toolCode) {
    return request({
        url: '/mold-toolLife/toolLife/getToolHisList',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            toolCode: toolCode
        }
    })
}

export function getLastDayAbnormalCount(startTime, endTime) {
    return request({
        url: '/mold-toolLife/toolLife/getLastDayAbnormalCount',
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

export function getLastDayScrapCount(startTime) {
    return request({
        url: '/mold-toolLife/toolLife/getLastDayScrapCount',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            startTime: startTime
        }
    })
}

export function getAreaInfo() {
    return request({
        url: '/mold-toolLife/toolLife/getAreaInfo',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function getAbnormalList() {
    return request({
        url: '/mold-toolLife/toolLife/getAbnormalList',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function getAbnormalToolLifeRatio() {
    return request({
        url: '/mold-toolLife/toolLife/abnormalToolLifeRatio',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function getAbnormalQty() {
    return request({
        url: '/mold-toolLife/toolLife/abnormalQty',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}



