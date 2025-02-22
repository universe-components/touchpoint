# Touchpoint Protocol

`touchpoint-protocol` ，触点协议（TPP协议），一个Agent之间协作通信的协议，该协议通过AI模型驱动Agent之间协作，是智联网的协作通信标准。

## 概述
TPP协议允许开发者添加自定义的数据到上下文，在Action之间传递。

## Example
比如，原来的机器人leader不稳定，我们可以给机器人添加一个 `leader = "robotA"` ，设置一个机器人集群由 `robotA`这个机器人指挥，方便集群中的其他机器人接收 `robotA`的动态指令。

实现 `Product Leader Action`，将 `NEED_REORDER_ACTION` 状态添加进方法输出：
```kotlin
@TouchPointAction(name = "robot_coordinator")
@Supervisor("fighting")
class RobotCoordinator : DefaultActionChecker<RobotInfo> {
   
   override fun run(robotInfo: RobotInfo, context: Context) : Boolean {
       val leader = robotInfo.getContext().getAction()
       val leaderMetric = robotInfo.getContext().getActionContext().getActionMetric(leader);
       if (leaderMetric.getPredictionCount() >= MAX_PREDICTIONS) {
           robotInfo.getContext().addExtContext("leader", "robotA");
       }
       return true
   }
 
}
```