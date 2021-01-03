package pl.jakubtrzcinski.priap;

import pl.jakubtrzcinski.priap.api.EventListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 27-12-2020
 */
@SuppressWarnings("rawtypes")
class ListenerCollection {
    private final Map<Class, List<EventListener>> listeners = new HashMap<>();


    public ListenerCollection(List<EventListener> listeners) {
        listeners.forEach(this::register);
    }

    public List<EventListener> findAllByEventType(Class clazz){
        return new LinkedList<>(
                listeners.getOrDefault(clazz, new LinkedList<>())
        );
    }
    public void register(EventListener eventListener){
        var clazz = GenericsUtils.getGeneric(eventListener);
        if(!listeners.containsKey(clazz)){
            listeners.put(clazz, new LinkedList<>());
        }
        listeners.get(clazz).add(eventListener);
    }
}
