const Home = () => import('@/components/layout/Home');
const NotFound = () => import('@/views/error/NotFound')
const NoAuth = () => import('@/views/error/NoAuth')
const Dashboard = () => import('@/views/home/Dashboard')

export default {
    Home,
    Dashboard,
    NotFound,
    NoAuth
};
