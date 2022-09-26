import request from '@/utils/request'


export function findMaintenanceOrderPage(conditions) {
    return request({
        url: '/wlg-equipment/maintenanceOrder/conditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findMaintenanceOrderById(maintenanceId) {
    return request({
        url: '/wlg-equipment/maintenanceOrder/' + maintenanceId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteMaintenanceOrder(deleteForm) {
    return request({
        url: '/wlg-equipment/maintenanceOrder/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/wlg-equipment/maintenanceOrder',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/wlg-equipment/maintenanceOrder/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}


export function addMaintenanceOrderItem(addForm) {
    return request({
        url: '/wlg-equipment/maintenanceOrder/addMaintenanceOrderItem',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function updateMaintenanceOrderItem(updateForm) {
    return request({
        url: '/wlg-equipment/maintenanceOrder/updateMaintenanceOrderItem/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function deleteMaintenanceOrderItem(deleteForm) {
    return request({
        url: '/wlg-equipment/maintenanceOrder/deleteMaintenanceOrderItem/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}


export function addMaintenanceOrderShift(addForm) {
    return request({
        url: '/wlg-equipment/maintenanceOrder/addMaintenanceOrderShift',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function updateMaintenanceOrderShift(updateForm) {
    return request({
        url: '/wlg-equipment/maintenanceOrder/updateMaintenanceOrderShift/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function deleteMaintenanceOrderShift(deleteForm) {
    return request({
        url: '/wlg-equipment/maintenanceOrder/deleteMaintenanceOrderShift/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}