# Touchpoint Protocol

The Touchpoint Protocol (TPP) is a collaboration communication protocol between agents, driven by AI models to facilitate inter-agent collaboration. It serves as the collaboration communication standard for the Intelligent Network (Smart Internet).

## Overview
The TPP protocol is based on a state-role-driven model, which achieves dynamic routing through the triggering of pre-action. The integration method is as follows:

- Add status codes to the output of the pre-action. The currently supported status codes are:  
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

  Developers can also define custom status codes.

- Add role annotations to the post-action to handle data redirected from the pre-action. Currently, there are four supported roles: `Proposer` 、 `Executor` 、`Coordinator` and `Supervisor`。    
  `Proposer`：Initiator, used to start a task.  
  `Executor`：Executor, used to operate on data and perform actions.  
  `Coordinator`：Coordinator, used to edit on actions and workflows.  
  `Supervisor`：Supervisor, used to check data, actions, and workflows.
- The post-action implements role interfaces. The currently supported interfaces and super classes are:     
`AgentActionExecutor`：execute actions.   
`ActionChecker`：check actions.  
`DataChecker`：check action inputs.  
`TaskChecker`：check tasks and workflows.  
`ActionGraphOperator`：modify workflows.  
`ActionOperator`：modify actions.  
`DataOperator`：modify action inputs.  
`ActionPredictor`：predict actions.

## Example
For example, the product team leader receives a message about a team-building event from a development team. As a result, the product leader informs the project manager to bypass that development team and first coordinate with other teams.

Implementing `Product Leader` action by adding the `NEED_REORDER_ACTION` status in the method output:
```kotlin
@TouchPointAction( 
  name = "productLeader", 
  desc = "hand off to pm",
  toActions = { "projectA[\"productManager\"]" })
class ProductLeader : AgentActionExecutor<TeamMessage, TeamResponse> {
   
   override fun run(message: TeamMessage, context: Context) : TeamResponse {
     TeamResponse teamResponse = new TeamResponse();
     if (message.getContent().contains("team-building")) {
       teamResponse.getContext().setAction("R&D");
       teamResponse.setState(new TouchPointState(
                 TaskState.NEED_REORDER_ACTION.getCode(), // Status code NEED_REORDER_ACTION, indicating the need to reorder actions
                 "The R&D team is team-building, followed by coordination with other teams", // Status description
                 "pm"); // the Action name corresponding to the NEED_REORDER_ACTION status code, which is the name of the post-action
     }
     
     return teamResponse;
   }
 
}
```

Implementing `PM` , where `PM` is the coordinator to bypass the development team and first coordinate with other teams by removing the `R&D` team from the task:
```kotlin
@TouchPointAction( 
  name = "pm"
  desc = "remove R&D team from task"
  toActions = { "projectA[]" })
@Coordinator(task = "projectA")
class PM : ActionGraphOperator<TeamResponse> {

    override fun run(teamResponse: TeamResponse, context: Context): ActionGraph {
        String taskName = teamResponse.getContext().getTask();
        ActionGraph actionGraph = TouchPointContextManager.getTouchPointContext(taskName).getActionGraph();
        AgentActionMetaInfo actionMeta = TouchPointContextManager.getTouchPointContext(taskName).getActionContext().getActionMetaInfo(teamResponse.getContext().getAction());
        adjList = actionGraph.getAdjList()
        List<AgentActionMetaInfo> successors = actionGraph.getAdjList().get(actionMeta);

        List<AgentActionMetaInfo> predecessors = new ArrayList<>();
        for (Map.Entry<AgentActionMetaInfo, List<AgentActionMetaInfo>> entry : adjList.entrySet()) {
            AgentActionMetaInfo node = entry.getKey();
            List<AgentActionMetaInfo> neighbors = entry.getValue();
    
            if (neighbors.contains(actionMeta)) {
              predecessors.add(node);
            }
        }

        // Connect all predecessor nodes with successor nodes
        for (AgentActionMetaInfo predecessor : predecessors) {
            for (AgentActionMetaInfo successor : successors) {
                adjList.get(predecessor).add(successor);
            }
        }
        // Remove the R&D team node
        adjList.remove(actionMeta);
        for (List<AgentActionMetaInfo> neighbors : adjList.values()) {
            neighbors.remove(actionMeta);
        }
        return actionGraph
    }

}
```
