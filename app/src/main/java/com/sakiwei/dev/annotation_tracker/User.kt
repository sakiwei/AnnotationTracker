package com.sakiwei.dev.annotation_tracker

import com.sakiwei.demo.annotationTracker.runtime.TrackAttributeProvider

data class User(val id: Int, val email: String, val name: String): TrackAttributeProvider {
  override fun getTrackAttributes(): Map<String, Any> {
    return mapOf("id" to id, "email" to email, "username" to name)
  }
}
