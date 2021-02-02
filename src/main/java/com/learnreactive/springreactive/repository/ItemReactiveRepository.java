package com.learnreactive.springreactive.repository;

import com.learnreactive.springreactive.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {

    Flux<Item> findByDescription(String desc);
}
