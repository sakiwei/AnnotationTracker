package com.sakiwei.demo.annotationTracker.runtime

interface TrackPointSubscriber {
  fun onCollectTrackPoint(action: Trackable)
}