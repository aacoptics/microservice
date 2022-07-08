import request from '@/utils/request'


export function uploadExcel(param) {
    const formData = new FormData()
    formData.append('file', param.file)
    return request({
        url: '/wlg-report/productionSummary/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function downloadTemplate(params) {
    return request({
        url: '/wlg-report/productionSummary/downloadTemplate',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: params,
        responseType: 'blob',
    })
}

export function exportExcel(conditions) {
    return request({
        url: '/wlg-report/productionSummary/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
        data: conditions
    })
}

export function findProductionSummaryPage(conditions) {
    return request({
        url: '/wlg-report/productionSummary/conditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findProductionSummaryById(productionSummaryId) {
    return request({
        url: '/wlg-report/productionSummary/' + productionSummaryId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteProductionSummary(deleteForm) {
    return request({
        url: '/wlg-report/productionSummary/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/wlg-report/productionSummary',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/wlg-report/productionSummary/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}
