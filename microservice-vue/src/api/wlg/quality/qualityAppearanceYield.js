import request from '@/utils/request'

export function listHeaders(conditions) {
    return request({
        url: '/quality-daily/qualityAppearanceYield/listHeaders',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listDetailHeaders(conditions) {
    return request({
        url: '/quality-daily/qualityAppearanceYield/listDetailHeaders',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listSummary(conditions) {
    return request({
        url: '/quality-daily/qualityAppearanceYield/listSummary',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listDetailSummary(conditions) {
    return request({
        url: '/quality-daily/qualityAppearanceYield/listDetailSummary',
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
        url: '/quality-daily/qualityAppearanceYield/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function exportExcel() {
    return request({
        url: '/quality-daily/qualityAppearanceYield/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob'
    })
}

export function listSummaryExportExcel(conditions) {
    return request({
        url: '/quality-daily/qualityAppearanceYield/listSummary/exportExcel',
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
        url: '/quality-daily/qualityAppearanceYield/listLineChat',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listLine() {
    return request({
        url: '/quality-daily/qualityAppearanceYield/listLine',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}
