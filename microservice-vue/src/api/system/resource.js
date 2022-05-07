import request from '@/utils/request'

export function findResourceTree() {
    return request({
        url: '/organization/resource/allAuthentication',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function findRoleResource(roleId) {
    return request({
        url: '/organization/resource/role/' + roleId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}


export function findResourcePage(conditions) {
    return request({
        url: '/organization/resource/conditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findResourceById(resourceId) {
    return request({
        url: '/organization/resource/' + resourceId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteResource(deleteForm) {
    return request({
        url: '/organization/resource/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/organization/resource',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/organization/resource/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}
