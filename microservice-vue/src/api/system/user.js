import request from '@/utils/request'
import * as qs from "qs";

export function login(loginInfo) {
    return request({
        url: '/auth-server/oauth/token',
        method: 'post',
        auth: {
            username: 'iot_client',
            password: '123456!iot'
        },
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        data: qs.stringify(loginInfo),
        params: {
            grant_type: 'ldap'
        }
    })
}

export function logout() {
    return request({
        url: '/auth-server/oauth/logout',
        method: 'get'
    })
}

export function refreshToken(token) {
    return request({
        url: '/auth-server/oauth/token',
        method: 'post',
        auth: {
            username: 'iot_client',
            password: '123456!iot'
        },
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        params: {
            grant_type: 'refresh_token',
            refresh_token: token
        }
    })
}

export function getUserInfo(username) {
    return request({
        url: '/organization/user',
        method: 'get',
        params: {
            uniqueId: username
        }
    })
}

export function getMenuByUsername(username) {
    return request({
        url: '/organization/menu/byUsername',
        method: 'get',
        params: {
            username: username
        }
    })
}

export function findUserInfoPage(conditions) {
    return request({
        url: '/organization/user/conditions',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: conditions
    })
}

export function findUserRolesById(userId) {
    return request({
        url: '/organization/role/user/' + userId,
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function deleteUser(deleteForm) {
    return request({
        url: '/organization/user/' + deleteForm.id,
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export function handleAdd(addForm) {
    return request({
        url: '/organization/user',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: addForm
    })
}

export function handleUpdate(updateForm) {
    return request({
        url: '/organization/user/' + updateForm.id,
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        data: updateForm
    })
}

export function getAllUser() {
    return request({
        url: '/organization/user/queryAll',
        method: 'get'
    })
}
