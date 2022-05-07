import request from '@/utils/request'

export function findDictTypePage(conditions) {
    return request({
        url: '/organization/dictType/list',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findDictTypeById(dictTypeById) {
    return request({
        url: '/organization/dictType/' + dictTypeById,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteDictType(deleteForm) {
    return request({
        url: '/organization/dictType/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/organization/dictType',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/organization/dictType/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

