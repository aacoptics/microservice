import request from '@/utils/request'

export function listHeaders(conditions) {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualitySortingData/listHeaders',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listSummary(conditions) {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualitySortingData/listSummary',
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
        url: '/non-mobile-quality-daily/nonMobileQualitySortingData/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function exportExcel() {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualitySortingData/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob'
    })
}

export function listSummaryExportExcel(conditions) {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualitySortingData/listSummary/exportExcel',
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
        url: '/non-mobile-quality-daily/nonMobileQualitySortingData/listLineChat',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listProject() {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualitySortingData/listProject',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}
