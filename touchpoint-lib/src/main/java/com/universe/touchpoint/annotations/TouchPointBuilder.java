//package com.universe.touchpoint.annotations;
//
//import com.qihoo360.replugin.RePlugin;
//import com.universe.touchpoint.TouchPointContextManager;
//
//import javax.inject.Inject;
//import java.lang.annotation.Annotation;
//
//import dagger.Module;
//import dagger.Provides;
//import dagger.hilt.InstallIn;
//import dagger.hilt.components.SingletonComponent;
//
//@Module
//@InstallIn(SingletonComponent.class)
//public class TouchPointBuilder {
//
//    @Provides
//    @TouchPoint
//    public com.universe.touchpoint.TouchPoint touchPoint() {
//
//        return new NetworkServiceImpl();
//    }
//
//    @Inject
//    public com.universe.touchpoint.TouchPoint generateTouchPoint() {
//        Annotation[] annotations = this.getClass().getAnnotations();
//        for (java.lang.annotation.Annotation annotation : annotations) {
//            if (annotation instanceof TouchPoint) {
//                return TouchPointContextManager.generateTouchPoint(this.getClass());
//            }
//        }
//        return null;
//    }
//
//}
