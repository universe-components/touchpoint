# Touchpoint Protocol

The Touchpoint Protocol (TPP) is a collaboration communication protocol between agents, driven by AI models to facilitate inter-agent collaboration. It serves as the collaboration communication standard for the Intelligent Network (Smart Internet).

## Overview
The TPP protocol allows developers to add custom data to the context and pass it between Actions.

## Example
You can add custom data to the context as follows:

```kotlin
@TouchPointAction(name = "robot_leader", desc = "Robot Leader", toActions = {"housework["robotA"]"})
class RobotLeader : AgentActionExecutor<ActionSequence, TouchPoint> {

    override fun run(actionSequence: ActionSequence): TouchPoint {
        actionSequence.getContext().addExtContext("flag", "flagA");
        return new TouchPoint();
    }

}
```

To read custom data from the context:
```kotlin
@TouchPointAction(name = "robotA", desc = "Robot A", toActions = {"housework[]"})
class RobotA : AgentActionExecutor<ActionSequence, TouchPoint> {

    override fun run(actionSequence: ActionSequence): TouchPoint {
        actionSequence.getContext().getExtContext("flag");
        return new TouchPoint();
    }

}
```