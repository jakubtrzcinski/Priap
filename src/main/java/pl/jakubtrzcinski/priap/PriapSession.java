package pl.jakubtrzcinski.priap;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import pl.jakubtrzcinski.priap.api.EventDispatcher;
import pl.jakubtrzcinski.priap.api.EventListener;
import pl.jakubtrzcinski.priap.dto.PriapMessage;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 27-12-2020
 */
@RequiredArgsConstructor
public class PriapSession implements Closeable, Runnable, EventDispatcher {

    private final String appName;

    private final ListenerSupervisor listenerSupervisor;

    private final Consumer<PriapMessage> messageConsumer;

    public static PriapSession create(
            String appName,
            int threadsCount,
            RabbitTemplate amqpTemplate,
            List<EventListener> listeners
    ) {
        String exchangeName = "exchange";
        var mapper = new BinaryPriapMessageMapper();
        var listenerSupervisor = new ListenerSupervisor(
                threadsCount,
                new PriapRabbitMQMessageSupplier(exchangeName, "priap-" + appName, amqpTemplate, mapper),
                new PriapMessageExecutor(
                        new BinaryPriapMessageMapper(),
                        new ListenerCollection(listeners)
                ),
                new LoggingEventErrorHandler()
        );
        return new PriapSession(
                appName,
                listenerSupervisor,
                new PriapRabbitMQMessageConsumer(
                        appName,
                        "priap-" + exchangeName,
                        amqpTemplate,
                        mapper
                )
        );
    }

    @Override
    public void run() {
        listenerSupervisor.run();
    }

    @Override
    public void close() throws IOException {
        listenerSupervisor.close();
    }


    @Override
    public void dispatch(Serializable event) {
        messageConsumer.accept(new PriapMessage(
                appName,
                event
        ));
    }
}
