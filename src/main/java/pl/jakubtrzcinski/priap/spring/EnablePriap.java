package pl.jakubtrzcinski.priap.spring;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 28-12-2020
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableRabbit
@Import(PriapConfiguration.class)
public @interface EnablePriap {}
