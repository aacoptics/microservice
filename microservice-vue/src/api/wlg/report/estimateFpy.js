import request from '@/utils/request'


export function uploadExcel(param) {
    const formData = new FormData()
    formData.append('file', param.file)
    return request({
        url: '/wlg-report/estimateFpy/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function downloadTemplate() {
    return request({
        url: '/wlg-report/estimateFpy/downloadTemplate',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
    })
}

export function exportExcel(conditions) {
    return request({
        url: '/wlg-report/estimateFpy/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
        data: conditions
    })
}

export function findEstimateFpyPage(conditions) {
    return request({
        url: '/wlg-report/estimateFpy/conditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findEstimateFpyById(estimateFpyId) {
    return request({
        url: '/wlg-report/estimateFpy/' + estimateFpyId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteEstimateFpy(deleteForm) {
    return request({
        url: '/wlg-report/estimateFpy/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/wlg-report/estimateFpy',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/wlg-report/estimateFpy/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}
