# Touchpoint Protocol

`touchpoint-protocol` ，触点协议（TPP协议），一个Agent之间协作通信的协议，该协议通过AI模型驱动Agent之间协作，是智联网的协作通信标准。

## 案例1：以自定义播放器播放功能为例

### 过去

第一步：定义接口
```java
public interface MediaPlayer {

    void play(String fileName);

}
```

第二步：实现接口
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
### 现在

第一步：实现执行者接口

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

第二步：实现协调者，替换现有播放格式
```java
@TouchPointAction(name = "media_coordinator", desc = "switch video player", toActions = {"movie[]"})
@Coordinator(task = "movie")
class MediaCoordinator implements ActionGraphOperator<MovieFile> {

    @Override
    private ActionGraph run(MovieFile file, Context context) {
        String task = file.getContext().getTask();
        ActionGraph graph = TouchPointContextManager.getTouchPointContext(task).getActionGraph(); // graph为当前任务的行为关系图
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
备注：开发者也可以为不同的文件格式定义不同的协调者，比如，分别实现 `mp4` 和 `flv` 的协调者。

## 案例2：绕过TCP协议栈

待更新

## 总结
|      | 接口模式                     | 协调者模式（TPP协议）                |
|------|--------------------------|-----------------------------|
| 友好性  | 需要系统内已定义接口，然后实现该接口               | 无需定义任何接口，所有扩展功能以Action方式实现  |
| 灵活性  | 只能替换固定位接口，无法调整接口在工作流中的位置 | 不仅可以替换固定位接口，还可以调整接口在工作流中的位置 |
| 可维护性 | 需重新编译打包，再进行发布            | 无需重新编译，在线添加扩展功能的Action即可    |
