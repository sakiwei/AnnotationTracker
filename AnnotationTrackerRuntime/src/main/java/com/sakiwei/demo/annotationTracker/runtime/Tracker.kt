package com.sakiwei.demo.annotationTracker.runtime

class Tracker private constructor(private val pointSubscriber: TrackPointSubscriber) :
  TrackerAspectListener {

  companion object {
    var sharedAttributeMap: Map<String, Any> = mutableMapOf()

    fun addAttribute(key: String, value: String) {
      sharedAttributeMap = sharedAttributeMap.toMutableMap().apply {
        put(key, value)
      }
    }

    fun removeAttribute(key: String) {
      sharedAttributeMap = sharedAttributeMap.toMutableMap().apply {
        remove(key)
      }
    }
  }

  init {
    TrackerAspect.setListener(this)
  }

  data class Builder(val pointSubscriber: TrackPointSubscriber) {
    fun build() = Tracker(pointSubscriber)
  }

  fun track(name: String, attributeMap: Map<String, Any>) {
    track(TrackPoint(name, attributeMap, sharedAttributeMap))
  }

  private fun track(trackPoint: Trackable) {
    pointSubscriber.onCollectTrackPoint(trackPoint)
  }

  // TrackActionSubscriber
  override fun onReceiveTrackPoint(trackPoint: Trackable) {
    track(trackPoint)
  }
}