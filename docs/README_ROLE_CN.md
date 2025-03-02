# Touchpoint Protocol

The Touchpoint Protocol (TPP) is a collaboration communication protocol between agents, driven by AI models to facilitate inter-agent collaboration. It serves as the collaboration communication standard for the Intelligent Network (Smart Internet).

## 概述
TPP协议基于上下文 - 角色驱动模型，通过前置Action实现动态路由。具体接入方法如下：
- 在前置Action输出中添加状态码，目前支持的状态码：  
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
  备注：状态码不是必须的，开发者也可以自定义状态码。
- 后置Action添加角色注解，处理前置Action重定向过来的数据。当前支持4种角色：`Proposer` 、 `Executor` 、`Coordinator` 和 `Supervisor`。  
  `Proposer`：发起者，用于发起任务。  
  `Executor`：执行者，用于操作Data，执行Action。  
  `Coordinator`：协调者，用于操作Action和工作流。  
  `Supervisor`：监督者，用于检查Data、Action和工作流。
- 后置Action实现角色接口，当前支持的接口和基类有：  
`AgentActionExecutor`：用于执行Action。  
`DefaultDataChecker`：用于检查Action输入。  
`ActionGraphOperator`：用于修改工作流。  
`ActionOperator`：用于修改Action。  
`ActionPredictor`：用于预测行为。

## Example
比如，当地水果农户准备通过代理，将自己的水果销售出去，于是，拟定了一份代理授权书，但是，他不确定授权书是否有漏洞，所以，找律师检查一下，检查通过后，给到代理商。

第一步：实现 `DraftAuthLetter`，将 `NEED_CHECK_DATA` 状态添加进方法输出：
```kotlin
@TouchPointAction( 
  name = "drafting a letter of authorization", 
  desc = "drafting a letter of authorization to sell fruits",
  toActions = { "sales[\"Authorized Agent\"]" })
class DraftAuthLetter : AgentActionExecutor<FruitInfo, AuthLetter> {
   
   override fun run(message: FruitInfo, context: Context) : AuthLetter {
     AuthLetter letter = new AuthLetter();
     letter.setState(new TouchPointState(
               TaskState.NEED_CHECK_DATA.getCode(), // 状态码为NEED_CHECK_DATA，表示下一步需要检查数据
               "Please help check if there are any issues with the authorization letter?", // 状态描述
               "lawyer"); // 状态码状态码为NEED_CHECK_DATA对应的Action名称，即后置Action
     
     return letter;
   }
 
}
```

第二步：实现 `Lawyer`，标记`Lawyer`为监督者，检查授权书是否有漏洞，如果通过，则将授权书发送给代理商：
```kotlin
@TouchPointAction( 
  name = "lawyer"
  desc = "check if there are any issues with the authorization letter?"
  toActions = { "sales[\"Authorized Agent\"]" })
@Supervisor(task = "sales")
class Lawyer : DefaultDataChecker<AuthLetter> {

    override fun run(letter: AuthLetter, context: Context): Boolean {
        // 检查授权书是否有漏洞
        if (letter.hasIssues()) {
            return false
        }
        return true
    }

}
```
