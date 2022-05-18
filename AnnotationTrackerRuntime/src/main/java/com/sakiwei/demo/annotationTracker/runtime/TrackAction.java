package com.sakiwei.demo.annotationTracker.runtime;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TrackAction {
    String value();
    boolean isTrackAtBeginning() default true;
}
