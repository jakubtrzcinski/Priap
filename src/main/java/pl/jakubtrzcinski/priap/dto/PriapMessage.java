package pl.jakubtrzcinski.priap.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 27-12-2020
 */
@Getter
@RequiredArgsConstructor
public class PriapMessage implements Serializable {

    private final String origin;

    private final Object content;

}
