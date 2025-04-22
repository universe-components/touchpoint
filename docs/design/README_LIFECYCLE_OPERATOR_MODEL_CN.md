# 生命周期和操作者模型

## 生命周期
TPP协议定义了两种生命周期，通过这两个生命周期驱动多个Action协作完成任务：

- 初始化生命周期：在集成TPP的终端启动、重启、条件触发时，该生命周期会自动将新的Action加入到协议中，并完成新Action的初始化，任务分配等。
- 执行生命周期：该生命周期包含任务规划 -> Action前置处理 -> Action执行 -> Action后置处理 -> 任务完成。通过操作者模型，开发者可以自定义该生命周期流程。

## 操作者模型
TPP协议的操作者模型定义了不同的操作类型，不同类型操作者在两个协议生命周期（初始化和执行）控制和组织多个Agent协作完成任务。

当前TPP内置的几个操作类型：

 - OrganizeAction：当任务环境发生变化时，用于重新组织多个Action来完成任务。该操作类型会接收外部环境变量，根据环境变量调用自定义Action重新组织多个Action。
 - ExceptionHandler：当某个Action抛出异常时，该操作类型会接收异常信息，并根据异常信息调用自定义Action来处理异常。
 - SwitchTask：当一个任务A需要调用另一个任务B中指定Action来完成时，那么，该操作类型会自动将任务B中的Action加入到任务A中，并调用任务B中的Action。

说明：TPP允许开发者自定义操作类型，具体请参考[Operator接口](../../../../touchpoint-protocol/src/java/com/universe/touchpoint/layer/Operator.java)。