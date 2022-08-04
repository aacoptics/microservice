import request from '@/utils/request'


export function findProjectMapPage(conditions) {
    return request({
        url: '/wlg-report/projectMap/conditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findProjectMapById(projectMapId) {
    return request({
        url: '/wlg-report/projectMap/' + projectMapId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteProjectMap(deleteForm) {
    return request({
        url: '/wlg-report/projectMap/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/wlg-report/projectMap',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/wlg-report/projectMap/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}
