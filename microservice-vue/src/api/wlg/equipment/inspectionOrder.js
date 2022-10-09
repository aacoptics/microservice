import request from '@/utils/request'


export function findInspectionOrderPage(conditions) {
    return request({
        url: '/wlg-equipment/inspectionOrder/conditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findInspectionOrderById(inspectionId) {
    return request({
        url: '/wlg-equipment/inspectionOrder/' + inspectionId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteInspectionOrder(deleteForm) {
    return request({
        url: '/wlg-equipment/inspectionOrder/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/wlg-equipment/inspectionOrder',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/wlg-equipment/inspectionOrder/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}


export function addInspectionOrderItem(addForm) {
    return request({
        url: '/wlg-equipment/inspectionOrder/addInspectionOrderItem',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function updateInspectionOrderItem(updateForm) {
    return request({
        url: '/wlg-equipment/inspectionOrder/updateInspectionOrderItem/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function deleteInspectionOrderItem(deleteForm) {
    return request({
        url: '/wlg-equipment/inspectionOrder/deleteInspectionOrderItem/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}


export function addInspectionOrderShift(addForm) {
    return request({
        url: '/wlg-equipment/inspectionOrder/addInspectionOrderShift',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function updateInspectionOrderShift(updateForm) {
    return request({
        url: '/wlg-equipment/inspectionOrder/updateInspectionOrderShift/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function deleteInspectionOrderShift(deleteForm) {
    return request({
        url: '/wlg-equipment/inspectionOrder/deleteInspectionOrderShift/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}


export function handleBatchConfirm(params) {
    return request({
        url: '/wlg-equipment/inspectionOrder/batchConfirm',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: params
    })
}