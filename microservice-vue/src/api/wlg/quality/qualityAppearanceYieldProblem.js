import request from '@/utils/request'

export function findQualityAppearanceYieldProblemPage(conditions) {
    return request({
        url: '/wlg-quality-daily/wlgQualityAppearanceYieldProblem/list',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/wlg-quality-daily/wlgQualityAppearanceYieldProblem/update',
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function handleDelete(deleteForm) {
    return request({
        url: `/wlg-quality-daily/wlgQualityAppearanceYieldProblem/delete/${deleteForm.id}`,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/wlg-quality-daily/wlgQualityAppearanceYieldProblem/add',
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
        url: '/wlg-quality-daily/wlgQualityAppearanceYieldProblem/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function exportExcel() {
    return request({
        url: '/wlg-quality-daily/wlgQualityAppearanceYieldProblem/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob'
    })
}

export function listExportExcel(conditions) {
    return request({
        url: '/wlg-quality-daily/wlgQualityAppearanceYieldProblem/list/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
        data: conditions
    })
}
