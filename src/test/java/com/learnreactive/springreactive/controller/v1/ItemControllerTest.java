package com.learnreactive.springreactive.controller.v1;

import com.learnreactive.springreactive.constant.ItemConstant;
import com.learnreactive.springreactive.document.Item;
import com.learnreactive.springreactive.repository.ItemReactiveRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext //to handle embedded database
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ItemControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @Before
    public void setUp(){
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(item -> {
                    System.out.println("Inserted items: "+item);
                })
                .blockLast(); //to wait for setUp() method to finish
    }

    public List<Item> data(){
        return Arrays.asList(new Item(null, "iPhone 11", new Double(1100)),
                new Item(null, "iPhone 10", new Double(100)),
                new Item("1", "iPhone 6s", new Double(500)),
                new Item(null, "iPhone 12", new Double(1400)));
    }

    @Test
    public void getAllItems(){
        webTestClient
                .get()
                .uri(ItemConstant.ITEM_ENDPOINT)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Item.class)
                .hasSize(4);
    }

    @Test
    public void getAllItems_approach2(){
        webTestClient
                .get()
                .uri(ItemConstant.ITEM_ENDPOINT)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Item.class)
                .hasSize(4)
        .consumeWith((response) -> {
            List<Item> items = response.getResponseBody();
            items.forEach(item -> {
                assertNotNull(item.getId());
            });
        });
    }

    @Test
    public void getAllItems_approach3(){
        Flux<Item> itemFlux = webTestClient
                .get()
                .uri(ItemConstant.ITEM_ENDPOINT)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .returnResult(Item.class)
                .getResponseBody();
        StepVerifier.create(itemFlux)
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void getOneItem(){
        webTestClient
                .get()
                .uri(ItemConstant.ITEM_ENDPOINT.concat("/{id}"), "1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.price",500);
    }

    @Test
    public void getOneItem_notfound(){
        webTestClient
                .get()
                .uri(ItemConstant.ITEM_ENDPOINT.concat("/{id}"), "xxx")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void createItem(){
        Item item = new Item(null, "OnePlus", new Double(670));
        webTestClient
                .post()
                .uri(ItemConstant.ITEM_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.price",670);
    }

    @Test
    public void deleteItem(){
        webTestClient
                .delete()
                .uri(ItemConstant.ITEM_ENDPOINT.concat("/{id}"), "1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);

        webTestClient
                .get()
                .uri(ItemConstant.ITEM_ENDPOINT)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Item.class)
                .hasSize(3);
    }


}