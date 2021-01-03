package pl.jakubtrzcinski.app;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 28-12-2020
 */
@Getter
@RequiredArgsConstructor
class ExampleEvent implements Serializable {
    private final String text;
}
