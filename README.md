# Touchpoint Protocol

`touchpoint-protocol` ，触点协议（TPP协议），是一个Agent之间通信的协议，每个Agent之间通过共享触点实现通信，是Agent网络的基础设施。

## 核心功能

- **支持多种Agent架构：** 不同Agent可在一个进程，也可分属不同进程，及不同Agent可部署在不同的设备上。

- **多种Agent文件格式：** 支持apk、jar、exe、ipa、py等文件格式。

- **可热插拔Agent：** Agent网络中的任意一个Agent都可在线加入和退出，不影响整个Agent网络。

- **共享触点对象：** Agent之间共享触点对象，通过触点对象交换数据，无需关心交换形式是消息队列、RPC、还是REST等。

- **支持多种LLM：** 支持多种LLM，如GPT-3、GPT-4、GPT-3.5、GPT-4.0、Claude-3、Claude-3.5等。

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

### 配置Agent

#### Entry Agent

`EntryApplication` 继承 `AgentApplication`
```kotlin
@TouchPointAgent(name = "entry_agent")
class EntryApplication : AgentApplication()
```

#### Weather Agent

`WeatherApplication` 继承 `AgentApplication`
```kotlin
@TouchPointAgent(name = "weather_agent", desc = "查询上海天气")
class WeatherApplication : AgentApplication()
```

#### 执行
```kotlin
AgentBuilder builder = AgentBuilder
    .createConfig(AgentConfig.Model.GPT_4) // 选择模型
    .setModelApiKey("My API Key") // 设置模型API Key
    .build();

builder.run("我想查询上海天气");
```

## 高级用法

（[如何自定义Agent编排？](./README_BASIC.md)）

## RoadMap

1. **新增注解方式注入和使用**
2. **实现跨端Agent连接**