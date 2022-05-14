import request from '@/utils/request'

export function findRoleInfoPage(conditions) {
    return request({
        url: '/organization/role/conditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findIoTDashManagementRole() {
    return request({
        url: '/organization/role/code/IoTDashboard',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/organization/role/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function handleRoleData() {
    return request({
        url: '/organization/role/all',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/organization/role',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function deleteRole(deleteForm) {
    return request({
        url: '/organization/role/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

