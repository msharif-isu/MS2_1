package harmonize.Controllers;

import org.springframework.web.bind.annotation.*;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome to Harmonize!";
    }
}
