package pl.jakubtrzcinski.priap;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.lang.Nullable;
import pl.jakubtrzcinski.priap.dto.PriapMessage;

import java.util.Map;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 28-12-2020
 */
@RequiredArgsConstructor
class AmqpMessagingAdapter implements MessagingAdapter {

    private final String exchangeName;

    private final String queueName;

    private final RabbitTemplate template;

    private final RabbitAdmin admin;

    private int toFetchFromHospital = 0;


    @Override
    public void initExchange() {
        admin.declareExchange(new FanoutExchange(exchangeName, true, false));
    }


    @Override
    public void declareQueueWithHospitalAndBindToExchange() {
        admin.declareQueue(new Queue(queueName +"-hospital", true, false, false));
        admin.declareQueue(new Queue(queueName, true, false, false));
        admin.declareBinding(new Binding(queueName, Binding.DestinationType.QUEUE, exchangeName, "", Map.of()));
        runFromHospital();
    }

    @Override
    @Nullable
    public PriapMessage recieve() {
        Message amqp;
        if (toFetchFromHospital == 0){
            amqp = template.receive(queueName);
        }
        else {
            amqp = template.receive(queueName + "-hospital");
            toFetchFromHospital--;
        }
        if(amqp == null){
            return null;
        }
        return map(amqp.getBody());
    }

    @Override
    public void send(PriapMessage message) {
        template.send(exchangeName, "",  new Message(map(message), new MessageProperties()));
    }

    @Override
    public void sendToHospital(PriapMessage message) {
        template.send("", queueName +"-hospital",  new Message(map(message), new MessageProperties()));
    }

    @Override
    public void runFromHospital() {
        var queueDetails = admin.getQueueInfo(queueName +"-hospital");
        if(queueDetails == null){
            return;
        }
        toFetchFromHospital = queueDetails.getMessageCount();
    }

    private byte[] map(PriapMessage message) {
        return SerializationUtils.serialize(message);
    }

    private PriapMessage map(byte[] message) {
        return (PriapMessage)SerializationUtils.deserialize(message);
    }
}
