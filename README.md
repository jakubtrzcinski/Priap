# Priap

Simple distributed event dispatcher with persistent hospital for failed events execution based on [Spring AMQP](https://github.com/spring-projects/spring-amqp)

## How it works

TODO

## Requirements
[Spring AMQP](https://github.com/spring-projects/spring-amqp)

## Instalation

### Maven 
```xml
<dependencies>
    <dependency>
        <groupId>pl.jakubtrzcinski</groupId>
        <artifactId>priap</artifactId>
        <version>1.0.1.RELEASE</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.amqp</groupId>
        <artifactId>spring-amqp</artifactId>
        <version>2.3.2</version>
    </dependency>
</dependencies>
```

### Gradle
```groovy
dependencies {
    compile 'pl.jakubtrzcinski:priap:1.0.1.RELEASE'
    compile 'org.springframework.amqp:spring-amqp:2.3.2'
}
```

## Setting up

### Annotation

```java
@EnablePriap
@SpringBootApplication
class ExampleApplication {
    //[...]
}
```

### Programaticly

```java
 var priapSession = PriapSession.createForAmqp(
        "application",
        10, // Define how much listener threads should be created
        rabbitTemplate,
        rabbitTemplate,
        listeners // List<EventListener>
);
priapSession.run();
return priapSession;
```
## Configuration

```properties
priap.threads=10 #Define how much listener threads should be created
```
## Spring Example

#### Example event 
_NOTE:_ All events has to be `Serializable`!
```java
@Getter
@RequiredArgsConstructor
class ExampleEvent implements Serializable {
    private final String text;
}
```

#### Example listener
```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.jakubtrzcinski.priap.api.EventListener;

@Slf4j
@Component
public class ExampleEventListener implements EventListener<ExampleEvent> {

    @Override
    public void process(ExampleEvent event) {
        log.error(event.getText());
    }
}
```
