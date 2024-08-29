package hello.springmvc.basic.request;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.springmvc.basic.HelloData;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class RequestBodyJsonController {

    private final ObjectMapper objectMapper = new ObjectMapper();


    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream servletInputStream = request.getInputStream();

        String messageBody = StreamUtils.copyToString(servletInputStream, StandardCharsets.UTF_8);

        log.info("messageBody = {}", messageBody);

        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);

        log.info("helloData={}", helloData);

        response.getWriter().write("ok");
    }


    @PostMapping("/request-body-json-v2")
    public @ResponseBody String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {

        log.info("messageBody = {}", messageBody);

        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);

        log.info("helloData={}", helloData);

        return "ok";
    }


    @PostMapping("/request-body-json-v3")
    public @ResponseBody String requestBodyJsonV3(@RequestBody HelloData helloData) {

        log.info("helloData={}", helloData);

        return "ok";
    }


    @PostMapping("/request-body-json-v4")
    public @ResponseBody String requestBodyJsonV4(HttpEntity<HelloData> httpEntity) {

        HelloData helloData = httpEntity.getBody();

        log.info("helloData={}", helloData);

        return "ok";
    }


    @PostMapping("/request-body-json-v5")
    public @ResponseBody HelloData requestBodyJsonV5(HttpEntity<HelloData> httpEntity) {

        HelloData helloData = httpEntity.getBody();

        log.info("helloData={}", helloData);

        return helloData;
    }

}
