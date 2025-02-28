# Touchpoint Protocol

`touchpoint-protocol` ，触点协议（TPP协议），一个Agent之间协作通信的协议，该协议通过AI模型驱动Agent之间协作，是智联网的协作通信标准。

## 概述
TPP协议允许开发者添加自定义的数据到上下文，在Action之间传递。

## Example
你可以通过以下方式添加自定义数据到上下文：

```kotlin
@TouchPointAction(name = "robot_leader", desc = "机器人领队", toActions = {"housework["robotA"]"})
class RobotLeader : AgentActionExecutor<ActionSequence, TouchPoint> {

    override fun run(actionSequence: ActionSequence, context: Context): TouchPoint {
        actionSequence.getContext().addExtContext("flag", "flagA");
        return new TouchPoint();
    }

}
```

通过以下方式从上下文读取自定义数据：
```kotlin
@TouchPointAction(name = "robotA", desc = "机器人A", toActions = {"housework[]"})
class RobotA : AgentActionExecutor<ActionSequence, TouchPoint> {

    override fun run(actionSequence: ActionSequence, context: Context): TouchPoint {
        actionSequence.getContext().getExtContext("flag");
        return new TouchPoint();
    }

}
```