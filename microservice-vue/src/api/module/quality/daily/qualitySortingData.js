import request from '@/utils/request'

export function listHeaders(conditions) {
    return request({
        url: '/module-quality-daily-api/moduleQualitySortingData/listHeaders',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listSummary(conditions) {
    return request({
        url: '/module-quality-daily-api/moduleQualitySortingData/listSummary',
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
        url: '/module-quality-daily-api/moduleQualitySortingData/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function exportExcel() {
    return request({
        url: '/module-quality-daily-api/moduleQualitySortingData/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob'
    })
}

export function listSummaryExportExcel(conditions) {
    return request({
        url: '/module-quality-daily-api/moduleQualitySortingData/listSummary/exportExcel',
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
        url: '/module-quality-daily-api/moduleQualitySortingData/listLineChat',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listProject() {
    return request({
        url: '/module-quality-daily-api/moduleQualitySortingData/listProject',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}
