import request from '@/utils/request'

export function findTaskInfoPage(conditions) {
    return request({
        url: '/notification-server/notification/conditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function deleteTask(deleteForm) {
    return request({
        url: '/notification-server/notification/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/notification-server/notification',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/notification-server/notification/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function startTask(Form) {
    return request({
        url: '/notification-server/notification/startNotificationJob/' + Form.id,
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function stopTask(Form) {
    return request({
        url: '/notification-server/notification/stopNotificationJob/' + Form.id,
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function getGroupInfo() {
    return request({
        url: '/notification-server/notification/listGroup',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function triggerNotificationJob(triggerJobForm) {
    return request({
        url: '/notification-server/notification/triggerNotificationJob',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: triggerJobForm
    })
}

export function queryMessageHistory(conditions) {
    return request({
        url: '/notification-server/notification/queryMessageHistory',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function deleteMessage(deleteForm) {
    return request({
        url: '/notification-server/notification/deleteMessage/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        },
        data: deleteForm
    })
}
