package pl.jakubtrzcinski;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.jakubtrzcinski.priap.PriapSession;

import static java.util.Collections.EMPTY_LIST;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 27-12-2020
 */
@EnableRabbit
@Configuration
class PriapConfiguration {
    @Bean
    public PriapSession priapSession(RabbitTemplate amqpTemplate){
        var priapSession = PriapSession.create(
                "test",
                1,
                amqpTemplate,
                EMPTY_LIST
        );
        priapSession.run();
        return priapSession;
    }
}
