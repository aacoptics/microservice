import request from '@/utils/request'

// 导出excel模板
export function exportExcelTemplate() {
    return request({
        url: '/moldflow-data-analysis/processConditionData/exportTemplate',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob'
    })
}

// 上传excel数据
export function uploadExcel(param) {
    const formData = new FormData()
    formData.append('file', param.file)
    return request({
        url: '/moldflow-data-analysis/processConditionData/uploadExcel',
        method: 'post',
        data: formData
    })
}

// 条件查询
export function getDataByConditions(conditions) {
    return request({
        url: '/moldflow-data-analysis/processConditionData/getDataByConditions',
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
        url: '/moldflow-data-analysis/processConditionData/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
        data: conditions
    })
}

// 单条删除数据
export function handleDelete(updateForm) {
    return request({
        url: `/moldflow-data-analysis/processConditionData/delete/${updateForm.id}`,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

// 修改数据
export function handleUpdate(updateForm) {
    return request({
        url: '/moldflow-data-analysis/processConditionData/update',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
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

// 获取模具序号
export function getMoldNo() {
    return request({
        url: '/moldflow-data-analysis/processConditionData/getMoldNo',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}
