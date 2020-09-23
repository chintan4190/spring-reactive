package com.learnreactive.springreactive.playground;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public class FlatMapTest {
    List<String> names = Stream.of("amsterdam", "dubai", "venice", "surat").collect(Collectors.toList());

    @Test
    void test() {
        User user1 = new User("chintan");
        User user2 = new User("doshi");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        List<List<User>> listList = new ArrayList<>();
        listList.add(userList);

        List<String> collect = userList.stream()
                .map(u -> u.name.toUpperCase())
                .collect(Collectors.toList());

        System.out.println(collect);

        listList.stream()
                .flatMap( u -> u.stream())
                .collect(Collectors.toList());
    }


    class User {
        User(String name) {
            this.name = name;
        }

        String name;
    }

}

