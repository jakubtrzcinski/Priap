package pl.jakubtrzcinski.priap;

import lombok.SneakyThrows;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.lang.Nullable;
import pl.jakubtrzcinski.priap.dto.PriapMessage;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 27-12-2020
 */
class PriapRabbitMQMessageConsumer implements Consumer<PriapMessage> {

    private final String appName;

    private final String exchange;

    private final AmqpTemplate amqpTemplate;

    private final PriapMessageMapper mapper;

    public PriapRabbitMQMessageConsumer(
            String appName,
            String exchange,
            AmqpTemplate amqpTemplate,
            PriapMessageMapper mapper
    ) {
        this.appName = appName;
        this.exchange = exchange;
        this.amqpTemplate = amqpTemplate;
        this.mapper = mapper;
    }




    @Override
    public void accept(PriapMessage message) {
        var properties = new MessageProperties();
        properties.setHeader("origin", appName);
        amqpTemplate.send(
                exchange,
                new Message(mapper.map(message), properties)
        );
    }
}
