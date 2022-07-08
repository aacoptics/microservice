import request from '@/utils/request'


export function uploadExcel(param) {
    const formData = new FormData()
    formData.append('file', param.file)
    return request({
        url: '/wlg-report/productionActual/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function exportExcel(conditions) {
    return request({
        url: '/wlg-report/productionActual/exportExcel',
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
        url: '/wlg-report/productionActual/downloadTemplate',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
    })
}

export function findProductionActualPage(conditions) {
    return request({
        url: '/wlg-report/productionActual/conditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findProductionActualById(productionActualId) {
    return request({
        url: '/wlg-report/productionActual/' + productionActualId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteProductionActual(deleteForm) {
    return request({
        url: '/wlg-report/productionActual/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/wlg-report/productionActual',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/wlg-report/productionActual/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}
