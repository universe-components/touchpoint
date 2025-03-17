package com.universe.touchpoint.config.mapping;

import com.universe.touchpoint.annotations.role.ExceptionHandler;
import com.universe.touchpoint.config.role.ExceptionHandlerConfig;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class ExceptionHandlerConfigMapping {

  public static final Map<Class<? extends Annotation>, Class<?>> annotation2Config =
      new HashMap<>();

  static {
    annotation2Config.put(ExceptionHandler.class, ExceptionHandlerConfig.class);
  }
}
