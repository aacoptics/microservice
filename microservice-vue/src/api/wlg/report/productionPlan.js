import request from '@/utils/request'


export function uploadExcel(param) {
    const formData = new FormData()
    formData.append('file', param.file)
    return request({
        url: '/wlg-report/productionPlan/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function exportExcel(conditions) {
    return request({
        url: '/wlg-report/productionPlan/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
        data: conditions
    })
}


export function downloadTemplate() {
    return request({
        url: '/wlg-report/productionPlan/downloadTemplate',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
    })
}

export function queryProductionPlanTitleByMonth(conditions) {
    return request({
        url: '/wlg-report/productionPlan/queryProductionPlanTitleByMonth',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findProductionPlanPage(conditions) {
    return request({
        url: '/wlg-report/productionPlan/queryProductionPlanByMonth',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findProductionPlanById(productionPlanId) {
    return request({
        url: '/wlg-report/productionPlan/' + productionPlanId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteProductionPlan(deleteForm) {
    return request({
        url: '/wlg-report/productionPlan/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/wlg-report/productionPlan',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/wlg-report/productionPlan/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}
