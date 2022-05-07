import {createStore} from 'vuex'
import state from './state'
import getters from './getters'
import modules from './modules'
import actions from './actions'
import mutations from './mutations'
//
// const modulesFiles = require.context('./modules', true, /\.js$/)
//
// // you do not need `import app from './modules/app'`
// // it will auto require all vuex module from modules file
// const modules = modulesFiles.keys().reduce((modules, modulePath) => {
//     // set './app.js' => 'app'
//     const moduleName = modulePath.replace(/^\.\/(.*)\.\w+$/, '$1')
//     const value = modulesFiles(modulePath)
//     modules[moduleName] = value.default
//     return modules
// }, {})

export default createStore({
    state,
    getters,
    mutations,
    actions,
    modules
})
