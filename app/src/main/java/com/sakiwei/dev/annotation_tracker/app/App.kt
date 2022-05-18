package com.sakiwei.dev.annotation_tracker.app

import android.app.Application
import android.util.Log
import com.sakiwei.demo.annotationTracker.runtime.TrackPointSubscriber
import com.sakiwei.demo.annotationTracker.runtime.Trackable
import com.sakiwei.demo.annotationTracker.runtime.Tracker

class App : Application() {
  companion object {
    const val TAG = "App"
  }

  override fun onCreate() {
    Tracker.Builder(object : TrackPointSubscriber {
      override fun onCollectTrackPoint(trackPoint: Trackable) {
        Log.d(TAG, "[track-point]: $trackPoint")
      }
    }).build()

    super.onCreate()
  }
}