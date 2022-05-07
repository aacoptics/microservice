import request from '@/utils/request'

export function getDict(dictType) {
    return request({
        url: '/organization/dictData/type/' + dictType,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function findDictDataPage(conditions) {
    return request({
        url: '/organization/dictData/list',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function deleteDictData(deleteForm) {
    return request({
        url: '/organization/dictData/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/organization/dictData',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/organization/dictData/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function selectDictLabel(datum, value) {
    let actions = [];
    Object.keys(datum).some((key) => {
        if (datum[key].dictValue === ('' + value)) {
            actions.push(datum[key].dictLabel);
            return true;
        }
    })
    return actions.join('');
}

export function selectDictLabelById(datum, value) {
    let actions = [];
    Object.keys(datum).some((key) => {
        if (('' + datum[key].id) === ('' + value)) {
            actions.push(datum[key].dictLabel);
            return true;
        }
    })
    return actions.join('');
}



