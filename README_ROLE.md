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
比如，HR主管给一个HR布置任务：检查员工考勤次数，次数少于阈值，交由办理离职的HR开除该员工：

实现 `HR Leader Action`，将 `NEED_CHECK_ACTION` 状态添加进方法输出：
```kotlin
@TouchPointAction( name = "hrLeader", toActions = { "daily_checking[\"hr\"]" })
class HRLeader : AgentActionListener<EmployeeRequest, EmployeeResponse> {
   
   override fun onReceive(employeeRequest: EmployeeRequest, context: Context) : EmployeeResponse {
     EmployeeResponse employee = new EmployeeResponse();
     employee.setEntity(employeeRequest.getEntity("James"));
     employee.setState(new TouchPointState(
             TaskState.NEED_CHECK_ACTION.getCode(),
             "Please check James' jobs",
             "hr");
     
     return employee;
   }
 
}
```

实现 `HR`，`HR`为监督者，检查HR主管转发给她的员工，并检查其考勤：
```kotlin
@TouchPointAction( name = "hr")
@Supervisor(task = "daily_checking")
class HR : DataChecker<EmployeeResponse, CheckResult> {

    override fun run(employee: EmployeeResponse): CheckResult {
      if (employee < 10) {
        checkResult = CheckResult();
        checkResult.setEmployee(employee.getEntity());
        checkResult.state = TouchPointState(
          TaskState.NEED_SWITCH_ACTION.code,
          "The employee[]'s attendance frequency is too low.",
          "check out work"
        )
        return checkResult
      }

      checkResult.setState(TouchPointState(TaskState.OK.getCode(), "success"));
      return true
    }

}
```

实现 `Check Out Work`，`Check Out Work`为协调者，将员工开除：
```kotlin
@TouchPointAction( name = "check out work")
@Coordinator(task = "daily_checking")
class HRCheckOutWork : ActionGraphOperator<CheckResult> {

    override fun run(employeeCheckResult: CheckResult, actionGraph: ActionGraph): Boolean {
      if (actionGraph.getAdjList().containsKey(employeeCheckResult.getEmployee()) {
        for (List<AgentActionMetaInfo> neighbors : adjList.values()) {
          neighbors.remove(employeeCheckResult.getEmployee());
        }
        // 移除该节点
        adjList.remove(employeeCheckResult.getEmployee());
      }

      return true
    }

}
```