import request from '@/utils/request'


export function findImageById(imageId) {
    return request({
        url: '/wlg-equipment/image/' + imageId,
        method: 'get',
        headers: {
            responseType: 'blob'
        }
    })
}

export function deleteImage(deleteForm) {
    return request({
        url: '/wlg-equipment/image/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function addImage(addForm) {
    const formData = new FormData()
    formData.append('file', addForm.file)
    return request({
        url: '/wlg-equipment/image',
        method: 'post',
        headers: {
            'Content-Type': 'multipart/form-data'
        },
        data: formData
    })
}

export function updateImage(updateForm) {
    return request({
        url: '/wlg-equipment/image/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'multipart/form-data'
        },
        data: updateForm
    })
}