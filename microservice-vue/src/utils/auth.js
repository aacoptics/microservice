import Cookies from 'js-cookie'

export function getUsername() {
    return Cookies.get('username')
}

export function setUsername(username) {
    return Cookies.set('username', username, {expires: 99999})
}

export function removeUsername() {
    return Cookies.remove('username')
}

export function getUserDetail() {
    return JSON.parse(window.localStorage.getItem('user_detail'))
}

export function setUserDetail(userDetail) {
    if (userDetail.password) {
        delete userDetail.password
    }
    return window.localStorage.setItem('user_detail', JSON.stringify(userDetail))
}

export function removeUserDetail() {
    return window.localStorage.removeItem('user_detail')
}


export function getAccessToken() {
    return Cookies.get('access_token')
}

export function setAccessToken(token) {
    return Cookies.set('access_token', token, {expires: 99999})
}

export function removeAccessToken() {
    return Cookies.remove('access_token')
}

export function getRefreshToken() {
    return Cookies.get('refresh_token')
}

export function setRefreshToken(token) {
    return Cookies.set('refresh_token', token, {expires: 99999})
}

export function removeRefreshToken() {
    return Cookies.remove('refresh_token')
}

export function getMenuInfo() {
    return JSON.parse(window.localStorage.getItem('menu_info'))
}

export function setMenuInfo(menuInfo) {
    return window.localStorage.setItem('menu_info', JSON.stringify(menuInfo))
}

export function removeMenuInfo() {
    return window.localStorage.removeItem('menu_info')
}

export function getMenuItems() {
    return JSON.parse(window.localStorage.getItem('menu_items'))
}

export function setMenuItems(menuItems) {
    return window.localStorage.setItem('menu_items', JSON.stringify(menuItems))
}

export function removeMenuItems() {
    return window.localStorage.removeItem('menu_items')
}

export function getExpireTime() {
    return window.localStorage.getItem('token_expire')
}

export function setExpireTime(expireTime) {
    return window.localStorage.setItem('token_expire', expireTime)
}

export function removeExpireTime() {
    return window.localStorage.removeItem('token_expire')
}

export function getRefreshTime() {
    return window.localStorage.getItem('refresh_time')
}

export function setRefreshTime(refreshTime) {
    return window.localStorage.setItem('refresh_time', refreshTime)
}

export function removeRefreshTime() {
    return window.localStorage.removeItem('refresh_time')
}

export function removeLoginInfo() {
    removeMenuInfo()
    removeAccessToken()
    removeMenuItems()
    removeRefreshToken()
    removeUserDetail()
    removeUsername()
    removeExpireTime()
    removeRefreshTime()
}
