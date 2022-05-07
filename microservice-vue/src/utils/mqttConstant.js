export default {
    MQTT_CONNECTION: {
        host: 'iot.nnmz.aacoptics.com',
        port: 8083,
        endpoint: '/mqtt',
        clean: true, // 保留会话
        connectTimeout: 5000, // 超时时间
        reconnectPeriod: 5000, // 重连时间间隔
    },
    MQTT_SECS_TOPIC: [
        {
            topic: 'SecsGem/Status/+',
            qos: 0
        },
        {
            topic: 'SecsGem/Alarm/+',
            qos: 0
        }
    ],
    MQTT_SECS_HEART_BEAT_TOPIC: [
        {
            topic: 'SecsGem/HeartBeat/+',
            qos: 0
        }
    ],
    MQTT_Company_HEART_BEAT_TOPIC: [
        {
            topic: '+/HeartBeat',
            qos: 0
        }
    ],
    PROGRESS_COLORS: [
        {color: '#f56c6c', percentage: 20},
        {color: '#e6a23c', percentage: 40},
        {color: '#1989fa', percentage: 60},
        {color: '#6f7ad3', percentage: 80},
        {color: '#5cb87a', percentage: 100}
    ]
}

