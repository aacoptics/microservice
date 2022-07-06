import request from '@/utils/request'

export function findQualityAppearanceYieldProblemPage(conditions) {
    return request({
        url: '/quality-daily/qualityAppearanceYieldProblem/list',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/quality-daily/qualityAppearanceYieldProblem/update',
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function handleDelete(id) {
    return request({
        url: `/quality-daily/qualityAppearanceYieldProblem/delete/${id}`,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/quality-daily/qualityAppearanceYieldProblem/add',
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
        url: '/quality-daily/qualityAppearanceYieldProblem/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function exportExcel() {
    return request({
        url: '/quality-daily/qualityAppearanceYieldProblem/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob'
    })
}

export function listExportExcel(conditions) {
    return request({
        url: '/quality-daily/qualityAppearanceYieldProblem/list/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
        data: conditions
    })
}
