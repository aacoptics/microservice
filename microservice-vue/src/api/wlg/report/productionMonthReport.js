import request from '@/utils/request'


export function exportProductionMonthExcel(conditions) {
    return request({
        url: '/wlg-report/productionReport/exportProductionMonthExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
        data: conditions
    })
}

export function queryProductionReportTitleByMonth(conditions) {
    return request({
        url: '/wlg-report/productionReport/queryProductionMonthReportTitleByCondition',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findProductionReportPage(conditions) {
    return request({
        url: '/wlg-report/productionReport/queryProductionMonthReportByCondition',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}
