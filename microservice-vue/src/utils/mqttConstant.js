export default {
    MQTT_CONNECTION: {
        host: 'k8s.aacoptics.com',
        port: 30931,
        endpoint: '/mqtt',
        clean: true, // 保留会话
        username: "admin",
        password: "123456!a",
        connectTimeout: 5000, // 超时时间
        reconnectPeriod: 5000, // 重连时间间隔
    }
}

