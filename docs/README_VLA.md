# Touchpoint Protocol

The Touchpoint Protocol (TPP) is a collaboration communication protocol between agents, driven by AI models to facilitate inter-agent collaboration. It serves as the collaboration communication standard for the Intelligent Network (Smart Internet).

## Example

### Item Classification

Step 1: Initiate task
```kotlin
data class Entry {

    @Task("item_classification_placement") // Specify the task
    val taskBuilder: TaskBuilder;

    fun classifiedPlacement(imageBytes: Array<Array<ByteArray>>) {
        val imageData = ImageData(imageBytes)
        taskBuilder.run("Please help me place these items you see into the fridge and the basket, respectively.", imageData)
    }

}
```

Step 2: Extend `ActionPredictor` to predict actions
```kotlin
@TouchPointAction(
    name = "predict robot actions", 
    desc = "Predict actions",
    toActions = {"item_classification_placement[\"action_executor\"]" })
@LangModel(name = Model.OPEN_VLA, apiHost = "http://127.0.0.1:8000")
class RoobotActionPredictor : ActionPredictor {
}
```

Step 3: Execute action sequence
```kotlin
@TouchPointAction(
    name = "action_executor", 
    desc = "Execute action sequence",
    toActions = {"item_classification_placement[]"})
class RobotActionExecutor : AgentActionExecutor<OpenVLA.ActionResponse, TouchPoint> {

    override fun run(actionSequence: OpenVLA.ActionResponse, context: Context): TouchPoint {
        // Execute the action sequence
        ......
        ......
        
        return new TouchPoint();
    }

}
```
