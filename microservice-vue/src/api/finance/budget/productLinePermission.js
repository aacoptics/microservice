import request from '@/utils/request'


export function findProductLinePermissionPage(conditions) {
    return request({
        url: '/budget-report/productLinePermission/conditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findProductLinePermissionById(productLinePermissionId) {
    return request({
        url: '/budget-report/productLinePermission/' + productLinePermissionId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteProductLinePermission(deleteForm) {
    return request({
        url: '/budget-report/productLinePermission/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/budget-report/productLinePermission',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/budget-report/productLinePermission/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}
