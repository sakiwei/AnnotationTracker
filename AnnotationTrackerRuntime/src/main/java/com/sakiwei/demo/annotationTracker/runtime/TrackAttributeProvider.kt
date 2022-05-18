package com.sakiwei.demo.annotationTracker.runtime

interface TrackAttributeProvider {
  fun getTrackAttributes(): Map<String, Any>
}