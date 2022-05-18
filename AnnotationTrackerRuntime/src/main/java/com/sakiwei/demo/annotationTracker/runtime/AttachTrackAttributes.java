package com.sakiwei.demo.annotationTracker.runtime;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AttachTrackAttributes {
    AttachTrackAttribute[] values();
}
