import request from '@/utils/request'


export function getAllMachineName() {
    return request({
        url: '/lens-fanuc-sz/fanucCheckData/getAllMachineName',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}


export function getAllMoldFileName() {
    return request({
        url: '/lens-fanuc-sz/fanucCheckData/getAllMoldFileName',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function getFanucCheckDataEveryDay(queryForm) {
    return request({
        url: '/lens-fanuc-sz/fanucCheckData/getFanucCheckDataEveryDay',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: queryForm
    })
}
