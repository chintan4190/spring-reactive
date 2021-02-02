package com.learnreactive.springreactive.initialize;

import com.learnreactive.springreactive.document.Item;
import com.learnreactive.springreactive.repository.ItemReactiveRepository;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Profile("!test")
public class ItemDataInitializer implements CommandLineRunner {
    @Autowired
    private ItemReactiveRepository itemReactiveRepository;

    @Override
    public void run(String... args) throws Exception {
        initialDataSetup();
    }

    private void initialDataSetup() {
        List<Item> itemList = Arrays.asList(new Item(null, "iPhone 11", new Double(1100)),
                new Item(null, "iPhone 10", new Double(100)),
                new Item("1", "iPhone 6s", new Double(500)),
                new Item(null, "iPhone 12", new Double(1400)));
        itemReactiveRepository.deleteAll().thenMany(Flux.fromIterable(itemList))
                .flatMap(item -> itemReactiveRepository.save(item))
                .thenMany(itemReactiveRepository.findAll())
                .subscribe( (item -> {
                    System.out.println("item inserted from cmd runner...:"+item);
                }));
    }
}
