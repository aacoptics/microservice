const Home = () => import('@/components/layout/Home');
const NotFound = () => import('@/views/error/404')
const NoAuth = () => import('@/views/error/403')
const Dashboard = () => import('@/views/home/Dashboard')

export default {
    Home,
    Dashboard,
    NotFound,
    NoAuth
};
