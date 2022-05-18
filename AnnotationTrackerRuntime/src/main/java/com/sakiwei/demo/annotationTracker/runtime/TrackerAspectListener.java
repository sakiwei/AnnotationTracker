package com.sakiwei.demo.annotationTracker.runtime;

import androidx.annotation.NonNull;

public interface TrackerAspectListener {
    void onReceiveTrackPoint(@NonNull Trackable trackPoint);
}
