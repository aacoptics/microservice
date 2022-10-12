import request from '@/utils/request'


export function findInspectionPage(conditions) {
    return request({
        url: '/wlg-equipment/inspectionManagement/conditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findInspectionById(inspectionId) {
    return request({
        url: '/wlg-equipment/inspectionManagement/' + inspectionId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteInspection(deleteForm) {
    return request({
        url: '/wlg-equipment/inspectionManagement/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/wlg-equipment/inspectionManagement',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/wlg-equipment/inspectionManagement/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}


export function addInspectionItem(addForm) {
    return request({
        url: '/wlg-equipment/inspectionManagement/addInspectionItem',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function updateInspectionItem(updateForm) {
    return request({
        url: '/wlg-equipment/inspectionManagement/updateInspectionItem/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function deleteInspectionItem(deleteForm) {
    return request({
        url: '/wlg-equipment/inspectionManagement/deleteInspectionItem/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}


export function addInspectionShift(addForm) {
    return request({
        url: '/wlg-equipment/inspectionManagement/addInspectionShift',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function updateInspectionShift(updateForm) {
    return request({
        url: '/wlg-equipment/inspectionManagement/updateInspectionShift/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function deleteInspectionShift(deleteForm) {
    return request({
        url: '/wlg-equipment/inspectionManagement/deleteInspectionShift/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function exportInspectionExcel(conditions) {
    return request({
        url: '/wlg-equipment/inspectionManagement/exportInspectionExcel',
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
        url: '/wlg-equipment/inspectionManagement/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function downloadTemplate() {
    return request({
        url: '/wlg-equipment/inspectionManagement/downloadTemplate',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob',
    })
}