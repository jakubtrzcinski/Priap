package pl.jakubtrzcinski.priap;

import lombok.RequiredArgsConstructor;
import pl.jakubtrzcinski.priap.dto.PriapMessage;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 27-12-2020
 */
@RequiredArgsConstructor
class PriapMessageExecutor {

    private final PriapMessageMapper mapper;

    private final ListenerCollection listenerCollection;

    void execute(PriapMessage message) throws Exception {

        var object = mapper.map(message);

        var listeners = listenerCollection.findAllByEventType(object.getClass());

        //noinspection unchecked
        listeners.forEach(e->e.process(object));
    }

}
