# Touchpoint Protocol

`touchpoint-protocol` ，触点协议（TPP协议），一个Agent之间协作通信的协议，该协议通过AI模型驱动Agent之间协作，是智联网的协作通信标准。

## 概述
TPP协议基于状态 - 角色驱动模型实现动态工作流的调整，包括Action重新编排、Action修改、更换等。其通过前置Action触发调整工作流。具体接入方法如下：
- 在前置Action输出中添加状态码，目前支持的状态码：  
  OK(200),  
  NEED_REORDER_ACTION(300),  
  NEED_SWITCH_AI_MODEL(301),  
  NEED_SWITCH_TRANSPORT(302),
  NEED_SWITCH_ACTION(303),  
  NEED_CHECK_ACTION(401),  
  NEED_CHECK_ACTION_GRAPH(402),  
  NEED_CHECK_DATA(403)  
- 后置Action添加角色注解，处理前置Action重定向过来的数据。当前支持的角色有：`Coordinator` 和 `Supervisor`。`Coordinator`用于操作Action和工作流，`Supervisor`用于检查Data、Action和工作流。
- 后置Action实现角色接口，当前支持的接口有：  
`ActionChecker`：用于检查Action。  
`DataChecker`：用于检查Action输入。  
`TaskChecker`：用于检查任务和工作流。  
`ActionGraphOperator`：用于修改工作流。  
`ActionOperator`：用于修改Action。  
`DataOperator`：用于修改Action输入。

## Example
比如，产品团队Leader收到一个研发小组团建的消息，于是，告知项目经理，他的项目绕过该研发小组，先和其他团队对接。

实现 `Product Leader Action`，将 `NEED_REORDER_ACTION` 状态添加进方法输出：
```kotlin
@TouchPointAction( name = "productLeader", toActions = { "projectA[\"pm\"]" })
class ProductLeader : AgentActionListener<TeamMessage, TeamResponse> {
   
   override fun onReceive(message: TeamMessage, context: Context) : TeamResponse {
     TeamResponse teamResponse = new TeamResponse();
     if (message.getContent().contains("team-building")) {
       teamResponse.setState(new TouchPointState(
                 TaskState.NEED_REORDER_ACTION.getCode(),
                 "The R&D team is team-building, followed by coordination with other teams",
                 "pm",
                 "R&D");
     }
     
     return teamResponse;
   }
 
}
```

实现 `PM`，`PM`为协调者，实施绕过该研发小组，先和其他团队对接，即从Task中移除该研发小组：
```kotlin
@TouchPointAction( name = "pm")
@Coordinator(task = "projectA")
class PM : ActionGraphOperator<TeamResponse> {

    override fun run(teamResponse: TeamResponse, actionGraph: ActionGraph, context: TouchPointContext): ActionGraph {
      AgentActionMetaInfo actionMeta = context.getActionMeta(teamResponse.getState().getCtxName())
      if (actionGraph.getAdjList().containsKey(actionMeta)) {
        for (List<AgentActionMetaInfo> neighbors : adjList.values()) {
          neighbors.remove(actionMeta);
        }
        // 移除该节点
        adjList.remove(actionMeta);
      }

      return actionGraph
    }

}
```