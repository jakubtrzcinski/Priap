package pl.jakubtrzcinski.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.jakubtrzcinski.priap.api.EventListener;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 28-12-2020
 */
@Slf4j
@Component
public class ExampleEventListener implements EventListener<ExampleEvent> {

    @Override
    public void process(ExampleEvent event) {
        log.error(event.getText());
    }
}
