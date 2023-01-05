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

export function getTemphumidityInfoDisplay(buildingNo, floorNo, startDate, endDate) {
    return request({
        url: '/temphumidity-iot/temphumidity/getTemphumidityInfoDisplay',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            buildingNo: buildingNo,
            floorNo: floorNo,
            startDate: startDate,
            endDate: endDate
        }
    })
}

export function getElectricMeterInfo(param) {
    return request({
        url: '/temphumidity-iot/electricMeter/getElectricMeterInfo',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: param
    })
}

export function getElectricMeterQueryDataList(type) {
    return request({
        url: '/temphumidity-iot/electricMeter/getElectricMeterQueryDataList',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            type: type
        }
    })
}

export function getElectricMeterPowerQty(startDate, endDate) {
    return request({
        url: '/temphumidity-iot/electricMeter/getPowerQty',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        params: {
            startDate: startDate,
            endDate: endDate
        }
    })
}