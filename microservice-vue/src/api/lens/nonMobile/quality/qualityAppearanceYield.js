import request from '@/utils/request'

export function listHeaders(conditions) {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualityAppearanceYield/listHeaders',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listDetailHeaders(conditions) {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualityAppearanceYield/listDetailHeaders',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listSummary(conditions) {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualityAppearanceYield/listSummary',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listDetailSummary(conditions) {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualityAppearanceYield/listDetailSummary',
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
        url: '/non-mobile-quality-daily/nonMobileQualityAppearanceYield/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function exportExcel() {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualityAppearanceYield/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob'
    })
}

export function listSummaryExportExcel(conditions) {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualityAppearanceYield/listSummary/exportExcel',
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
        url: '/non-mobile-quality-daily/nonMobileQualityAppearanceYield/listLineChat',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listLine() {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualityAppearanceYield/listLine',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}
