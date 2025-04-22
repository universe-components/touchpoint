# Touchpoint Protocol

The Touchpoint Protocol (TPP) is a collaboration communication protocol between agents, driven by AI models to facilitate inter-agent collaboration. It serves as the collaboration communication standard for the Intelligent Network (Smart Internet).

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
  name = "lawyer",
  desc = "check if there are any issues with the authorization letter?",
  toActions = { "sales[\"Authorized Agent\"]" },
  operateType = OperateType.CHECK_DATA)
class Lawyer : DefaultChecker<AuthLetter> {

    override fun run(letter: AuthLetter, context: Context): Boolean {
        // 检查授权书是否有漏洞
        if (letter.hasIssues()) {
            return false
        }
        return true
    }

}
```
