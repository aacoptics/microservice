import request from '@/utils/request'


export function findBusinessDivisionProductLinePage(conditions) {
    return request({
        url: '/budget-report/businessDivisionProductLine/conditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findBusinessDivisionProductLineById(businessDivisionProductLineId) {
    return request({
        url: '/budget-report/businessDivisionProductLine/' + businessDivisionProductLineId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteBusinessDivisionProductLine(deleteForm) {
    return request({
        url: '/budget-report/businessDivisionProductLine/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/budget-report/businessDivisionProductLine',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/budget-report/businessDivisionProductLine/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function findAllBusinessDivision() {
    return request({
        url: '/budget-report/businessDivisionProductLine/getAllBusinessDivision',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}
export function findProductLineByBusinessDivision(data) {
    return request({
        url: '/budget-report/businessDivisionProductLine/getProductLineByBusinessDivision',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: data
    })
}
