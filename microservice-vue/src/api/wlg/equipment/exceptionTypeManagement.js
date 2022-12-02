import request from '@/utils/request'


export function findExceptionTypePage(conditions) {
    return request({
        url: '/wlg-equipment/exceptionTypeManagement/conditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findExceptionTypeById(inspectionId) {
    return request({
        url: '/wlg-equipment/exceptionTypeManagement/' + inspectionId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteExceptionType(deleteForm) {
    return request({
        url: '/wlg-equipment/exceptionTypeManagement/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/wlg-equipment/exceptionTypeManagement',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/wlg-equipment/exceptionTypeManagement/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}


export function addExceptionSubClass(addForm) {
    return request({
        url: '/wlg-equipment/exceptionTypeManagement/addExceptionSubClass',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function updateExceptionSubClass(updateForm) {
    return request({
        url: '/wlg-equipment/exceptionTypeManagement/updateExceptionSubClass/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function deleteExceptionSubClass(deleteForm) {
    return request({
        url: '/wlg-equipment/exceptionTypeManagement/deleteExceptionSubClass/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}
