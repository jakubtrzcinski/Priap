package pl.jakubtrzcinski.priap;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.lang.Nullable;
import pl.jakubtrzcinski.priap.dto.PriapMessage;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 27-12-2020
 */
class PriapRabbitMQMessageSupplier implements Supplier<PriapMessage> {

    private final String exchange;

    private final String queue;

    private final RabbitTemplate amqpTemplate;

    private final PriapMessageMapper mapper;

    public PriapRabbitMQMessageSupplier(String exchange, String queue, RabbitTemplate amqpTemplate, PriapMessageMapper mapper) {
        this.exchange = exchange;
        this.queue = queue;
        this.amqpTemplate = amqpTemplate;
        this.mapper = mapper;
        init();
    }

    private void init(){
        var admin = new RabbitAdmin(amqpTemplate);
        admin.declareExchange(new FanoutExchange(exchange, true, false));
        admin.declareQueue(new Queue(queue, true, false, false));
        admin.declareQueue(new Queue(queue+"-hospital", true, false, false));
        admin.declareBinding(new Binding(queue, Binding.DestinationType.QUEUE, exchange, "", Map.of()));
    }

    @SneakyThrows
    @Override
    @Nullable
    public PriapMessage get() {
        var amqpMessage = amqpTemplate.receive(queue);
        if(amqpMessage == null){
            return null;
        }
        var properties = amqpMessage.getMessageProperties();
        return new PriapMessage(
                properties.getHeader("origin"),
                mapper.map(amqpMessage.getBody())
        );
    }
}
