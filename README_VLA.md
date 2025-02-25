# Touchpoint Protocol

`touchpoint-protocol` ，触点协议（TPP协议），一个Agent之间协作通信的协议，该协议通过AI模型驱动Agent之间协作，是智联网的协作通信标准。

## Example

### 以物品分类为例

第一步：发起任务
```kotlin
data class Entry {

    @Task("item_classification_placement") // 指定task
    @LangModel(name = Model.GPT_4, temperature = 0.0f, apiKey = "My API Key") // 指定模型, 默认使用o1
    val taskBuilder: TaskBuilder;

    fun classifiedPlacement() {
        TaskBuilder.task("item_classification_placement").run("Please help me place these items you see into the fridge and the basket, respectively.", imageData)
    }

}
```

第二步：实时图像进行编码
```kotlin
@TouchPointAction(name = "robot_vision_encoder", 
  toActions = {"item_classification_placement[\"action_executor\"]" })
@VisionModel(name = Model.DINO_V2)
@VisionLangModel(name = Model.SIGLIP)
class RoobotVisionEncoder : ImageActionExecutor {
}
```

第三步：执行行为序列
```kotlin
@TouchPointAction(name = "action_executor", toActions = {"item_classification_placement[]"})
class RobotActionExecutor : AgentActionExecutor<ActionSequence, TouchPoint> {

    override fun run(actionSequence: ActionSequence, context: Context): ActionSequence {
        // 执行行为序列
        ......
        ......
        
        return new TouchPoint();
    }

}
```
