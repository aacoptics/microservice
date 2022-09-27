import request from '@/utils/request'

export function findRepairOrderPage(conditions) {
    return request({
        url: '/wlg-equipment/repairOrder/conditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findRepairOrderById(repairId) {
    return request({
        url: '/wlg-equipment/repairOrder/' + repairId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteRepairOrder(deleteForm) {
    return request({
        url: '/wlg-equipment/repairOrder/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/wlg-equipment/repairOrder',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/wlg-equipment/repairOrder/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}
