package com.nineoldandroids.animation;

import android.view.View;
import com.nineoldandroids.util.Property;
import com.nineoldandroids.view.animation.AnimatorProxy;
import java.util.HashMap;
import java.util.Map;

public final class ObjectAnimator extends ValueAnimator {
    private static final boolean DBG = false;
    private static final Map<String, Property> PROXY_PROPERTIES = new HashMap();
    private Property mProperty;
    private String mPropertyName;
    private Object mTarget;

    static {
        PROXY_PROPERTIES.put("alpha", PreHoneycombCompat.ALPHA);
        PROXY_PROPERTIES.put("pivotX", PreHoneycombCompat.PIVOT_X);
        PROXY_PROPERTIES.put("pivotY", PreHoneycombCompat.PIVOT_Y);
        PROXY_PROPERTIES.put("translationX", PreHoneycombCompat.TRANSLATION_X);
        PROXY_PROPERTIES.put("translationY", PreHoneycombCompat.TRANSLATION_Y);
        PROXY_PROPERTIES.put("rotation", PreHoneycombCompat.ROTATION);
        PROXY_PROPERTIES.put("rotationX", PreHoneycombCompat.ROTATION_X);
        PROXY_PROPERTIES.put("rotationY", PreHoneycombCompat.ROTATION_Y);
        PROXY_PROPERTIES.put("scaleX", PreHoneycombCompat.SCALE_X);
        PROXY_PROPERTIES.put("scaleY", PreHoneycombCompat.SCALE_Y);
        PROXY_PROPERTIES.put("scrollX", PreHoneycombCompat.SCROLL_X);
        PROXY_PROPERTIES.put("scrollY", PreHoneycombCompat.SCROLL_Y);
        PROXY_PROPERTIES.put("x", PreHoneycombCompat.f85X);
        PROXY_PROPERTIES.put("y", PreHoneycombCompat.f86Y);
    }

    public void setPropertyName(String str) {
        if (this.mValues != null) {
            PropertyValuesHolder propertyValuesHolder = this.mValues[0];
            String propertyName = propertyValuesHolder.getPropertyName();
            propertyValuesHolder.setPropertyName(str);
            this.mValuesMap.remove(propertyName);
            this.mValuesMap.put(str, propertyValuesHolder);
        }
        this.mPropertyName = str;
        this.mInitialized = false;
    }

    public void setProperty(Property property) {
        if (this.mValues != null) {
            PropertyValuesHolder propertyValuesHolder = this.mValues[0];
            String propertyName = propertyValuesHolder.getPropertyName();
            propertyValuesHolder.setProperty(property);
            this.mValuesMap.remove(propertyName);
            this.mValuesMap.put(this.mPropertyName, propertyValuesHolder);
        }
        if (this.mProperty != null) {
            this.mPropertyName = property.getName();
        }
        this.mProperty = property;
        this.mInitialized = false;
    }

    public String getPropertyName() {
        return this.mPropertyName;
    }

    public ObjectAnimator() {
    }

    private ObjectAnimator(Object obj, String str) {
        this.mTarget = obj;
        setPropertyName(str);
    }

    private <T> ObjectAnimator(T t, Property<T, ?> property) {
        this.mTarget = t;
        setProperty(property);
    }

    public static ObjectAnimator ofInt(Object obj, String str, int... iArr) {
        ObjectAnimator objectAnimator = new ObjectAnimator(obj, str);
        objectAnimator.setIntValues(iArr);
        return objectAnimator;
    }

    public static <T> ObjectAnimator ofInt(T t, Property<T, Integer> property, int... iArr) {
        ObjectAnimator objectAnimator = new ObjectAnimator(t, property);
        objectAnimator.setIntValues(iArr);
        return objectAnimator;
    }

    public static ObjectAnimator ofFloat(Object obj, String str, float... fArr) {
        ObjectAnimator objectAnimator = new ObjectAnimator(obj, str);
        objectAnimator.setFloatValues(fArr);
        return objectAnimator;
    }

    public static <T> ObjectAnimator ofFloat(T t, Property<T, Float> property, float... fArr) {
        ObjectAnimator objectAnimator = new ObjectAnimator(t, property);
        objectAnimator.setFloatValues(fArr);
        return objectAnimator;
    }

