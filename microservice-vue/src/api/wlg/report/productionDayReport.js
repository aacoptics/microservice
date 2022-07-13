import request from '@/utils/request'


export function findProductionDayReportPage(conditions) {
    return request({
        url: '/wlg-report/productionReport/queryProductionDayReportByCondition',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function exportProductionDayExcel(conditions) {
    return request({
        url: '/wlg-report/productionReport/exportProductionDayExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
        data: conditions
    })
}
