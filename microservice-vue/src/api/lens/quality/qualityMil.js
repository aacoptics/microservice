import request from '@/utils/request'

export function findQualityMilPage(conditions) {
    return request({
        url: '/quality-daily/qualityMil/listQualityMil',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function listAllUser() {
    return request({
        url: '/quality-daily/qualityMil/users',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function findEnvironmentDataById(personPlacementDayId) {
    return request({
        url: '/xiaomi/environment/environmentData/' + personPlacementDayId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteEnvironmentData(factoryCode, deleteForm) {
    return request({
        url: '/xiaomi/environment/environmentData/' + deleteForm.id,
        method: 'delete',
        params: {
            'factoryCode': factoryCode
        },
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(factoryCode, addForm) {
    return request({
        url: '/xiaomi/environment/environmentData',
        method: 'post',
        params: {
            'factoryCode': factoryCode
        },
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(factoryCode, updateForm) {
    return request({
        url: '/xiaomi/environment/environmentData/' + updateForm.id,
        method: 'put',
        params: {
            'factoryCode': factoryCode
        },
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
        url: '/quality-daily/qualityMil/uploadExcel',
        method: 'post',
        data: formData
    })
}

export function exportExcel() {
    return request({
        url: '/xiaomi/environment/environmentData/exportExcel',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        responseType: 'blob'
    })
}

export const validateNumber = (rule, value, callback) => {
    let numberReg = /^\d+$|^\d+[.]?\d+$/;
    if (value !== '' && value !== null) {
        if (!numberReg.test(value)) {
            callback(new Error('请输入数字'));
        } else {
            callback();
        }
    } else {
        callback();
    }
}
