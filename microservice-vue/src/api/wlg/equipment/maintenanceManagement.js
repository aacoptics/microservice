import request from '@/utils/request'


export function findMaintenancePage(conditions) {
    return request({
        url: '/wlg-equipment/maintenanceManagement/conditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findMaintenanceById(maintenanceId) {
    return request({
        url: '/wlg-equipment/maintenanceManagement/' + maintenanceId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteMaintenance(deleteForm) {
    return request({
        url: '/wlg-equipment/maintenanceManagement/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/wlg-equipment/maintenanceManagement',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/wlg-equipment/maintenanceManagement/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}


export function addMaintenanceItem(addForm) {
    return request({
        url: '/wlg-equipment/maintenanceManagement/addMaintenanceItem',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function updateMaintenanceItem(updateForm) {
    return request({
        url: '/wlg-equipment/maintenanceManagement/updateMaintenanceItem/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function deleteMaintenanceItem(deleteForm) {
    return request({
        url: '/wlg-equipment/maintenanceManagement/deleteMaintenanceItem/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}


export function exportMaintenanceExcel(conditions) {
    return request({
        url: '/wlg-equipment/maintenanceManagement/exportMaintenanceExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
        data: conditions
    })
}

export function uploadExcel(param) {
    const formData = new FormData()
    formData.append('file', param.file)
    return request({
        url: '/wlg-equipment/maintenanceManagement/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function downloadTemplate() {
    return request({
        url: '/wlg-equipment/maintenanceManagement/downloadTemplate',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
    })
}