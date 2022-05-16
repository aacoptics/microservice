import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './assets/css/icon.css'
import './styles/common.css'
import './assets/css/font.css'
import 'element-plus/dist/index.css'
import 'font-awesome/css/font-awesome.min.css'
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import moment from 'moment'


const app = createApp(App)

app.config.globalProperties.$moment = moment

app.use(ElementPlus, {locale: zhCn})
    .use(store)
    .use(router)
    .mount('#app')
