import request from '@/utils/request'

export function findMenuTree() {
    return request({
        url: '/organization/menu/all',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function findIoTDashManagementMenuTree() {
    return request({
        url: '/organization/menu/byName',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            name: 'IoTDashboard'
        }
    })
}

export function findRoleMenus(roleId) {
    return request({
        url: '/organization/menu/byRole',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {roleId: roleId}
    })
}

export function deleteMenu(deleteForm) {
    return request({
        url: '/organization/menu/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/organization/menu',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/organization/menu/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function logMenuAccess(menuForm) {
    return request({
        url: '/organization/menu/logMenuAccess',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: menuForm
    })
}

export function getAccessLogByTime(conditions) {
    return request({
        url: '/organization/menu/getAccessLogByTime',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            page: conditions.current,
            size: conditions.size,
            startTime: conditions.startTime,
            endTime: conditions.endTime
        }
    })
}

export function getLastWeekMenuCount() {
    return request({
        url: '/organization/menu/getLastWeekMenuCount',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function getLastMouthTotalCount() {
    return request({
        url: '/organization/menu/getLastMouthTotalCount',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}
