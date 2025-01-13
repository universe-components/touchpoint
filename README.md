# Touchpoint Protocol

`touchpoint-protocol` ，触点协议，是一个Agent之间通信的协议，每个Agent之间通过共享触点实现通信。

## 核心功能

- **动态连接Agent：** 动态连接多个Apk Agent，不同Agent可在一个进程，也可分属不同进程。

- **可热插拔Agent：** Agent网络中的任意一个Agent都可在线加入和退出，不影响整个Agent网络。

- **共享触点对象：** Agent之间共享触点对象，通过触点对象交换数据，无需关心交换形式是消息队列、RPC、还是REST等。

## 使用指南

### 导入依赖

在你的 `build.gradle` 文件中添加以下依赖：

```gradle
dependencies {
    implementation 'com.universe.touchpoint:touchpoint-lib:1.0.0'
}
```

### 配置Agent

#### Example 

以更新备忘录字体为例，其中，有两个Agent：Memo Agent和Font Agent。

#### Memo Agent

`MemoApplication` 继承 `AgentApplication`
```kotlin
class MemoApplication : AgentApplication()
```

配置Memo Agent Name
```xml
<meta-data
    android:name="com.universe.agent.name"
    android:value="memo_agent" />
```

#### Font Agent

`FontApplication` 继承 `AgentApplication`
```kotlin
class FontApplication : AgentApplication()
```

配置Font Agent Name
```xml
<meta-data
    android:name="com.universe.agent.name"
    android:value="font_agent" />
<meta-data
    android:name="com.universe.agent.iconName"
    android:value="字体" />
```

### 定义触点
```java
public class TextTouchPoint extends TouchPoint {

    private Float fontSize;
    private Boolean isBold = false;
    private Boolean isUnderlined = false;
    private String text;

    public TextTouchPoint() {
        super();
    }

    public Float getFontSize() {
        return fontSize;
    }

    public TextTouchPoint setFontSize(Float fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public Boolean getBold() {
        return isBold;
    }

    public TextTouchPoint setBold(Boolean bold) {
        this.isBold = bold;
        return this;
    }

    public Boolean getUnderlined() {
        return isUnderlined;
    }

    public TextTouchPoint setUnderlined(Boolean underlined) {
        this.isUnderlined = underlined;
        return this;
    }

    public String getText() {
        return text;
    }

    public TextTouchPoint setText(String text) {
        this.text = text;
        return this;
    }

}
```

### 触点注入

在 `Font Agent` 中注入触点：

```kotlin
import com.universe.touchpoint.touchpoints.TextTouchPoint

val textTouchPoint = TouchPointContextManager
    .generateTouchPoint<TextTouchPoint>(TextTouchPoint::class.java)
            
textTouchPoint
    .setFontSize(10.0)
    .finish()
```

### 接收触点
在 `Memo Agent` 中接收触点，方法如下：<br>
1. 继承 `TouchPointListener` 接口，并实现 `onReceive` 方法。<br>
2. 注解 `@TouchPointListener` 用于标识该类为触点监听器，`from` 属性用于指定该监听器接收的触点来自哪个Agent。

```kotlin
@com.universe.touchpoint.annotations.TouchPointListener(from = "memo")
class FontBroadcastListener : TouchPointListener<TextTouchPoint> {

    override fun onReceive(textTouchPoint: TextTouchPoint, context: Context) {
        val memoActivity = (context as MainApplication).currentActivity as MemoActivity
        val memoEditView = memoActivity.findViewById<EditText>(R.id.memo_text_view)

        // 只在字体大小发生变化时更新
        if (textTouchPoint.fontSize != null && textTouchPoint.fontSize != 1f) {
            memoEditView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textTouchPoint.fontSize)
        }
    }
        
}
```

说明：TextTouchPoint为用户自定义触点类，需继承TouchPoint类，该类可在不同应用间共享。

## RoadMap

1. **新增拉取触点**

2. **新增注解方式注入和使用**
