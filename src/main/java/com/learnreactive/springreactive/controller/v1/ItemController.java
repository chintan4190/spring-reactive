package com.learnreactive.springreactive.controller.v1;

import com.learnreactive.springreactive.document.Item;
import com.learnreactive.springreactive.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.learnreactive.springreactive.constant.ItemConstant.ITEM_ENDPOINT;

@RestController
@Slf4j
public class ItemController {

    @Autowired
    private ItemReactiveRepository itemReactiveRepository;

    @GetMapping(ITEM_ENDPOINT)
    public Flux<Item> getAllItems() {
        return itemReactiveRepository.findAll();
    }
    @GetMapping(ITEM_ENDPOINT+"/{id}")
    public Mono<ResponseEntity<Item>> getOneItem(@PathVariable String id){
        return itemReactiveRepository.findById(id)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(ITEM_ENDPOINT)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> createItem(@RequestBody Item item){
        return itemReactiveRepository.save(item);
    }

    @DeleteMapping(ITEM_ENDPOINT+"/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> deleteItem(@PathVariable String id){
        return itemReactiveRepository.deleteById(id);
    }

}
