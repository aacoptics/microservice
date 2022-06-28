import request from '@/utils/request'

export function listHeaders(conditions) {
    return request({
        url: '/quality-daily/qualityOqcPpm/listHeaders',
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
        url: '/quality-daily/qualityOqcPpm/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function exportExcel() {
    return request({
        url: '/quality-daily/qualityOqcPpm/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob'
    })
}

export function listQualityOqcPpmExportExcel(conditions) {
    return request({
        url: '/quality-daily/qualityOqcPpm/listQualityOqcPpm/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
        data: conditions
    })
}
