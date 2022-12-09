import request from '@/utils/request'

export function getTemphumidityInfoByDate(date) {
    return request({
        url: '/temphumidity-iot/temphumidity/getTemphumidityInfoByDate',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            date: date
        }
    })
}

export function getSmartMeterInfoByDate(date) {
    return request({
        url: '/temphumidity-iot/temphumidity/getSmartMeterInfoByDate',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            date: date
        }
    })
}