package pl.jakubtrzcinski.priap;

import lombok.RequiredArgsConstructor;
import pl.jakubtrzcinski.priap.api.EventListener;
import pl.jakubtrzcinski.priap.dto.PriapMessage;

import java.util.List;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 27-12-2020
 */
@RequiredArgsConstructor
class PriapMessageExecutor {

    private final ListenerCollection listenerCollection;

    public PriapMessageExecutor(List<EventListener> listeners) {
        listenerCollection = new ListenerCollection(listeners);
    }

    void execute(PriapMessage message) {

        var event = message.getContent();

        //noinspection unchecked
        listenerCollection.findAllByEventType(event.getClass())
                .stream()
                .filter(e -> message.getInvokeOn() == null || message.getInvokeOn().contains(e.getClass()))
                .forEach(e -> {
                    try {
                        e.process(event);
                    } catch (Exception ex){
                    }
                });


    }

}