    public static ObjectAnimator ofObject(Object obj, String str, TypeEvaluator typeEvaluator, Object... objArr) {
        ObjectAnimator objectAnimator = new ObjectAnimator(obj, str);
        objectAnimator.setObjectValues(objArr);
        objectAnimator.setEvaluator(typeEvaluator);
        return objectAnimator;
    }

    public static <T, V> ObjectAnimator ofObject(T t, Property<T, V> property, TypeEvaluator<V> typeEvaluator, V... vArr) {
        ObjectAnimator objectAnimator = new ObjectAnimator(t, property);
        objectAnimator.setObjectValues(vArr);
        objectAnimator.setEvaluator(typeEvaluator);
        return objectAnimator;
    }

    public static ObjectAnimator ofPropertyValuesHolder(Object obj, PropertyValuesHolder... propertyValuesHolderArr) {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        objectAnimator.mTarget = obj;
        objectAnimator.setValues(propertyValuesHolderArr);
        return objectAnimator;
    }

    public void setIntValues(int... iArr) {
        if (this.mValues == null || this.mValues.length == 0) {
            Property property = this.mProperty;
            if (property != null) {
                setValues(PropertyValuesHolder.ofInt(property, iArr));
                return;
            }
            setValues(PropertyValuesHolder.ofInt(this.mPropertyName, iArr));
            return;
        }
        super.setIntValues(iArr);
    }

    public void setFloatValues(float... fArr) {
        if (this.mValues == null || this.mValues.length == 0) {
            Property property = this.mProperty;
            if (property != null) {
                setValues(PropertyValuesHolder.ofFloat(property, fArr));
                return;
            }
            setValues(PropertyValuesHolder.ofFloat(this.mPropertyName, fArr));
            return;
        }
        super.setFloatValues(fArr);
    }

    public void setObjectValues(Object... objArr) {
        if (this.mValues == null || this.mValues.length == 0) {
            Property property = this.mProperty;
            if (property != null) {
                setValues(PropertyValuesHolder.ofObject(property, null, (V[]) objArr));
                return;
            }
            setValues(PropertyValuesHolder.ofObject(this.mPropertyName, (TypeEvaluator) null, objArr));
            return;
        }
        super.setObjectValues(objArr);
    }

    public void start() {
        super.start();
    }

    /* access modifiers changed from: 0000 */
    public void initAnimation() {
        if (!this.mInitialized) {
            if (this.mProperty == null && AnimatorProxy.NEEDS_PROXY && (this.mTarget instanceof View) && PROXY_PROPERTIES.containsKey(this.mPropertyName)) {
                setProperty((Property) PROXY_PROPERTIES.get(this.mPropertyName));
            }
            for (PropertyValuesHolder propertyValuesHolder : this.mValues) {
                propertyValuesHolder.setupSetterAndGetter(this.mTarget);
            }
            super.initAnimation();
        }
    }

    public ObjectAnimator setDuration(long j) {
        super.setDuration(j);
        return this;
    }

    public Object getTarget() {
        return this.mTarget;
    }

    public void setTarget(Object obj) {
        Object obj2 = this.mTarget;
        if (obj2 != obj) {
            this.mTarget = obj;
            if (obj2 == null || obj == null || obj2.getClass() != obj.getClass()) {
                this.mInitialized = false;
            }
        }
    }

    public void setupStartValues() {
        initAnimation();
        for (PropertyValuesHolder propertyValuesHolder : this.mValues) {
            propertyValuesHolder.setupStartValue(this.mTarget);
        }
    }

    public void setupEndValues() {
        initAnimation();
        for (PropertyValuesHolder propertyValuesHolder : this.mValues) {
            propertyValuesHolder.setupEndValue(this.mTarget);
        }
    }

    /* access modifiers changed from: 0000 */
    public void animateValue(float f) {
        super.animateValue(f);
        for (PropertyValuesHolder animatedValue : this.mValues) {
            animatedValue.setAnimatedValue(this.mTarget);
        }
    }

    public ObjectAnimator clone() {
        return (ObjectAnimator) super.clone();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ObjectAnimator@");
        sb.append(Integer.toHexString(hashCode()));
        sb.append(", target ");
        sb.append(this.mTarget);
        String sb2 = sb.toString();
        if (this.mValues != null) {
            for (PropertyValuesHolder propertyValuesHolder : this.mValues) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(sb2);
                sb3.append("\n    ");
                sb3.append(propertyValuesHolder.toString());
                sb2 = sb3.toString();
            }
        }
        return sb2;
    }
}
