import request from '@/utils/request'


export function uploadExcel(param) {
    const formData = new FormData()
    formData.append('file', param.file)
    return request({
        url: '/wlg-report/moldUse/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function exportExcel(conditions) {
    return request({
        url: '/wlg-report/moldUse/exportExcel',
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
        url: '/wlg-report/moldUse/downloadTemplate',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
    })
}

export function queryMoldUseTitleByMonth(conditions) {
    return request({
        url: '/wlg-report/moldUse/queryMoldUseTitleByMonth',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findMoldUsePage(conditions) {
    return request({
        url: '/wlg-report/moldUse/queryMoldUseByMonth',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findMoldUseById(moldUseId) {
    return request({
        url: '/wlg-report/moldUse/' + moldUseId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteMoldUse(deleteForm) {
    return request({
        url: '/wlg-report/moldUse/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/wlg-report/moldUse',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/wlg-report/moldUse/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}
