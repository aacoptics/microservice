import {getUserInfo, login} from '@/api/system/user'
import {
    getAccessToken,
    getExpireTime,
    getRefreshToken,
    getUsername,
    setAccessToken,
    setExpireTime,
    setRefreshTime,
    setRefreshToken,
    setUserDetail,
    setUsername
} from '@/utils/auth'
import {ElMessage} from "element-plus";

const state = {
    refreshToken: getRefreshToken(),
    accessToken: getAccessToken(),
    username: getUsername(),
    expireTime: getExpireTime()
}

const mutations = {
    SET_ACCESS_TOKEN: (state, token) => {
        state.accessToken = token
    },

    SET_REFRESH_TOKEN: (state, token) => {
        state.refreshToken = token
    },

    SET_USER_NAME: (state, username) => {
        state.username = username
    }
}

const actions = {
    // user login
    login({commit}, userInfo) {
        const {username, password} = userInfo
        return new Promise((resolve, reject) => {
            login({username: username.trim(), password: password}).then(response => {
                if (response.status !== 200) {
                    reject()
                    if (response.status === 401) {
                        ElMessage.error("没有此用户，请联系IT开通！")
                    }
                    return
                }
                const {access_token, refresh_token, expires_in} = response.data
                setUsername(username.trim())
                setAccessToken(access_token)
                setRefreshToken(refresh_token)
                commit('SET_ACCESS_TOKEN', access_token)
                commit('SET_REFRESH_TOKEN', refresh_token)
                commit('SET_USER_NAME', username.trim())
                const curTime = new Date();
                const expireDate = new Date(curTime.setSeconds(curTime.getSeconds() + expires_in));
                setExpireTime(expireDate)
                setRefreshTime(expireDate)
                getUserInfo(username.trim()).then(response => {
                    const {data} = response.data
                    setUserDetail(data)
                    resolve()
                }).catch(error => {
                    reject(error)
                })
            }).catch(error => {
                reject(error)
            })
        })
    }
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}
