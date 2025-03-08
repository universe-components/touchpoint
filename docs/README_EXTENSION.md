# Touchpoint Protocol

The Touchpoint Protocol (TPP) is a collaboration communication protocol between agents, driven by AI models to facilitate inter-agent collaboration. It serves as the collaboration communication standard for the Intelligent Network (Smart Internet).

## Case 1: Custom Media Player Playback Feature

Select different players to play the video based on the file extension.

### Past

Step 1: Define an Interface
```java
public interface MediaPlayer {

    void play();

}
```

Step 2: Implement the Interface
```java
public class Mp4Player implements MediaPlayer {

    @Override
    public void play() {
        // play video
    }

}
```

```java
public class FlvPlayer implements MediaPlayer {

    @Override
    public void play() {
        // play video
    }

}
```

Step 3: In the playback handling logic, add this interface and call it to play the video.
```java
public class VideoProcessor {
    
    public static void run(String fileName) {
        ......
        ......
        
        MediaPlayer player = new MediaPlayerSelector().select(fileName);
        player.play();
    }
    
}
```

Step 4: Compile and publish the project containing the interface.
```shell
mvn clean install & mvn deploy
reboot player
```

### Now

Step 1: Implement `AgentActionExecutor` Interface
```java
@TouchPointAction(name = "mp4_player", desc = "play mp4 video", toActions = {"movie[]"})
class Mp4Player implements AgentActionExecutor<String, TouchPoint> {

    @Override
    private TouchPoint run(String file, TouchPointContext context) {
        // play video
        ......
        ......
        
        return new TouchPoint();
    }

}
```

Step 2: Define the router's Action to choose different players based on the file extension to play the video.
```java
@TouchPointAction(name = "router", desc = "select player to play video", toActions = {"movie[]"})
class Router implements AgentActionExecutor<String, TouchPoint> {

    @Override
    private TouchPoint run(String file, TouchPointContext context) {
        TouchPoint response = new TouchPoint();
        if (file.getFileName().endsWith(".mp4")) {
            response.setState(new TouchPointState(
                    TaskState.OK.getCode(),
                    "use mp4_player to play video",
                    "mp4_player"));
        }
        if (file.getFileName().endsWith(".flv")) {
            response.setState(new TouchPointState(
                    TaskState.OK.getCode(),
                    "use flv_player to play video",
                    "flv_player"));
        }
        return response;
    }

}
```

Step 3: Implement `Coordinator` to weave the router into the playback process.
```java
@TouchPointAction(name = "media_coordinator", desc = "add router to playback process", toActions = {"movie[]"})
@Coordinator(task = "movie")
class MediaCoordinator implements ActionGraphOperator<String> {

    @Override
    private ActionGraph run(String file, TouchPointContext context) {
        String task = context.getTask();
        ActionGraph graph = TouchPointContextManager.getTouchPointContext(task).getActionGraph(); // `graph` represents the action relationship graph of the current task
        // Weave the router into the action graph, connecting the actions before and after playback
        
        return graph;
    }

}
```

Step 4: Add the environment variables to the context to trigger the `MediaCoordinator` to automatically weave in the router, without compilation, publishing, or restarting.
```java
@Task("movie")
public class VideoSocket extends TaskSocket {
}
```

```java
public class VideoProcessor {

    @Autowired
    private final VideoSocket videoSocket; 
    
    public static void run(String fileName) {
        ......
        ......

        // The following code is already in the production environment and does not require modification.
        // In this case, `route_player` is flag, and `media_coordinator` is the Action corresponding to that flag, which is passed through parameters from the sensor.
        TouchPointContext ctx = new TouchPointContext("movie");
        ctx.getTaskContext().setActionGraphContext(new ActionGraphContext("route_player", "media_coordinator"));
        videoSocket.send("Video Playback Processing: Parse Protocol -> Parse Container Format -> Audio and Video Decoding -> Audio and Video Synchronization -> Rendering and Playback", ctx);
    }
    
}
```

## Case 2: Bypassing the TCP Protocol Stack

To be updated

## Conclusion
|      | Interface Pattern                    | Coordinator Pattern (TPP)   |
|------|--------------------------|-----------------------------|
| User-Friendliness  | Requires predefined system interfaces, then implements those interfaces               | No need to define any interfaces; all extended functionalities are implemented as Actions  |
| Flexibility  | Can only replace fixed interfaces and cannot adjust their position in the workflow | Can replace fixed interfaces and also adjust their position in the workflow |
| Maintainability | Requires recompilation and packaging before deployment            | No need to recompile; extended functionalities can be added online via    |
