package nuzlocke.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestTestController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
