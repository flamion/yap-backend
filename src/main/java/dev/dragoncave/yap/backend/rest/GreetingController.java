package dev.dragoncave.yap.backend.rest;

import java.util.concurrent.atomic.AtomicLong;

import dev.dragoncave.yap.backend.rest.objects.Greeting;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @GetMapping("/greeting/{id}")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name, @PathVariable Long id) {
        return new Greeting(id, String.format(template, name));
    }
}
