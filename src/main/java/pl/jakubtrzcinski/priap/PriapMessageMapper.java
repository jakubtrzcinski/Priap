package pl.jakubtrzcinski.priap;

import pl.jakubtrzcinski.priap.dto.PriapMessage;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 27-12-2020
 */
public interface PriapMessageMapper {
    byte[] map(Object message);
    Object map(byte[] message);
}
