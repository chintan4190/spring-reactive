package com.learnreactive.springreactive.repository;

import com.learnreactive.springreactive.document.Item;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@DirtiesContext
class ItemReactiveRepositoryTest {

    @Autowired
    private ItemReactiveRepository itemReactiveRepository;

    List<Item> itemList = Arrays.asList(new Item(null, "iPhone 11", new Double(1100)),
            new Item(null, "iPhone 10", new Double(100)),
            new Item("1", "iPhone 6s", new Double(500)),
            new Item(null, "iPhone 12", new Double(1400)));

    @BeforeEach
    public void setUp() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(itemList))
                .flatMap(i -> itemReactiveRepository.save(i))
                .doOnNext(i -> System.out.println("inserted item " + i))
                .blockLast();

    }

    @Test
    void selectAllItems() {
        Flux<Item> all = itemReactiveRepository.findAll();

        StepVerifier.create(all)
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    void getItemById() {
        Mono<Item> byId = itemReactiveRepository.findById("1");
        StepVerifier.create(byId)
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findItemByDesc() {
        StepVerifier.create(itemReactiveRepository.findByDescription("iPhone 12").log())
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void saveItem() {
        Item item = new Item("DEF", "mouse", new Double(100));
        Mono<Item> itemMono = itemReactiveRepository.save(item);
    }

    @Test
    void updateItem() {
        Flux<Item> byDescription = itemReactiveRepository.findByDescription("iPhone 12")
                .map(item -> {
                    item.setPrice(99999.99);
                    return item;
                })
                .flatMap(item -> itemReactiveRepository.save(item));
        StepVerifier.create(byDescription)
                .expectSubscription()
                .expectNextMatches(item -> item.getPrice()==99999.99)
                .verifyComplete();
    }
}