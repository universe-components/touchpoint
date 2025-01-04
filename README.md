# Touchpoint-Lib

`touchpoint-lib` 是一个触点IoC框架，用于远程和本地触点注入和使用。

## 使用指南

### 导入依赖

在你的 `build.gradle` 文件中添加以下依赖：

```gradle
dependencies {
    implementation 'com.universe.touchpoint:touchpoint-lib:1.0.0'
}
```

### 触点注入
```kotlin
import com.universe.touchpoint.touchpoints.TextTouchPoint

val textTouchPoint = TouchPointContextManager
    .generateTouchPoint<TextTouchPoint>(TextTouchPoint::class.java, context)
            
textTouchPoint
    .setFontSize(10.0)
    .finish()
```

### 接收触点
```kotlin
class FontBroadcastReceiver : TouchPointReceiver<TextTouchPoint> {

    override fun onReceive(textTouchPoint: TextTouchPoint, context: Context) {
        val memoActivity = (context as MainApplication).currentActivity as MemoActivity
        val memoEditView = memoActivity.findViewById<EditText>(R.id.memo_text_view)

        // 只在字体大小发生变化时更新
        if (textTouchPoint.fontSize != null && textTouchPoint.fontSize != 1f) {
            memoEditView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textTouchPoint.fontSize)
        }
        
}
```

说明：TextTouchPoint为用户自定义触点类，需继承TouchPoint类，该类可在不同应用间共享。

## RoadMap

1. **新增拉取触点**

2. **新增注解方式注入和使用**