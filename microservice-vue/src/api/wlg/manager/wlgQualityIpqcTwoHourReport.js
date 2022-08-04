import request from '@/utils/request'


export function qualityIpqcTwoHourReport(conditions) {
    return request({
        url: '/wlg-quality-daily/wlgQualityIpqcTwoHourReport/qualityIpqcTwoHourReport',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function qualityIpqcTwoHourReportExportExcel(conditions) {
    return request({
        url: '/wlg-quality-daily/wlgQualityIpqcTwoHourReport/qualityIpqcTwoHourReport/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
        data: conditions
    })
}

