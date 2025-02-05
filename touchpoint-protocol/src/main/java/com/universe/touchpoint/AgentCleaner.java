//package com.universe.touchpoint;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//
//import com.universe.touchpoint.agent.Agent;
//import com.universe.touchpoint.context.AgentContext;
//import com.universe.touchpoint.driver.ActionGraph;
//import com.universe.touchpoint.helper.TouchPointHelper;
//import com.universe.touchpoint.memory.TouchPointMemory;
//
//public class AgentCleaner extends AgentReporter<AgentCleaner.AgentClean> {
//
//    @Override
//    public void report(AgentClean clean, Context context) {
//        Intent intent = new Intent(
//                TouchPointHelper.touchPointFilterName(
//                        TouchPointConstants.TOUCH_POINT_CLEAN_FILTER, Agent.getName()));
//        intent.putExtra(TouchPointConstants.TOUCH_POINT_CLEAN_EVENT_NAME, clean.type);
//
//        context.sendBroadcast(intent);
//    }
//
//    @Override
//    public <C extends AgentContext> void registerReceiver(Context appContext, C context) {
//        IntentFilter filter = new IntentFilter(
//                TouchPointHelper.touchPointFilterName(
//                        TouchPointConstants.TOUCH_POINT_CLEAN_FILTER,
//                        agentName));
//        appContext.registerReceiver(new AgentCleanerReceiver(), filter, Context.RECEIVER_EXPORTED);
//    }
//
//    public static class AgentCleanerReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            int cleanType = intent.getIntExtra(TouchPointConstants.TOUCH_POINT_CLEAN_EVENT_NAME, 0);
//            if (cleanType == AgentClean.ALL.type) {
//                ActionGraph.getInstance().clear();
//                TouchPointMemory.clear();
//            }
//        }
//
//    }
//
//    public enum AgentClean {
//        ALL(0);
//
//        private final int type;
//
//        AgentClean(int type) {
//            this.type = type;
//        }
//    }
//
//}
