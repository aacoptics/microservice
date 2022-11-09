import request from '@/utils/request'

export function listHeaders(conditions) {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualityCustomerGrade/listHeaders',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listSummary(conditions) {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualityCustomerGrade/listSummary',
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
        url: '/non-mobile-quality-daily/nonMobileQualityCustomerGrade/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function exportExcel() {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualityCustomerGrade/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob'
    })
}

export function listSummaryExportExcel(conditions) {
    return request({
        url: '/non-mobile-quality-daily/nonMobileQualityCustomerGrade/listSummary/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
        data: conditions
    })
}
