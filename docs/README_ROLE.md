# Touchpoint Protocol

The Touchpoint Protocol (TPP) is a collaboration communication protocol between agents, driven by AI models to facilitate inter-agent collaboration. It serves as the collaboration communication standard for the Intelligent Network (Smart Internet).

## Overview
The TPP protocol is based on a context-role driven model, which achieves dynamic routing through the triggering of pre-action. The integration method is as follows:

- Add status codes to the output of the pre-action. The currently provided status codes are:  
  `OK(200)`,  
  `NEED_REORDER_ACTION(300)`,  
  `NEED_SWITCH_LANG_MODEL(301)`,   
  `NEED_SWITCH_VISION_MODEL(302)`,  
  `NEED_SWITCH_VISION_LANG_MODEL(303)`,  
  `NEED_SWITCH_TRANSPORT(304)`,  
  `NEED_SWITCH_ACTION(305)`,  
  `NEED_CHECK_ACTION(401)`,  
  `NEED_CHECK_ACTION_GRAPH(402)`,  
  `NEED_CHECK_DATA(403)`  

  Note: Status codes are not mandatory. Developers can also define custom status codes.

- Add role annotations to the post-action to handle data redirected from the pre-action. Currently, there are four supported roles: `Proposer` 、 `Executor` 、`Coordinator` and `Supervisor`。    
  `Proposer`：Initiator, used to start a task.  
  `Executor`：Executor, used to operate on data and perform actions.  
  `Coordinator`：Coordinator, used to edit on actions and workflows.  
  `Supervisor`：Supervisor, used to check data, actions, and workflows.
- The post-action implements role interfaces. The currently supported interfaces and super classes are:     
`AgentActionExecutor`：execute actions.   
`DefaultDataChecker`：check action inputs.   
`ActionGraphOperator`：modify workflows.  
`ActionOperator`：modify actions.  
`ActionPredictor`：predict actions.

## Example
For example, a local fruit farmer plans to sell their fruit through an agent and has drafted an authorization letter. However, he is unsure if the letter contains any issues, so he consult a lawyer to check it. Once the letter is reviewed and approved, it is given to the agent.

Step 1: Implement `DraftAuthLetter` and add the `NEED_CHECK_DATA` status to the method output:
```kotlin
@TouchPointAction(
  name = "drafting a letter of authorization",
  desc = "drafting a letter of authorization to sell fruits",
  toActions = { "sales[\"Authorized Agent\"]" })
class DraftAuthLetter : AgentActionExecutor<FruitInfo, AuthLetter> {

  override fun run(message: FruitInfo, context: Context) : AuthLetter {
    AuthLetter letter = new AuthLetter();
    letter.setState(new TouchPointState(
            TaskState.NEED_CHECK_DATA.getCode(), // The status code NEED_CHECK_DATA indicates that the next step requires data verification
      "Please help check if there are any issues with the authorization letter?", // Status description
      "lawyer"); // The action name corresponding to the NEED_CHECK_DATA status code, which is the next action

    return letter;
  }

}
```

Step 2: Implement `Lawyer`, mark Lawyer as a `supervisor`, and check if there are any issues with the authorization letter. If it passes, send the letter to the agent: 
```kotlin
@TouchPointAction(
  name = "lawyer"
          desc = "check if there are any issues with the authorization letter?"
          toActions = { "sales[\"Authorized Agent\"]" })
@Supervisor(task = "sales")
class Lawyer : DefaultDataChecker<AuthLetter> {

  override fun run(letter: AuthLetter, context: Context): Boolean {
    // Check if there are any issues with the authorization letter
    if (letter.hasIssues()) {
      return false
    }
    return true
  }

}
```
