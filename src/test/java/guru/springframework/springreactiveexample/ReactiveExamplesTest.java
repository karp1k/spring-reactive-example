package guru.springframework.springreactiveexample;

import guru.springframework.springreactiveexample.domain.Person;
import guru.springframework.springreactiveexample.domain.PersonDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author kas
 */
@Slf4j
public class ReactiveExamplesTest {

    Person michael = new Person("Michael", "Schwartz");
    Person alice = new Person("Alice", "Fulton");
    Person bob = new Person("Bob", "Murry");
    Person katie = new Person("Katie", "Blazkowicz");

    @Test
    void monoTest() {
        Mono<Person> personMono = Mono.just(michael);
        Person p = personMono.block();
        log.info(p.getFullName());
    }

    @Test
    void monoTransform() {
        Mono<Person> personMono = Mono.just(alice);
        PersonDto dto = personMono.map(PersonDto::new).block();
        log.info(dto.getFullName());
    }

    @Test
    void monoFilter() {
        assertThrows(NullPointerException.class, () -> {
           Mono<Person> personMono = Mono.just(bob);
           Person p = personMono.filter(person -> person.getFirstName().equals("foo")).block();
           log.info(p.getFullName());
        });
    }

    @Test
    void fluxTest() {
        Flux<Person> flux = Flux.just(michael, alice, bob, katie);
        flux.subscribe(person -> log.info(person.getFullName()));
    }

    @Test
    void fluxTestFilter() {
        Flux<Person> flux = Flux.just(michael, alice, bob, katie);
        flux.filter(person -> person.getFirstName().equals(katie.getFirstName()))
                .subscribe(person -> log.info(person.getFullName()));
    }

    @Test
    void fluxTestDelayNoOutput() {
        Flux<Person> flux = Flux.just(michael, alice, bob, katie);
        flux.delayElements(Duration.ofSeconds(1)).subscribe(person -> log.info(person.getFullName()));
    }

    @Test
    void fluxTestDelay() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Flux<Person> flux = Flux.just(michael, alice, bob, katie);
        flux.delayElements(Duration.ofSeconds(1)).doOnComplete(countDownLatch::countDown).subscribe(person -> log.info(person.getFullName()));
        countDownLatch.await();
    }

    @Test
    void fluxTestFilterDelay() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Flux<Person> flux = Flux.just(michael, alice, bob, katie);
        flux
                .filter(p -> p.getFirstName().contains("ic"))
                .delayElements(Duration.ofSeconds(1))
                .doOnComplete(countDownLatch::countDown)
                .subscribe(person -> log.info(person.getFullName()));
        countDownLatch.await();
    }
}
