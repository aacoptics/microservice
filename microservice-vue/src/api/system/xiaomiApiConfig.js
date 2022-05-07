import request from '@/utils/request'


export function findXiaomiApiConfigPage(conditions) {
    return request({
        url: '/x5_api/xiaomiApiManagement/conditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findXiaomiApiConfigById(xiaomiApiConfigId) {
    return request({
        url: '/x5_api/xiaomiApiManagement/' + xiaomiApiConfigId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function findXiaomiApiConfigAll() {
    return request({
        url: '/x5_api/xiaomiApiManagement/all',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteXiaomiApiConfig(deleteForm) {
    return request({
        url: '/x5_api/xiaomiApiManagement/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/x5_api/xiaomiApiManagement',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/x5_api/xiaomiApiManagement/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}
