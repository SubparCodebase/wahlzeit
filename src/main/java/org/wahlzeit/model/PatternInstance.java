package org.wahlzeit.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Repeatable(PatternInstances.class)
public @interface PatternInstance {
    String patternName();
    String[] participants();
}
