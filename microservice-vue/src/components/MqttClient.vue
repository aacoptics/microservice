<script>
import mqtt from "mqtt"
import Constant from "../utils/mqttConstant";

export default {
  name: "MqttClient",
  props: {
    value: {
      type: Array,
      default: () => []
    },
  },
  data() {
    return {
      client: {
        connected: false,
      }
    };
  },
  methods: {
    createMqttConnection() {
      const {host, port, endpoint, ...options} = Constant.MQTT_CONNECTION
      const connectUrl = `ws://${host}:${port}${endpoint}`
      try {
        this.client = mqtt.connect(connectUrl, options)
      } catch (error) {
        console.log('mqtt.connect error', error)
      }
      // 订阅消息
      this.client.on("connect", () => {
        this.value.forEach(item => {
          this.doSubscribe(item)
        })
      });
      //  接收消息
      this.client.on("message", (topic, message) => {
        this.$emit("messageArrived", JSON.parse(message))
        this.$emit("topicMessageArrived", topic, JSON.parse(message))
      })
      // 断开发起重连（非必须）
      this.client.on("reconnect", (error) => {
        console.log("正在重连:", error)
      })
      // 链接异常处理（非必须）
      this.client.on("error", (error) => {
        console.log("连接失败:", error)
      })
    },
    destroyMqttConnection() {
      if (this.client.connected) {
        try {
          this.client.end()
          this.client = {
            connected: false
          }
          console.log('断开连接成功')
        } catch (error) {
          console.log('断开连接失败！', error.toString())
        }
      }
    },
    doSubscribe(subscription) {
      const {topic, qos} = subscription
      this.client.subscribe(topic, qos, (error, res) => {
        if (error) {
          console.log('消息订阅失败！', error)
          return
        }
        console.log('消息订阅成功！', res)
      })
    }
  },
};
</script>
