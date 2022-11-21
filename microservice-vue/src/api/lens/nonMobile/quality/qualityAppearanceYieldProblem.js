import request from '@/utils/request'

export function findQualityAppearanceYieldProblemPage(conditions) {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualityAppearanceYieldProblem/list',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualityAppearanceYieldProblem/update',
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function handleDelete(deleteForm) {
    return request({
        url: `/non-mobile-quality-daily/nonMobileQualityAppearanceYieldProblem/delete/${deleteForm.id}`,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualityAppearanceYieldProblem/add',
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
        url: '/non-mobile-quality-daily/nonMobileQualityAppearanceYieldProblem/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function exportExcel() {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualityAppearanceYieldProblem/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob'
    })
}

export function listExportExcel(conditions) {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualityAppearanceYieldProblem/list/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
        data: conditions
    })
}
