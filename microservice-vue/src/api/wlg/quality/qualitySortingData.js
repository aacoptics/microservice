import request from '@/utils/request'

export function listHeaders(conditions) {
    return request({
        url: '/wlg-quality-daily/wlgQualitySortingData/listHeaders',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listSummary(conditions) {
    return request({
        url: '/wlg-quality-daily/wlgQualitySortingData/listSummary',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function uploadExcel(param) {
    const formData = new FormData()
    formData.append('file', param.file)
    return request({
        url: '/wlg-quality-daily/wlgQualitySortingData/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function exportExcel() {
    return request({
        url: '/wlg-quality-daily/wlgQualitySortingData/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob'
    })
}

export function listSummaryExportExcel(conditions) {
    return request({
        url: '/wlg-quality-daily/wlgQualitySortingData/listSummary/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
        data: conditions
    })
}

export function listLineChat(conditions) {
    return request({
        url: '/wlg-quality-daily/wlgQualitySortingData/listLineChat',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listProject() {
    return request({
        url: '/wlg-quality-daily/wlgQualitySortingData/listProject',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}
