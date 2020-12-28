package pl.jakubtrzcinski.priap;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
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
    private final AmqpMessagingAdapter messagingAdapter;


    public static PriapSession createForAmqp(
            String appName,
            int threadsCount,
            RabbitTemplate amqpTemplate,
            RabbitAdmin rabbitAdmin,
            List<EventListener> listeners
    ) {
        String exchangeName = "exchange";

        var messagingAdapter = new AmqpMessagingAdapter(
                "priap-"+exchangeName,
                "priap-"+appName,
                amqpTemplate,
                rabbitAdmin
        );
        var listenerSupervisor = new ListenerSupervisor(
                appName,
                threadsCount,
                messagingAdapter,
                listeners
        );
        return new PriapSession(
                appName,
                listenerSupervisor,
                messagingAdapter
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
        messagingAdapter.send(new PriapMessage(
                appName,
                event
        ));
    }

    public void rerunFromHospital() {
        messagingAdapter.runFromHospital();
    }
}
