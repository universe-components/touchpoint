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

### Now

Step 1: Implement `AgentActionExecutor` Interface

```java
class MovieFile extends TouchPoint {

    private String fileName;

    public String getFileName() {
        return fileName;
    }
    
}

@TouchPointAction(name = "mp4_player", desc = "play mp4 video", toActions = {"movie[]"})
class Mp4Player implements AgentActionExecutor<MovieFile, TouchPoint> {

    @Override
    private TouchPoint run(MovieFile file, Context context) {
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
class Router implements AgentActionExecutor<MovieFile, TouchPoint> {

    @Override
    private TouchPoint run(MovieFile file, Context context) {
        if (file.getFileName().endsWith(".mp4")) {
            new Mp4Player().run(file, context);
        }
        if (file.getFileName().endsWith(".flv")) {
            new FlvPlayer().run(file, context);
        }
        return new TouchPoint();
    }

}
```

Step 3: Implement `Coordinator` to weave the router into the playback process.
```java
@TouchPointAction(name = "media_coordinator", desc = "add router to playback process", toActions = {"movie[]"})
@Coordinator(task = "movie")
class MediaCoordinator implements ActionGraphOperator<MovieFile> {

    @Override
    private ActionGraph run(MovieFile file, Context context) {
        String task = file.getContext().getTask();
        ActionGraph graph = TouchPointContextManager.getTouchPointContext(task).getActionGraph(); // `graph` represents the action relationship graph of the current task
        // Weave the router into the action graph, connecting the actions before and after playback
        
        return graph;
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
