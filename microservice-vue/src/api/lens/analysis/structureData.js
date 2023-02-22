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

// 获取类别
export function getCategory() {
    return request({
        url: '/moldflow-data-analysis/structureData/getCategory',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

// 获取项目
export function getProject() {
    return request({
        url: '/moldflow-data-analysis/structureData/getProject',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

// 获取零件名称
export function getPartName() {
    return request({
        url: '/moldflow-data-analysis/structureData/getPartName',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

// 获取材料
export function getMaterial() {
    return request({
        url: '/moldflow-data-analysis/structureData/getMaterial',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

// 获取事业部
export function getDepartment() {
    return request({
        url: '/moldflow-data-analysis/structureData/getDepartment',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

// 获取镜片数
export function getLensNumber() {
    return request({
        url: '/moldflow-data-analysis/structureData/getLensNumber',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

// 清空数据
export function deleteData() {
    return request({
        url: `/moldflow-data-analysis/structureData/deleteData`,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}



