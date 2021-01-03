package pl.jakubtrzcinski.priap.api;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 27-12-2020
 */
public interface EventListener<T> {
    void process(T event);
}
