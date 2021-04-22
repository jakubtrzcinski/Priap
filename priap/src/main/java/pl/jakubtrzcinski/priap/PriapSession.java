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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 27-12-2020
 */
@RequiredArgsConstructor
public class PriapSession implements Closeable, Runnable, EventDispatcher {

    private final Map<Class, SimplePriapSession> sessions;

    public static PriapSession createForAmqp(
            String appName,
            int threadsCount,
            RabbitTemplate amqpTemplate,
            RabbitAdmin rabbitAdmin,
            List<EventListener> listeners
    ) {
        Map<Class, SimplePriapSession> sessions = new HashMap<>();
        listeners.forEach(it->{
            var eventClazz = GenericsUtils.getGeneric(it);
            var session = SimplePriapSession.createForAmqp(
                    appName+"-"+eventClazz.getSimpleName(),
                    threadsCount,
                    amqpTemplate,
                    rabbitAdmin,
                    List.of(it)
            );
            sessions.put(eventClazz, session);
        });
        return new PriapSession(
                sessions
        );
    }

    @Override
    public void run() {
        sessions.values().forEach(SimplePriapSession::run);
    }

    @Override
    public void close() throws IOException {
        for (SimplePriapSession simplePriapSession : sessions.values()) {
            simplePriapSession.close();
        }
    }


    @Override
    public void dispatch(Serializable event) {
        sessions.get(event.getClass()).dispatch(event);
    }

    public void rerunFromHospital() {
        sessions.values().forEach(SimplePriapSession::rerunFromHospital);
    }
}
