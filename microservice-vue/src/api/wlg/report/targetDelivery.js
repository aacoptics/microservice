import request from '@/utils/request'


export function uploadExcel(param) {
    const formData = new FormData()
    formData.append('file', param.file)
    return request({
        url: '/wlg-report/targetDelivery/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function exportExcel(conditions) {
    return request({
        url: '/wlg-report/targetDelivery/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
        data: conditions
    })
}

export function downloadTemplate() {
    return request({
        url: '/wlg-report/targetDelivery/downloadTemplate',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
    })
}

export function queryTargetDeliveryTitleByMonth(conditions) {
    return request({
        url: '/wlg-report/targetDelivery/queryTargetDeliveryTitleByMonth',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findTargetDeliveryPage(conditions) {
    return request({
        url: '/wlg-report/targetDelivery/queryTargetDeliveryByMonth',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findTargetDeliveryById(targetDeliveryId) {
    return request({
        url: '/wlg-report/targetDelivery/' + targetDeliveryId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteTargetDelivery(deleteForm) {
    return request({
        url: '/wlg-report/targetDelivery/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/wlg-report/targetDelivery',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/wlg-report/targetDelivery/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}
