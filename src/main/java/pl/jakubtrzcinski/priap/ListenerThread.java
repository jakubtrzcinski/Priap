package pl.jakubtrzcinski.priap;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import pl.jakubtrzcinski.priap.api.EventListener;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 27-12-2020
 */
@Slf4j
class ListenerThread extends Thread implements Closeable {

    private final String queueName;

    private final MessagingAdapter messagingAdapter;

    private final ListenerCollection listenerCollection;

    @Getter
    private boolean running = true;

    public ListenerThread(
            String queueName,
            MessagingAdapter messagingAdapter,
            List<EventListener> listeners
    ) {
        this.queueName = queueName;
        this.messagingAdapter = messagingAdapter;
        this.listenerCollection = new ListenerCollection(listeners);
    }


    @Override
    public void run() {
        while (running) {
            var message = messagingAdapter.recieve();
            if (message == null) {
                continue;
            }


            var event = message.getContent();
            listenerCollection.findAllByEventType(event.getClass())
                    .stream()
                    .filter(e -> message.getInvokeOn() == null || message.getInvokeOn().contains(e.getClass()))
                    .forEach(e -> {
                        try {
                            //noinspection unchecked
                            e.process(event);
                        } catch (Exception ex) {
                            log.error("An error ocurred during event handling "+event.toString(), ex);
                            messagingAdapter.sendToHospital(message.withInvokeOn(List.of(e.getClass())));
                        }
                    });
        }
        running = false;
    }

    @Override
    public void close() throws IOException {
        running = false;
    }
}
