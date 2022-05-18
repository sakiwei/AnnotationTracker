package com.sakiwei.demo.annotationTracker.runtime

interface Trackable {
  val name: String
  val attributeMap: Map<String, Any>
  val sharedAttributeMap: Map<String, Any>
}