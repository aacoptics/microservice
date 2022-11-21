import request from '@/utils/request'

export function findQualityMilPage(conditions) {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualityMil/listQualityMil',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listAllUser() {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualityMil/users',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualityMil/update',
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function handleDelete(milType, id) {
    return request({
        url: `/non-mobile-quality-daily/nonMobileQualityMil/delete/${milType}/${id}`,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualityMil/add',
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
        url: '/non-mobile-quality-daily/nonMobileQualityMil/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function exportExcel() {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualityMil/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob'
    })
}

export function listQualityMilExportExcel(conditions) {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualityMil/listQualityMil/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
        data: conditions
    })
}
