package com.sakiwei.demo.annotationTracker.runtime;

import androidx.annotation.Nullable;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
public class TrackerAspect {

    private static TrackerAspectListener listener;

    static void setListener(TrackerAspectListener listener) {
        TrackerAspect.listener = listener;
    }

    @Pointcut("execution(@com.sakiwei.demo.annotationTracker.runtime.TrackAction * *(..))")
    public void methodAnnotatedTrackAction() {
        // No implementation is needed
    }

    @Pointcut("execution(@com.sakiwei.demo.annotationTracker.runtime.TrackAction *.new(..))")
    public void constructorAnnotatedTrackAction() {
        // No implementation is needed
    }

    @Around("methodAnnotatedTrackAction() || constructorAnnotatedTrackAction()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        TrackAction trackAction = method.getAnnotation(TrackAction.class);

        if (trackAction == null) {
            return joinPoint.proceed();
        }

        boolean isTrackAtBeginning = trackAction.isTrackAtBeginning();

        if (isTrackAtBeginning) {
            Map<String, Object> attributeMap = getAttributes(method, joinPoint);
            sendTrackPoint(trackAction, attributeMap);
            return joinPoint.proceed();
        } else {
            Object result = joinPoint.proceed();
            Map<String, Object> attributeMap = getAttributes(method, joinPoint);
            sendTrackPoint(trackAction, attributeMap);
            return result;
        }
    }

    private Map<String, Object> getAttributes(Method method, JoinPoint joinPoint) throws Throwable {
        Class<?> declaringClass = method.getDeclaringClass();

        Map<String, Object> map = new HashMap<>();

        while (declaringClass != null) {
            addClassAttributes(declaringClass, map);
            declaringClass = declaringClass.getEnclosingClass();
        }

        declaringClass = joinPoint.getThis().getClass();
        addClassAttributes(declaringClass, map);
        addMethodAttributes(method, map);
        addParameterAttributes(method, joinPoint, map);
        return map;
    }

    private void addClassAttributes(Class<?> declaringClass, Map<String, Object> map) {
        addAttachedAttribute(declaringClass.getAnnotation(AttachTrackAttribute.class), map);
        addAttachedAttributes(declaringClass.getAnnotation(AttachTrackAttributes.class), map);
    }

    private void addAttachedAttribute(@Nullable AttachTrackAttribute attribute, Map<String, Object> map) {
        if (attribute == null) {
            return;
        }
        map.put(attribute.key(), attribute.value());
    }

    private void addAttachedAttributes(@Nullable AttachTrackAttributes attributes, Map<String, Object> map) {
        if (attributes == null) {
            return;
        }
        AttachTrackAttribute[] attributeList = attributes.values();
        for (AttachTrackAttribute attributeItem : attributeList) {
            map.put(attributeItem.key(), attributeItem.value());
        }
    }

    private void addMethodAttributes(Method method, Map<String, Object> map) {
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof AttachTrackAttribute) {
                addAttachedAttribute((AttachTrackAttribute) annotation, map);
            }
            if (annotation instanceof AttachTrackAttributes) {
                addAttachedAttributes((AttachTrackAttributes) annotation, map);
            }
        }
    }

    private void addParameterAttributes(Method method, JoinPoint joinPoint, Map<String, Object> map) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Annotation[][] keys = method.getParameterAnnotations();
        if (args == null) {
            // no parameter
            return;
        }

        int keySize = keys.length;
        for (int i = 0; i < keySize; i++) {
            if (keys[i].length == 0) {
                // no annotation
                continue;
            }

            Object value = args[i];
            Annotation annotation = keys[i][0];
            if (annotation instanceof TrackParameter) {
                if (value instanceof TrackAttributeProvider) {
                    TrackAttributeProvider provider = (TrackAttributeProvider) value;
                    map.putAll(provider.getTrackAttributes());
                }
            }
            if (annotation instanceof TrackParameterNamed) {
                if (value instanceof TrackAttributeProvider) {
                    throw new RuntimeException("TrackParameterNamed and TrackAttributeProvider cannot be used together. It causes a conflict in attribute's name ");
                }
                String name = ((TrackParameterNamed) annotation).name();
                map.put(name, value);
            }
        }
    }

    private void sendTrackPoint(TrackAction trackAction, Map<String, Object> attributeMap) {
        if (TrackerAspect.listener == null || trackAction == null) return;

        TrackPoint object = new TrackPoint(
                trackAction.value(),
                attributeMap,
                Tracker.Companion.getSharedAttributeMap());
        TrackerAspect.listener.onReceiveTrackPoint(object);
    }

}
