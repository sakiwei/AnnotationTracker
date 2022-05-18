# Annotation Tracker Sample

This is a sample project to make use of AspectJ to create a centralized tracker library which support tracking by annotations.

---

## How to build AAR library

Run this gradle command from project root:

```
$ ./gradlew :AnnotationTrackerRuntime:assemble
```

Then AAR library will be generated in: 

```
AnnotationTrackerRuntime/build/outputs/aar/
```

---

### How to use the AnnotationTrackerRuntime

#### By adding AAR library (Recommended) 

1. Copy the `AnnotationTrackerRuntime.aar` to `app/libs`
2. Add the following lines to `app/build.gradle` under `dependencies`:

```
implementation fileTree(dir: "libs", include: ["*.aar", "*.jar"])
implementation "org.aspectj:aspectjrt:1.9.8"
```

#### By embeding whole library module (source code)

1. Copy the `AnnotationTrackerRuntime` module to your own project.
2. Add `include ':AnnotationTrackerRuntime'` to settings.gradle
3. Add the following lines to `app/build.gradle` under `dependencies`:

```
implementation project(":AnnotationTrackerRuntime")
implementation "org.aspectj:aspectjrt:1.9.8"
```

---

### Author

__Wai Cheung__

*  [Github](https://github.com/sakiwei)

*  [LinkedIn](https://www.linkedin.com/in/sakiwei/)
