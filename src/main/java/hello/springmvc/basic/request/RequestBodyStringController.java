package hello.springmvc.basic.request;


import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class RequestBodyStringController {

    @PostMapping("/request-body-string-v1")
    public void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream servletInputStream = request.getInputStream();

        String messageBody = StreamUtils.copyToString(servletInputStream, StandardCharsets.UTF_8);

        log.info("messageBody = {}", messageBody);

        response.getWriter().write("ok");
    }


    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer writer) throws IOException {

        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody = {}", messageBody);

        writer.write("ok");
    }


    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) {

        log.info("messageBody = {}", httpEntity.getBody());

        return new HttpEntity<>("ok");
    }


    @PostMapping("/request-body-string-v3-1")
    public ResponseEntity<String> requestBodyStringV4(RequestEntity<String> httpEntity) {

        log.info("messageBody = {}", httpEntity.getBody());

        return new ResponseEntity<>("ok", HttpStatus.OK);
    }


    @PostMapping("/request-body-string-v4")
    public @ResponseBody String requestBodyStringV3(@RequestBody String messageBody) {

        log.info("messageBody = {}", messageBody);

        return "ok";
    }
}
