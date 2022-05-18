package com.sakiwei.demo.annotationTracker.runtime

data class TrackPoint(
  override val name: String,
  override val attributeMap: Map<String, Any>,
  override val sharedAttributeMap: Map<String, Any>
) : Trackable