package com.example.dracoworld.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {

	String[] value() default {}; // 권한(ADMIN, NORMAL, BANNED) 리스트

	boolean checkAuthor() default false;

	String Type() default "";

	String idParam() default "id";

	AuthorType AUTHOR_TYPE();

}
