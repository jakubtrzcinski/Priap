package pl.jakubtrzcinski.priap.spring;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.jakubtrzcinski.priap.PriapSession;
import pl.jakubtrzcinski.priap.api.EventListener;

import java.util.List;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 27-12-2020
 */
@Configuration
class PriapConfiguration {

    @Bean
    public PriapSession priapSession(
            @Value("${spring.application.name:none}") String appName,
            @Value("${priap.threads:10}") int threads,
            RabbitTemplate amqpTemplate,
            List<EventListener> listeners
    ){
        var priapSession = PriapSession.createForAmqp(
                appName,
                threads,
                amqpTemplate,
                new RabbitAdmin(amqpTemplate),
                listeners
        );
        priapSession.run();
        return priapSession;
    }
}
