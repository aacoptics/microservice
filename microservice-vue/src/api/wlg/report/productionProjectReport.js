import request from '@/utils/request'


export function exportProductionProjectExcel(conditions) {
    return request({
        url: '/wlg-report/productionReport/exportProductionProjectExcel',
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
        url: '/wlg-report/productionReport/queryProductionProjectReportTitleByCondition',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findProductionReportPage(conditions) {
    return request({
        url: '/wlg-report/productionReport/queryProductionProjectReportByCondition',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}
