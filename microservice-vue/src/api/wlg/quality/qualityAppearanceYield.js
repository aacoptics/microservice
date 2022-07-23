import request from '@/utils/request'

export function listHeaders(conditions) {
    return request({
        url: '/wlg-quality-daily/wlgQualityAppearanceYield/listHeaders',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listDetailHeaders(conditions) {
    return request({
        url: '/wlg-quality-daily/wlgQualityAppearanceYield/listDetailHeaders',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listSummary(conditions) {
    return request({
        url: '/wlg-quality-daily/wlgQualityAppearanceYield/listSummary',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listDetailSummary(conditions) {
    return request({
        url: '/wlg-quality-daily/wlgQualityAppearanceYield/listDetailSummary',
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
        url: '/wlg-quality-daily/wlgQualityAppearanceYield/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function exportExcel() {
    return request({
        url: '/wlg-quality-daily/wlgQualityAppearanceYield/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob'
    })
}

export function listSummaryExportExcel(conditions) {
    return request({
        url: '/wlg-quality-daily/wlgQualityAppearanceYield/listSummary/exportExcel',
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
        url: '/wlg-quality-daily/wlgQualityAppearanceYield/listLineChat',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listLine() {
    return request({
        url: '/wlg-quality-daily/wlgQualityAppearanceYield/listLine',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}
