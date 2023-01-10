import request from '@/utils/request'




export function getAnalysisData(queryForm) {
    return request({
        url: '/lens-fanuc-ne/fanucDashboard/getAnalysisData',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: queryForm
    })
}
