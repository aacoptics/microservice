import request from '@/utils/request'


export function getMachineName() {
    return request({
        url: '/wlg-iot-dashboard/moldingMachineParam/getMoldingMachineName',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
    })
}

export function getWaferIds(machineName, startTime, endTime) {
    return request({
        url: '/wlg-iot-dashboard/moldingMachineParam/getWaferIds',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            machineName: machineName,
            startTime: startTime,
            endTime: endTime
        }
    })
}

export function getMachineEvents(machineName, startTime, endTime, current, size) {
    return request({
        url: '/wlg-iot-dashboard/moldingMachineParam/getMachineEvents',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            machineName: machineName,
            startTime: startTime,
            endTime: endTime,
            current: current,
            size: size
        }
    })
}

export function getMachineErrors(machineName, startTime, endTime, current, size) {
    return request({
        url: '/wlg-iot-dashboard/moldingMachineParam/getMachineErrors',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            machineName: machineName,
            startTime: startTime,
            endTime: endTime,
            current: current,
            size: size
        }
    })
}

export function getMachineAbnormalData(machineName, startTime, endTime, current, size) {
    return request({
        url: '/wlg-iot-dashboard/moldingMachineParam/getMachineAbnormalData',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            machineName: machineName,
            startTime: startTime,
            endTime: endTime,
            current: current,
            size: size
        }
    })
}

export function getMoldParamName(params) {
    return request({
        url: '/wlg-iot-dashboard/moldingMachineParam/getMoldParamName',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: params
    })
}

export function getMoldParamValue(params) {
    return request({
        url: '/wlg-iot-dashboard/moldingMachineParam/getMoldParamArray',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: params
    })
}

export function updateOutPutInfo(params) {
    return request({
        url: '/wlg-iot-dashboard/InputReport/updateOutPutInfo',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: params
    })
}

export function getByDateAndMachineName(params) {
    return request({
        url: '/wlg-iot-dashboard/InputReport/getByDateAndMachineName',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: params
    })
}

export function getMoldingInfo(machineName, current, size) {
    return request({
        url: '/wlg-iot-dashboard/moldingMachine/getMachineInfo',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            machineName: machineName,
            current: current,
            size: size
        }
    })
}

export function getParamThreshold(machineId, current, size) {
    return request({
        url: '/wlg-iot-dashboard/moldingMachine/getParamThreshold',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            machineId: machineId,
            current: current,
            size: size
        }
    })
}

export function deleteParamThreshold(deleteForm) {
    return request({
        url: '/wlg-iot-dashboard/moldingMachine/MoldingParamThreshold/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/wlg-iot-dashboard/moldingMachine/MoldingParamThreshold',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function getAnalysisData(queryForm) {
    return request({
        url: '/wlg-iot-dashboard/moldingMachineParam/getAnalysisData',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: queryForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/wlg-iot-dashboard/moldingMachine/MoldingParamThreshold/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function handleUpdateFeedingAlarm(updateForm) {
    return request({
        url: '/wlg-iot-dashboard/moldingMachine/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function handleCycleDetailUpdate(updateForm) {
    return request({
        url: '/wlg-iot-dashboard/CycleDetail/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function getCycleDetail(queryForm) {
    return request({
        url: '/wlg-iot-dashboard/CycleDetail/conditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: queryForm
    })
}

export function downloadExcel(queryForm) {
    return request({
        url: '/wlg-iot-dashboard/CycleDetail/downloadExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: queryForm
    })
}
