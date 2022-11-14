import request from '@/utils/request'

export function listHeaders(conditions) {
    return request({
        url: '/module-quality-daily-api/moduleQualityAppearanceYield/listHeaders',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listDetailHeaders(conditions) {
    return request({
        url: '/module-quality-daily-api/moduleQualityAppearanceYield/listDetailHeaders',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listSummary(conditions) {
    return request({
        url: '/module-quality-daily-api/moduleQualityAppearanceYield/listSummary',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listDetailSummary(conditions) {
    return request({
        url: '/module-quality-daily-api/moduleQualityAppearanceYield/listDetailSummary',
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
        url: '/module-quality-daily-api/moduleQualityAppearanceYield/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function exportExcel() {
    return request({
        url: '/module-quality-daily-api/moduleQualityAppearanceYield/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob'
    })
}

export function listSummaryExportExcel(conditions) {
    return request({
        url: '/module-quality-daily-api/moduleQualityAppearanceYield/listSummary/exportExcel',
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
        url: '/module-quality-daily-api/moduleQualityAppearanceYield/listLineChat',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listLine() {
    return request({
        url: '/module-quality-daily-api/moduleQualityAppearanceYield/listLine',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}
