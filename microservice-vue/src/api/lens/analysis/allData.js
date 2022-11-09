import request from '@/utils/request'

// 条件查询
export function getDataByConditions(conditions) {
    return request({
        url: '/moldflow-data-analysis/allData/getDataByConditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}


// 导出条件查询后的excel数据
export function exportExcel(conditions) {
    return request({
        url: '/moldflow-data-analysis/allData/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
        data: conditions
    })
}

// 获取类别
export function getCategory() {
    return request({
        url: '/moldflow-data-analysis/processConditionData/getCategory',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

// 获取项目
export function getProject() {
    return request({
        url: '/moldflow-data-analysis/processConditionData/getProject',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

// 获取零件名称
export function getPartName() {
    return request({
        url: '/moldflow-data-analysis/processConditionData/getPartName',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

// 获取材料
export function getMaterial() {
    return request({
        url: '/moldflow-data-analysis/processConditionData/getMaterial',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

// 获取图片流
export function getStream(params) {
    return request({
        url: '/moldflow-data-analysis/allData/fileStream',
        method: 'get',
        params: params,
        responseType: "blob",
        headers: {
            'Cache-Control': 'no-cache'
        }
    })
}
