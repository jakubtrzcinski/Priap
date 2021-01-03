package pl.jakubtrzcinski.priap;

import lombok.RequiredArgsConstructor;
import pl.jakubtrzcinski.priap.api.EventListener;

import java.io.Closeable;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 27-12-2020
 */
@RequiredArgsConstructor
class ListenerSupervisor implements Closeable, Runnable {

    private final String appName;

    private final int threadsCount;

    private final MessagingAdapter messagingAdapter;

    private final List<EventListener> listeners;

    private final List<ListenerThread> threads = new LinkedList<>();


    @Override
    public void run() {
        messagingAdapter.initExchange();
        messagingAdapter.declareQueueWithHospitalAndBindToExchange();
        if(!threads.isEmpty()){
            return;
        }
        for (int i = 0; i < threadsCount; i++) {
            var thread = new ListenerThread(
                    appName,
                    messagingAdapter,
                    listeners
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
