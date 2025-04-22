# Touchpoint Protocol

The Touchpoint Protocol (TPP) is a collaboration communication protocol between agents, driven by AI models to facilitate inter-agent collaboration. It serves as the collaboration communication standard for the Intelligent Network (Smart Internet).

## Example
For example, a local fruit farmer plans to sell their fruit through an agent and has drafted an authorization letter. However, he is unsure if the letter contains any issues, so he consult a lawyer to check it. Once the letter is reviewed and approved, it is given to the agent.

Step 1: Implement `DraftAuthLetter` and add the `NEED_CHECK_DATA` status to the method output:
```kotlin
@TouchPointAction(
  name = "drafting a letter of authorization",
  desc = "drafting a letter of authorization to sell fruits",
  toActions = { "sales[\"Authorized Agent\"]" })
class DraftAuthLetter : AgentActionExecutor<FruitInfo, AuthLetter> {

  override fun run(message: FruitInfo, context: TouchPointContext) : AuthLetter {
    AuthLetter letter = new AuthLetter(message);
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
  name = "lawyer",
  desc = "check if there are any issues with the authorization letter?",
  toActions = { "sales[\"Authorized Agent\"]" },
  operateType = OperateType.CHECK_DATA
)
class Lawyer : DefaultDataChecker<AuthLetter> {

  override fun run(letter: AuthLetter, context: TouchPointContext): Boolean {
    // Check if there are any issues with the authorization letter
    if (letter.hasIssues()) {
      return false
    }
    return true
  }

}
```
