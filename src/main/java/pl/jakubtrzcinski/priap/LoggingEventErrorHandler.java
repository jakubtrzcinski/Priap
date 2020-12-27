package pl.jakubtrzcinski.priap;

import lombok.extern.slf4j.Slf4j;
import pl.jakubtrzcinski.priap.api.EventErrorHandler;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 27-12-2020
 */
@Slf4j
class LoggingEventErrorHandler implements EventErrorHandler {
    @Override
    public void handle(Exception ex) {
        log.error("Error occured during event handling", ex);
    }
}
