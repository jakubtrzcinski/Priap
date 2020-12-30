# Priap

Simple distributed event dispatcher with persistent hospital for failed events execution based on [Spring AMQP](https://github.com/spring-projects/spring-amqp)

## How it works

TODO

## Requirements
[Spring AMQP](https://github.com/spring-projects/spring-amqp)

[Running RabbitMQ Instance](https://www.rabbitmq.com/)

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
spring.rabbitmq.username=rabbit
spring.rabbitmq.password=rabbit
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5671
```
## Spring Example

#### Event 
_NOTE:_ All events has to be `Serializable`!
```java
@Getter
@RequiredArgsConstructor
class ExampleEvent implements Serializable {
    private final String text;
}
```

#### Listener
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

#### Event dispatching
```java
import lombok.RequiredArgsConstructor;
import pl.jakubtrzcinski.priap.api.EventDispatcher;

@RequiredArgsConstructor
class FooService  {

    private final EventDispatcher eventDispatcher;
    
    public void foo() {
        eventDispatcher.dispatch(new ExampleEvent("Hello World"));
    }
}
```

#### Reruning failed events from hospital

*NOTE:* _All events from hospital are reruned after Piriap Session is created_

```java
import lombok.RequiredArgsConstructor;
import pl.jakubtrzcinski.priap.PriapSession;

@RequiredArgsConstructor
class FooService  {

    private final PriapSession session;

    public void foo() {
        session.rerunFromHospital();
    }
}
```


