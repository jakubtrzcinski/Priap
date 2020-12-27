package pl.jakubtrzcinski.priap.api;

import java.io.Serializable;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 27-12-2020
 */
public interface EventDispatcher {
    /**
     * Fire and Forget
     */
    void dispatch(Serializable event);
}
