import request from '@/utils/request'


export function findResearchBudgetPage(conditions) {
    return request({
        url: '/budget-report/researchBudget/conditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findResearchBudgetById(researchBudgetId) {
    return request({
        url: '/budget-report/researchBudget/' + researchBudgetId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteResearchBudget(deleteForm) {
    return request({
        url: '/budget-report/researchBudget/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/budget-report/researchBudget',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/budget-report/researchBudget/' + updateForm.id,
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
        url: '/budget-report/researchBudget/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function downloadTemplate() {
    return request({
        url: '/budget-report/researchBudget/downloadTemplate',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
    })
}