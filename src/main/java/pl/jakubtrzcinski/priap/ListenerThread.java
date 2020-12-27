package pl.jakubtrzcinski.priap;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.jakubtrzcinski.priap.api.EventErrorHandler;
import pl.jakubtrzcinski.priap.dto.PriapMessage;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Supplier;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 27-12-2020
 */
@RequiredArgsConstructor
class ListenerThread extends Thread implements Closeable {

    private final Supplier<PriapMessage> messageSupplier;

    private final PriapMessageExecutor executor;

    private final EventErrorHandler eventErrorHandler;

    @Getter
    private boolean running = true;

    @Override
    public void run() {
        while (running) {
            try {
                var message = messageSupplier.get();
                if (message != null) {
                    executor.execute(message);
                }
            } catch (Exception e) {
                eventErrorHandler.handle(e);
            }
        }
        running = false;
    }

    @Override
    public void close() throws IOException {
        running = false;
    }
}
