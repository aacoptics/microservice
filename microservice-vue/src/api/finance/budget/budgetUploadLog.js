import request from '@/utils/request'


export function findBudgetUploadLogPage(conditions) {
    return request({
        url: '/budget-report/budgetUploadLog/conditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findBudgetUploadLogById(budgetUploadLogId) {
    return request({
        url: '/budget-report/budgetUploadLog/' + budgetUploadLogId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteBudgetUploadLog(deleteForm) {
    return request({
        url: '/budget-report/budgetUploadLog/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/budget-report/budgetUploadLog',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/budget-report/budgetUploadLog/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}
