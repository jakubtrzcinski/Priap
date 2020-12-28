package pl.jakubtrzcinski.priap;

import org.springframework.lang.Nullable;
import pl.jakubtrzcinski.priap.dto.PriapMessage;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 28-12-2020
 */
public interface MessagingAdapter {

    void initExchange();

    void declareQueueWithHospitalAndBindToExchange();

    @Nullable
    PriapMessage recieve();

    void send(PriapMessage message);

    void sendToHospital(PriapMessage message);

    void runFromHospital();
}
