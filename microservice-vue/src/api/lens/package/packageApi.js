import request from '@/utils/request'

export function getShipmentInfos(conditions) {
    return request({
        url: '/package-server-xny/customerShipment/getShipmentInfos',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            page: conditions.current,
            size: conditions.size,
            customer: conditions.customer,
            orderNo: conditions.orderNo
        }
    })
}


export function uploadShipmentInfos(uploadForm) {
    return request({
        url: '/package-server-xny/customerShipment/uploadShipmentInfos',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: uploadForm
    })
}
