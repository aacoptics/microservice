import request from '@/utils/request'


export function findXiaomiAuthPage(conditions) {
    return request({
        url: '/x5_api/xiaomiAuthManagement/conditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findXiaomiAuthById(xiaomiApiConfigId) {
    return request({
        url: '/x5_api/xiaomiAuthManagement/' + xiaomiApiConfigId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function findXiaomiAuthAll() {
    return request({
        url: '/x5_api/xiaomiAuthManagement/all',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteXiaomiAuth(deleteForm) {
    return request({
        url: '/x5_api/xiaomiAuthManagement/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/x5_api/xiaomiAuthManagement',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/x5_api/xiaomiAuthManagement/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}
