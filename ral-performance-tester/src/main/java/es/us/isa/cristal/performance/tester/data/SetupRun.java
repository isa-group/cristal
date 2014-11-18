package es.us.isa.cristal.performance.tester.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SetupRun
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * @author resinas
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SetupRun {

}
