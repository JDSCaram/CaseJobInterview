package com.entrevista.ifood2.dagger.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * {Created by Jonatas Caram on 22/06/2017}.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerFragmentScope {
}
