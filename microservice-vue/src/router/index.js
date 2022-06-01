import {createRouter, createWebHistory} from "vue-router";
import Login from "../views/login/Login.vue";
import routeMap from "./components"
import {getAccessToken, getMenuItems, getUsername, setMenuInfo, setMenuItems} from '@/utils/auth'
import {getMenuByUsername} from "@/api/system/user";
import {saveRefreshTime} from "@/api";
import {logMenuAccess} from "@/api/system/menu";

const routes = [
    {
        path: '/',
        redirect: '/mainPage'
    },
    {
        path: "/login",
        name: "Login",
        meta: {
            title: '登录'
        },
        component: Login
    }
]
const dynamicRouteDic = {}

let isFetchRemote = true;

const formatMenus = function (menuData, menuItems, levelInfo = '') {
    if (!menuItems) {
        menuItems = [];
    }
    menuData.length && menuData.forEach(menu => {
        const newParent = levelInfo ? levelInfo + '|' + menu.title : '' + menu.title;
        const menuItem = {
            visible: menu.visible,
            icon: menu.icon,
            index: menu.path ? menu.path.replace("/", "") : menu.name,
            title: menu.title,
            levelInfo: newParent
        }
        menuItems.push(menuItem)
        if (menu.children && menu.children.length) {
            menuItem.subs = [];
            formatMenus(menu.children, menuItem.subs, newParent);
        }
    });
    return menuItems;
}

const formatRoutes = function (routes, routeData) {
    if (!routeData) {
        routeData = {
            path: '/',
            name: 'home',
            component: routeMap['Home'],
            children: [
                {
                    path: "/mainPage",
                    name: "mainPage",
                    meta: {
                        title: '系统首页'
                    },
                    component: routeMap['MainPage']
                },
                {
                    path: '/:catchAll(.*)',
                    name: '404',
                    meta: {
                        title: '找不到页面'
                    },
                    component: routeMap['NotFound']
                },
                {
                    path: '/403',
                    name: '403',
                    meta: {
                        title: '没有权限'
                    },
                    component: routeMap['NoAuth']
                }
            ]
        };
    }
    routes.length && routes.forEach(route => {
        if (route.path) {
            if (route.path.split('/').length > 2) {
                let paths = route.path.split('/')
                if (dynamicRouteDic[`${paths[1]}_${paths.length}`] === undefined)
                    dynamicRouteDic[`${paths[1]}_${paths.length}`] = []
                dynamicRouteDic[`${paths[1]}_${paths.length}`].push(route)
            } else {
                routeData.children.push({
                    path: route.path.indexOf('?') > -1 ? route.path.split('?')[0] : route.path,
                    name: route.name,
                    component: () => import('../' + route.component),
                    meta: {
                        title: route.title,
                        webUrl: route.webUrl ? route.webUrl : null
                    },
                })
            }
        }
        if (route.children && route.children.length) {
            formatRoutes(route.children, routeData);
        }
    });
    return routeData;
};

const updateDocumentTitle = function (to) {
    if (to.meta['title'] !== undefined) {
        if (to.meta['title'] instanceof Array) {
            document.title = `${to.meta['title'].find(i => i.path === to.path).title} | 光学统一数据平台`;
        } else {
            document.title = `${to.meta['title']} | 光学统一数据平台`;
        }
    }
}

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
});

router.beforeEach((to, from, next) => {
    saveRefreshTime()
    updateDocumentTitle(to)

    if (to.meta.openView) {
        updateDocumentTitle(to)
        next();
        return
    }
    if (!getAccessToken() && to.path !== '/login') {
        next('/login');
    } else if ((isFetchRemote || !getMenuItems()) && to.path !== '/login') {
        getMenuByUsername(getUsername()).then((response) => {
            const responseData = response.data
            if (responseData.code === '000000') {
                isFetchRemote = false;
                const menuData = responseData.data;
                setMenuInfo(menuData);
                const routesData = formatRoutes(menuData);
                const menuItems = formatMenus(menuData);
                setMenuItems(menuItems);
                Object.keys(dynamicRouteDic).forEach(key => {
                    let routes = dynamicRouteDic[key];
                    let paths = routes[0].path.split('/');
                    let path = `${paths[0]}/${paths[1]}/${Array.from({length: paths.length - 2}, (_, i) => i + 1).map(i => `:id${i}`).join('/')}`
                    routesData.children.push({
                        path: path,
                        name: paths[1],
                        component: () => import('../' + routes[0].component),
                        meta: {
                            title: routes.map(r => ({title: r.title, path: r.path})),
                            webUrl: null
                        },
                    })
                });
                router.addRoute(routesData)
                router.push({
                    path: to.path,
                    query: to.query
                });
            } else {
                isFetchRemote = true;
            }
            next();
        })
            .catch(err => {
                console.log(err);
            });

    } else {
        if (getAccessToken() && to.path !== '/login')
            logMenuAccess({name: to.name});
        next();
    }
});

export default router;
