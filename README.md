# Touchpoint Protocol

`touchpoint-protocol` ，触点协议（TPP协议），一个Agent之间协作通信的协议，该协议通过AI模型驱动Agent之间协作，是智联网的协作通信标准。

## 核心功能

- **多种Agent架构：** 不同Agent可在一个进程，也可分属不同进程，及不同Agent可部署在不同的终端上。

- **多种Action执行模式：** 不同Action可以并行，也可以串行执行。

- **多种Agent文件格式：** 支持apk、jar、exe、ipa、py等文件格式。

- **可热插拔Agent：** Agent网络中的任意一个Agent都可在线加入和退出，不影响整个Agent网络。

- **运行时修改Action和工作流：** 可在任务运行时，动态调整Action和工作流。

- **多种调用方式：** Action之间可以通过IPC、Broadcast、MQTT、RPC及REST等方式通信。

- **多级模型驱动：** LLM驱动可以作用在Agent，也可以作用在Task及Action。

- **多级调用策略：** 调用策略可以作用在Agent， 也可以作用在Action上。

- **多种LLM：** 支持多种LLM，如GPT-3、GPT-3.5、GPT-4、Claude-3、Claude-3.5等。

## 架构

<div align="center">
  <img src="architecture.png" alt="Architecture" />
</div>

## 使用指南

### 导入依赖

在你的 `build.gradle` 文件中添加以下依赖：

```gradle
dependencies {
    implementation 'com.universe.touchpoint:touchpoint-protocol:1.0.0'
}
```

### Example

以获取上海天气为例，其中，有一个入口Entry Agent和一个Weather Agent。

### 实现Agent

#### Entry Agent

`EntryApplication` 继承 `AgentApplication`
```kotlin
/**
 * 其中，socketBindProtocol指定socket绑定流程使用的协议，其作用域为所在Agent上的所有task
 */
@TouchPointAgent(name = "entry_agent")
@AgentSocket(bindProtocol = SocketProtocol.MQTT5, brokerUri = "tcp://127.0.0.1:1883")
class EntryApplication : AgentApplication()
```

在` Entry Agent` 中执行
```kotlin
data class Entry {
    
    @Task("query_weather")
    @Dubbo(applicationName = "entry_agent") // 可选的全局配置，指定dubbo应用名和注册中心地址
    @AIModel(name = Model.GPT_4, temperature = 0.0f, apiKey = "My API Key") // 指定模型, 默认使用o1
    @AgentSocket(bindProtocol = SocketProtocol.MQTT5, brokerUri = "tcp://127.0.0.1:1883")
    val taskBuilder: TaskBuilder;
    
    fun queryWeather() {
        TaskBuilder.task("query_weather").run("我想查询上海天气")
    }

}
```

如果需要 `Entry Agent` 重新编排Actions，可以添加注解 `Coordinator`，并实现接口 `ActionCoordinator` 中方法 `run()`如下：
```kotlin
@TouchPointAction(
    name = "entry_action"
)
@Coordinator(task = "query_weather") // task指定是哪个任务的协调者
class Entry : ActionGraphOperator<PredecessorResponse> {

    override fun run(PredecessorResponse predecessorResponse, actionGraph: ActionGraph) : ActionGraph {
        // 重新编排 Actions
    }
    
}
其中，PredecessorResponse必须继承TouchPoint。
```

如果希望 `Entry Agent` 成为监督者，检查前置Action输出，可以配置如下：
```kotlin
@TouchPointAction(
    name = "entry_action", role = ActionRole.SUPERVISOR
)
@Supervisor(task = "query_weather") // task指定是哪个任务的监督者
class Entry : DataChecker<PredecessorResponse> {

    override fun run(PredecessorResponse input) : Boolean {
        // 检查input
    }
    
}
```

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

定义获取天气的响应类
```kotlin
data class WeatherResponse(val weather: String, val temperature: String) : TouchPoint()
```

监听来自 `Entry Agent` 的Action，并返回天气信息。
```kotlin
@TouchPointAction(
    name = "weather_action",
    tasks = {"entry_agent", "task"} // 可以指定多个参与的任务
    toActions = {
        "entry_agent[next_action"], 
        "task2[next_action, next_action1, next_action2"]
    } //格式为：task_name[action_name1, action_name2, ...]
) 
@AIModel(name = Model.GPT_4, temperature = 0.0f) // 指定模型, 默认使用o1
class WeathertListener : AgentActionListener<String, WeatherResponse> {

    override fun onReceive(city: String, context: Context) : WeatherResponse {
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

如果希望 `weather_action` 使用Dubbo协议，可以配置如下：
```kotlin
@TouchPointAction(
    name = "weather_action",
    tasks = {"entry_agent"} // 可以指定多个任务发起者
) 
@AIModel(name = Model.GPT_4, temperature = 0.0f) // 指定模型, 默认使用o1
@DubboService(interfaceClass = IWeatherService::class) //必须指定接口
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

## 高级用法
- [自定义角色行为](./README_ROLE.md)
- [降级为非模型驱动的Agent编排](./README_BASIC.md)

## RoadMap

1. **支持DeepSeek、Claude等模型**
2. **支持jar、exe、ipa、py等Agent文件类型**
3. **支持多Action并行**
