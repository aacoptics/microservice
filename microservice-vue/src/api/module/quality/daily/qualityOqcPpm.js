import request from '@/utils/request'

export function listHeaders(conditions) {
    return request({
        url: '/module-quality-daily-api/moduleQualityOqcPpm/listHeaders',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listSummary(conditions) {
    return request({
        url: '/module-quality-daily-api/moduleQualityOqcPpm/listSummary',
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
        url: '/module-quality-daily-api/moduleQualityOqcPpm/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function exportExcel() {
    return request({
        url: '/module-quality-daily-api/moduleQualityOqcPpm/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob'
    })
}

export function listSummaryExportExcel(conditions) {
    return request({
        url: '/module-quality-daily-api/moduleQualityOqcPpm/listSummary/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
        data: conditions
    })
}
