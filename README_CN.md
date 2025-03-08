# Touchpoint Protocol

## 概述

`touchpoint-protocol` ，触点协议（TPP协议），一个Agent之间协作通信的协议，该协议通过AI模型驱动Agent之间协作，是智联网的协作通信标准。

### 为什么使用TPP协议？

TPP协议秉承 “ 一切皆行为 ” 的理念，重新定义了智能体协作，实现了真正意义上的自适应多智能体协作模型。

- **上下文 - 角色驱动模型：** 通过上下文 - 角色驱动模型，多智能体之间完全可以根据不同场景、环境自主调整优化它们之间的协作关系，无需人工干预。

- **兼容互联网时代主流技术栈：** TPP协议主要用Java实现，完全兼容互联网时期的主流编程语言，及操作系统。基于注解，开发者几乎不用修改App，将App转变为智能体。

- **统一的协作对接方式：** TPP协议颠覆了传统的API对接方式，对接双方只需约定触点结构，无需指定Rest或RPC等通信协议，AI模型会自动选择合适的协议实现双方通信。

- **颠覆传统功能扩展方式：** TPP协议颠覆性地提出了协作者范式，通过该范式，开发者可以无侵入、平滑、无损地扩展智能体功能。

- **VLA模型和智能体协作超融合：** TPP协议彻底融合了VLA和智能体协作，使得其可以部署在任意终端，包括但不限于服务器、手机、眼镜、汽车、机器人、无人机等，真正实现All in One。

## 主要功能

- **支持多模态：** 支持处理文本、图像、音频、视频、传感器数据等，实现了VLA和智能体的融合。

- **支持多种智能体形态：** Agent可以是软件，比如：App、TCP，也可以是硬件，比如：机器人、汽车、无人机、云端等。

- **支持多种Agent架构：** 不同Agent可在一个进程，也可分属不同进程，及不同Agent可部署在不同的终端上。

- **支持多种Action执行模式：** 不同Action可以并行，也可以串行执行。

- **支持多种Agent文件格式：** 支持apk、jar(Spring)、exe、ipa、py等文件格式。

- **可热插拔Agent：** Agent网络中的任意一个Agent都可在线加入和退出，不影响整个Agent网络。

- **支持动态工作流：** 可在任务运行时，动态调整Action和工作流。

- **支持多种调用方式：** Action之间可以通过IPC、Broadcast、MQTT、RPC及REST等方式通信。

- **多级模型驱动：** AI模型可以作用在Agent，也可以作用在Task及Action。

- **多级调用策略：** 调用策略可以作用在Agent， 也可以作用在Action上。

- **支持多种AI模型：** 支持多种模型，如GPT-3、GPT-3.5、GPT-4、Claude-3、Claude-3.5、DINOv2、SigLIP等。

## 架构

<div align="center">
  <img src="architecture_cn.jpg" alt="Architecture" />
</div>

## Quick start

### 导入依赖

在你的 `build.gradle` 文件中添加以下依赖：

```gradle
dependencies {
    implementation 'com.universe.touchpoint:touchpoint-protocol:1.0.0'
}
```

### Example

以获取上海天气为例，其中，有一个入口Entry Agent和一个Weather Agent。

### 绑定Agent
如果 `Entry Agent` 和 `Weather Agent` 在同一个节点部署，需要在 `Entry Agent`添加如下代码完成绑定：
```kotlin
// 以两个Agent都是apk为例
AgentSocket.bind(./"weather_agent.apk", BinderType.ANDROID_BINDER);
```
备注：
（1）仅同一节点且多个Agent分属不同apk，部署需要显式绑定，如果多个Agent在一个apk内，无需显式绑定。
（2）其他情况都无需显式绑定。

### 实现Agent

#### Entry Agent

`EntryApplication` 继承 `AgentApplication`
```kotlin
/**
 * 其中，bindProtocol指定协作关系建立流程使用的协议，其作用域为所在Agent上的所有task，默认为MQTT5
 */
@TouchPointAgent(name = "entry_agent")
@AgentSocket(bindProtocol = SocketProtocol.MQTT5, brokerUri = "tcp://127.0.0.1:1883")
class EntryApplication : AgentApplication()
```
说明：由于TPP协议包含两个工作流：协作建立流和执行流。其中，协作建立流完成协作关系建立，执行流完成Action的执行。因此，`AgentSocket`为协作建立流相关配置。

在` Entry Agent` 中添加代码如下：
```kotlin
@Task("query_weather") // Specify the task
@Dubbo(applicationName = "entry_agent", registryAddress = "127.0.0.1:2181") // Optional global configuration to specify the Dubbo app name and registry address
@AIModel(name = Model.GPT_4, temperature = 0.0f, apiKey = "My API Key") // Specify the model, default is o1
@AgentSocket(bindProtocol = SocketProtocol.MQTT5, brokerUri = "tcp://127.0.0.1:1883")
public class QueryWeatherTask : TaskSocket() {
}
```

```kotlin
data class Entry {
    
    @Autowired
    val taskSocket: QueryWeatherTask;
    
    fun queryWeather() {
        taskSocket.send("我想查询上海天气")
    }

}
```
备注：以上配置作用域为Task。其中，`Dubbo` 指定协作执行流的通信协议，默认为MQTT5。

#### Weather Agent

