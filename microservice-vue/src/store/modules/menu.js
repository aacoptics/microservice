import {getMenuInfo, getMenuItems} from '@/utils/auth'

const state = {
    menuInfo: getMenuInfo(),
    menuItems: getMenuItems()
}

const mutations = {}


const actions = {}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}
