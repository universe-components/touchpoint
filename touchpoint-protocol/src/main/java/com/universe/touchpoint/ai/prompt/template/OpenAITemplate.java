package com.universe.touchpoint.ai.prompt.template;

public class OpenAITemplate {

    public static final String PREFIX = "Answer the following questions as best you can. You have access to the following tools:";
    public static final String FORMAT_INSTRUCTIONS = """
            Use the following format:
            
            Question: the input question you must answer
            Thought: you should always think about what to do
            Action: the agentActionMeta to take, should be one of [{agent_names}]
            Action Input: the input to the agentActionMeta
            Observation: the result of the agentActionMeta
            ... (this Thought/Action/Action Input/Observation can repeat N times)
            Thought: I now know the final answer
            Final Answer: the final answer to the original input question""";

    public static final String SUFFIX = """
            Begin!
            
            Question: {input}
            Thought: {agent_scratchpad}""";

}
