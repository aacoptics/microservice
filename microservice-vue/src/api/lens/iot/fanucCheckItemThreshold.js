import request from '@/utils/request'


export function findFanucCheckItemThresholdPage(conditions) {
    return request({
        url: '/lens-fanuc-sz/fanucCheckItemThreshold/conditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}
export function findFanucCheckItemThresholdList() {
    return request({
        url: '/lens-fanuc-sz/fanucCheckItemThreshold/findFanucCheckItemThresholdList',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
    })
}


export function findFanucCheckItemThresholdById(fanucCheckItemThresholdId) {
    return request({
        url: '/lens-fanuc-sz/fanucCheckItemThreshold/' + fanucCheckItemThresholdId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteFanucCheckItemThreshold(deleteForm) {
    return request({
        url: '/lens-fanuc-sz/fanucCheckItemThreshold/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/lens-fanuc-sz/fanucCheckItemThreshold',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/lens-fanuc-sz/fanucCheckItemThreshold/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}
