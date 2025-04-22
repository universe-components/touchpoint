# Lifecycle and Operator Model

## Lifecycle
The TPP protocol defines two types of lifecycles, which drive multiple Actions to collaborate in completing tasks:

- Initialization Lifecycle: Triggered automatically when the terminal integrating TPP starts, restarts, or meets specific conditions. This lifecycle is responsible for adding new Actions to the protocol, initializing them, and assigning tasks to them.
- Execution Lifecycle: This lifecycle consists of task planning → pre-processing of Actions → Action execution → post-processing of Actions → task completion. Developers can customize this lifecycle using the operator model.

## Operator Model
The TPP protocol's operator model defines different types of operations. Operators of different types control and coordinate multiple Agents across the two lifecycles (initialization and execution) to complete tasks.

The following operator types are currently built into TPP:

 - OrganizeAction: Used to reorganize multiple Actions to complete a task when the task environment changes. This operator type receives external environment variables and calls custom Actions based on those variables to reorganize multiple Actions.
 - ExceptionHandler: Used to handle exceptions thrown by an Action. This operator type receives the exception information and calls custom Actions to handle the exception accordingly.
 - SwitchTask: When task A needs to call a specific Action from task B to proceed, this operator type automatically adds the Action from task B into task A and invokes it.

Note: TPP allows developers to define custom operator types using SPI. For details, please refer to the [Operator Interface](../../../../blob/master/touchpoint-protocol/src/main/java/com/universe/touchpoint/layer/Operator.java).
