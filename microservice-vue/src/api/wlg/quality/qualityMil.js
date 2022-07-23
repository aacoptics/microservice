import request from '@/utils/request'

export function findQualityMilPage(conditions) {
    return request({
        url: '/wlg-quality-daily/wlgQualityMil/listQualityMil',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listAllUser() {
    return request({
        url: '/wlg-quality-daily/wlgQualityMil/users',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/wlg-quality-daily/wlgQualityMil/update',
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function handleDelete(milType, id) {
    return request({
        url: `/wlg-quality-daily/wlgQualityMil/delete/${milType}/${id}`,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/wlg-quality-daily/wlgQualityMil/add',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function uploadExcel(param) {
    const formData = new FormData()
    formData.append('file', param.file)
    return request({
        url: '/wlg-quality-daily/wlgQualityMil/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function exportExcel() {
    return request({
        url: '/wlg-quality-daily/wlgQualityMil/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob'
    })
}

export function listQualityMilExportExcel(conditions) {
    return request({
        url: '/wlg-quality-daily/wlgQualityMil/listQualityMil/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
        data: conditions
    })
}
