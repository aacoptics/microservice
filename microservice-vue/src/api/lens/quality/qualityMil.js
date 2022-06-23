import request from '@/utils/request'

export function findQualityMilPage(conditions) {
    return request({
        url: '/quality-daily/qualityMil/listQualityMil',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listAllUser() {
    return request({
        url: '/quality-daily/qualityMil/users',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/quality-daily/qualityMil/update',
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function handleDelete(milType, id) {
    return request({
        url: `/quality-daily/qualityMil/delete/${milType}/${id}`,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/quality-daily/qualityMil/add',
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
        url: '/quality-daily/qualityMil/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function exportExcel() {
    return request({
        url: '/quality-daily/qualityMil/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob'
    })
}
