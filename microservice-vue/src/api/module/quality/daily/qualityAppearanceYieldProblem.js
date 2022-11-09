import request from '@/utils/request'

export function findQualityAppearanceYieldProblemPage(conditions) {
    return request({
        url: '/module-quality-daily-api/moduleQualityAppearanceYieldProblem/list',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/module-quality-daily-api/moduleQualityAppearanceYieldProblem/update',
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function handleDelete(deleteForm) {
    return request({
        url: `/module-quality-daily-api/moduleQualityAppearanceYieldProblem/delete/${deleteForm.id}`,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/module-quality-daily-api/moduleQualityAppearanceYieldProblem/add',
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
        url: '/module-quality-daily-api/moduleQualityAppearanceYieldProblem/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function exportExcel() {
    return request({
        url: '/module-quality-daily-api/moduleQualityAppearanceYieldProblem/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob'
    })
}

export function listExportExcel(conditions) {
    return request({
        url: '/module-quality-daily-api/moduleQualityAppearanceYieldProblem/list/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
        data: conditions
    })
}
