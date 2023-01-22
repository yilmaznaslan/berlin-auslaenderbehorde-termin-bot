package org.example.auslanderbehorde.formfiller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class Controller {
    private static final String template = "Hello, %s!. Counter:%s";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/search")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format(template, name, counter.incrementAndGet()
        );
    }

}
