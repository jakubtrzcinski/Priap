package pl.jakubtrzcinski.priap;

import lombok.RequiredArgsConstructor;
import pl.jakubtrzcinski.priap.api.EventErrorHandler;
import pl.jakubtrzcinski.priap.dto.PriapMessage;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Supplier;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 27-12-2020
 */
@RequiredArgsConstructor
class ListenerSupervisor implements Closeable, Runnable {

    private final int threadsCount;

    private final Supplier<PriapMessage> messageSupplier;

    private final PriapMessageExecutor executor;

    private final EventErrorHandler errorHandler;

    private final List<ListenerThread> threads = new LinkedList<>();


    @Override
    public void run() {
        if(!threads.isEmpty()){
            return;
        }
        for (int i = 0; i < threadsCount; i++) {
            var thread = new ListenerThread(
                    messageSupplier,
                    executor,
                    errorHandler
            );
            thread.setName("priap-listener-"+i);
            thread.start();
            threads.add(thread);
        }
    }

    @Override
    public void close() throws IOException {
        for (ListenerThread thread : threads) {
            try {
                thread.close();
            } catch (Exception e){
                //should never happen, just to make compiler happy
            }
        }
    }


}
