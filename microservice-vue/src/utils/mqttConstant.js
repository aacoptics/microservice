export default {
    MQTT_CONNECTION: {
        host: '10.69.76.40',
        port: 8083,
        endpoint: '/mqtt',
        clean: true, // 保留会话
        connectTimeout: 5000, // 超时时间
        reconnectPeriod: 5000, // 重连时间间隔
    }
}

