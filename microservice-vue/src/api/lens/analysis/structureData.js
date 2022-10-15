import request from '@/utils/request'

// 导出excel模板
export function exportExcelTemplate() {
    return request({
        url: '/moldflow-data-analysis/structureData/exportTemplate',
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
        url: '/moldflow-data-analysis/structureData/uploadExcel',
        method: 'post',
        data: formData
    })
}

// 条件查询
export function getDataByConditions(conditions) {
    return request({
        url: '/moldflow-data-analysis/structureData/getDataByConditions',
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
        url: '/moldflow-data-analysis/structureData/exportExcel',
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
        url: `/moldflow-data-analysis/structureData/delete/${updateForm.id}`,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

// 修改数据
export function handleUpdate(updateForm) {
    return request({
        url: '/moldflow-data-analysis/structureData/update',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}