`WeatherApplication` 继承 `AgentApplication`
```kotlin
@TouchPointAgent(name = "weather_agent", desc = "查询城市的天气信息")
@Dubbo(applicationName = "weather_agent", registryAddress = "127.0.0.1:2181") // 可选，指定dubbo应用名和注册中心地址
@AgentSocket(bindProtocol = SocketProtocol.MQTT5, brokerUri = "tcp://127.0.0.1:1883")
class WeatherApplication : AgentApplication()
```
如果希望 `Weather Agent` 使用指定LLM，可以配置如下：
```kotlin
/**
 * model默认使用o1
 */
@TouchPointAgent(name = "weather_agent", desc = "查询城市的天气信息")
@AIModel(name = Model.GPT_4, temperature = 0.0f) // 指定模型, 默认使用o1
class WeatherApplication : AgentApplication()
```
备注：以上配置作用域为Agent。Agent上所有未配置Action都使用以上配置。

定义获取天气的请求和响应类
```kotlin
data class WeatherRequest(val city: String) : TouchPoint()
```
```kotlin
data class WeatherResponse(val weather: String, val temperature: String) : TouchPoint()
```

监听来自 `Entry Agent` 的Action，并返回天气信息。
```kotlin
@TouchPointAction(
    name = "weather_action",
    desc = "查询城市的天气信息",
    toActions = {
        "entry_agent[\"next_action\"]", 
        "task2[next_action, next_action1, next_action2"]
    } //格式为：task_name[action_name1, action_name2, ...]
) 
@AIModel(name = Model.GPT_4, temperature = 0.0f) // 指定模型, 默认使用o1
class WeatherService : AgentActionExecutor<WeatherRequest, WeatherResponse> {

    override fun run(cityRequest: WeatherRequest, context: Context) : WeatherResponse {
        val client = OkHttpClient()

        // 设置请求的 URL 和参数
        val url = "$BASE_URL?q=$city&appid=$WEATHER_API_KEY&units=metric&lang=zh_cn"
        
        // 创建请求对象
        val request = Request.Builder()
            .url(url)
            .build()
    
        // 发送请求并获取响应
        val response: Response = client.newCall(request).execute()
    
        // 解析 JSON 响应
        if (response.isSuccessful) {
            val jsonResponse = response.body?.string()
    
            // 使用 Moshi 来解析 JSON
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val jsonAdapter = moshi.adapter(WeatherResponse::class.java)
    
            val weatherResponse = jsonAdapter.fromJson(jsonResponse)
    
            return if (weatherResponse != null) {
                val weatherDescription = weatherResponse.weather[0].description
                val temperature = weatherResponse.main.temp
                WeatherResponse(weatherDescription, temperature.toString())
            } else {
                throw RunTimeException("无法解析天气信息。")
            }
        } else {
            throw RunTimeException("无法获取天气信息，请检查城市名称是否正确。")
        }
    }

}
```
备注：以上代码中的`onReceive`方法输入和输出必须继承 `TouchPoint`。

如果希望 `weather_action` 使用Dubbo协议，可以配置如下：
```kotlin
@TouchPointAction(
    name = "weather_action"
    desc = "查询城市的天气信息",
) 
@AIModel(name = Model.GPT_4, temperature = 0.0f) // 指定模型, 默认使用o1
@DubboService(interfaceClass = IWeatherService::class) //必须指定接口，该注解为Dubbo自带注解
class WeatherService {

    override fun query(city: String) : WeatherResponse {
        val client = OkHttpClient()

        // 设置请求的 URL 和参数
        val url = "$BASE_URL?q=$city&appid=$WEATHER_API_KEY&units=metric&lang=zh_cn"
        
        // 创建请求对象
        val request = Request.Builder()
            .url(url)
            .build()
    
        // 发送请求并获取响应
        val response: Response = client.newCall(request).execute()
    
        // 解析 JSON 响应
        if (response.isSuccessful) {
            val jsonResponse = response.body?.string()
    
            // 使用 Moshi 来解析 JSON
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val jsonAdapter = moshi.adapter(WeatherResponse::class.java)
    
            val weatherResponse = jsonAdapter.fromJson(jsonResponse)
    
            return if (weatherResponse != null) {
                val weatherDescription = weatherResponse.weather[0].description
                val temperature = weatherResponse.main.temp
                WeatherResponse(weatherDescription, temperature.toString())
            } else {
                throw RunTimeException("无法解析天气信息。")
            }
        } else {
            throw RunTimeException("无法获取天气信息，请检查城市名称是否正确。")
        }
    }

}
```
备注：以上配置作用域为Action。配置优先级：Action > Task > Agent。

## 高级用法
- [动态路由](./docs/README_ROLE.md)
- [自定义上下文](./docs/README_CONTEXT.md)
- [VLA模型](./docs/README_VLA.md)
- [扩展功能新范式](./docs/README_EXTENSION.md)

## Design Notes
- [实体关系模型](./docs/design/README_ENTITY_CN.md)
- [协商握手](./docs/design/README_HANDSHAKE_CN)
- [状态同步](./docs/design/README_STATE_SYNC.md)

## RoadMap

1. **支持DeepSeek、Claude等模型**
2. **支持jar、exe、ipa、py等Agent文件类型**
3. **支持多Action并行**
4. **传输层Action化**
