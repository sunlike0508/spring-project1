package hello.springmvc.basic;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
//@Controller
@RestController
public class LongTestController {

    //private final Logger log = LoggerFactory.getLogger(getClass());


    @RequestMapping("/log-test")
    public String logTest() {
        String name = "Spring";

        System.out.println("name=" + name);

        log.trace("trace log={}", name);
        log.debug("debug log={}", name);
        log.info("info log={} {}", name, "info");
        log.warn("warn log={}", name);
        log.error("info log={}", name);


        return "ok";
    }
}
