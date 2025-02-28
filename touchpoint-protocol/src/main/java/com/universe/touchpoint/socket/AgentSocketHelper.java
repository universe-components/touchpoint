package com.universe.touchpoint.socket;

public class AgentSocketHelper {

    public static String socketFilter(String type, String task, String role) {
        return String.join("_", type, task, role);
    }

    public static String replaceFilterRole(String filter, String role) {
        String[] parts = filter.split("_");
        parts[parts.length - 1] = role;
        return String.join("_", parts);
    }

    public static String extractTask(String filter) {
        String[] parts = filter.split("_");
        return parts[parts.length - 2];
    }

    public static String extractRole(String filter) {
        String[] parts = filter.split("_");
        return parts[parts.length - 1];
    }

}
