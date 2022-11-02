import request from '@/utils/request'


export function findProductLineBudgetPage(conditions) {
    return request({
        url: '/budget-report/productLineBudget/conditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findProductLineBudgetById(productLineBudgetId) {
    return request({
        url: '/budget-report/productLineBudget/' + productLineBudgetId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteProductLineBudget(deleteForm) {
    return request({
        url: '/budget-report/productLineBudget/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/budget-report/productLineBudget',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/budget-report/productLineBudget/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function uploadExcel(param) {
    const formData = new FormData()
    formData.append('file', param.file)
    return request({
        url: '/budget-report/productLineBudget/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function downloadTemplate() {
    return request({
        url: '/budget-report/productLineBudget/downloadTemplate',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
    })
}

export function exportProductLineBudgetExcel(conditions) {
    return request({
        url: '/budget-report/productLineBudget/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
        data: conditions
    })
}