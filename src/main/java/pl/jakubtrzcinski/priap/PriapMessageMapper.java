package pl.jakubtrzcinski.priap;

import pl.jakubtrzcinski.priap.dto.PriapMessage;

import java.io.Serializable;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 27-12-2020
 */
public interface PriapMessageMapper {
    byte[] map(Serializable message);
    Serializable map(byte[] message);
}
