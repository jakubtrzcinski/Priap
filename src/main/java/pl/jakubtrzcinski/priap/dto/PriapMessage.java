package pl.jakubtrzcinski.priap.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.With;

import java.io.Serializable;
import java.util.List;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 27-12-2020
 */
@Getter
@RequiredArgsConstructor
public class PriapMessage implements Serializable {

    private final String origin;

    private final Serializable content;

    @With
    private final List<Class> invokeOn;

    public PriapMessage(String origin, Serializable content) {
        this.origin = origin;
        this.content = content;
        this.invokeOn = null;
    }
}
