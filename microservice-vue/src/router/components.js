const Home = () => import('@/components/layout/Home');
const NotFound = () => import('@/views/error/NotFound')
const NoAuth = () => import('@/views/error/NoAuth')
const MainPage = () => import('@/views/home/MainPage')

export default {
    Home,
    MainPage,
    NotFound,
    NoAuth
};
