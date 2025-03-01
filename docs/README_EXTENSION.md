# Touchpoint Protocol

The Touchpoint Protocol (TPP) is a collaboration communication protocol between agents, driven by AI models to facilitate inter-agent collaboration. It serves as the collaboration communication standard for the Intelligent Network (Smart Internet).

## Case 1: Custom Media Player Playback Feature

### Past

Step 1: Define an Interface
```java
public interface MediaPlayer {

    void play(String fileName);

}
```

Step 2: Implement the Interface
```java
public class Mp4Player implements MediaPlayer {

    @Override
    public void play(String fileName) {
        // play video
    }

}
```

```java
public class FlvPlayer implements MediaPlayer {

    @Override
    public void play(String fileName) {
        // play video
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

Step 2: Implement `Coordinator` to Switch Playback Format
```java
@TouchPointAction(name = "media_coordinator", desc = "switch video player", toActions = {"movie[]"})
@Coordinator(task = "movie")
class MediaCoordinator implements ActionGraphOperator<MovieFile> {

    @Override
    private ActionGraph run(MovieFile file, Context context) {
        String task = file.getContext().getTask();
        ActionGraph graph = TouchPointContextManager.getTouchPointContext(task).getActionGraph(); // `graph` represents the action relationship graph of the current task
        if (file.getFileName().endsWith(".mp4")) {
            // replace the player action to mp4 in graph
            ......
            ......
        }
        if (file.getFileName().endsWith(".flv")) {
            // replace the player action to flv in graph
            ......
            ......
        }
        return graph;
    }

}
```
Note: Developers can also define different coordinators for different file formats, such as separate implementations for `mp4` and `flv` coordinators.

## Case 2: Bypassing the TCP Protocol Stack

To be updated

## 总结
|      | Interface Pattern                    | Coordinator Pattern (TPP)   |
|------|--------------------------|-----------------------------|
| User-Friendliness  | Requires predefined system interfaces, then implements those interfaces               | No need to define any interfaces; all extended functionalities are implemented as Actions  |
| Flexibility  | Can only replace fixed interfaces and cannot adjust their position in the workflow | Can replace fixed interfaces and also adjust their position in the workflow |
| Maintainability | Requires recompilation and packaging before deployment            | No need to recompile; extended functionalities can be added online via    |
