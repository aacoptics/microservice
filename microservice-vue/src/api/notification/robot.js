import request from '@/utils/request'
import * as qs from "qs";

export function findRobotInfoPage(conditions) {
    return request({
        url: '/notification-server/robot/conditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function deleteRobot(deleteForm) {
    return request({
        url: '/notification-server/robot/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/notification-server/robot',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/notification-server/robot/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function getAllRobotInfo() {
    return request({
        url: '/notification-server/robot/listAll',
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function findByNames(robotNames) {
    return request({
        url: '/notification-server/robot/findByNames',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: robotNames
    })
}
