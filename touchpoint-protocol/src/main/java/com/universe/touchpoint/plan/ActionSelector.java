package com.universe.touchpoint.plan;

import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import java.util.List;

public interface ActionSelector<Req> {

  List<AgentActionMeta> select(String task, SocketRequest<Req> request);
}
