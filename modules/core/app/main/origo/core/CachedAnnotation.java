package main.origo.core;

import main.origo.core.annotations.Relationship;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class CachedAnnotation implements Comparable<CachedAnnotation> {

    public final Annotation annotation;
    public final Method method;
    public final Relationship relationship;

    public CachedAnnotation(Annotation annotation, Method method) {
        this.annotation = annotation;
        this.method = method;
        this.relationship = null;
    }

    public CachedAnnotation(Annotation annotation, Method method, Relationship relationship) {
        this.annotation = annotation;
        this.method = method;
        this.relationship = relationship;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CachedAnnotation that = (CachedAnnotation) o;

        return !(annotation != null ? !annotation.equals(that.annotation) : that.annotation != null)
                && !(method != null ? !method.equals(that.method) : that.method != null);
    }

    @Override
    public int hashCode() {
        int result = annotation != null ? annotation.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(CachedAnnotation cachedAnnotation) {
        return new Integer(hashCode()).compareTo(cachedAnnotation.hashCode());
    }

    public interface InterceptorSelector {
        boolean isCorrectInterceptor(CachedAnnotation cachedAnnotation);
    }

}
