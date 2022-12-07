import request from '@/utils/request'


export function exportSectionOrderCountExcel(conditions) {
    return request({
        url: '/wlg-equipment/report/exportSectionOrderCountExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
        data: conditions
    })
}

export function findSectionOrderCount(conditions) {
    return request({
        url: '/wlg-equipment/report/findSectionOrderCount',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}
