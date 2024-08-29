package hello.springmvc.basic.response;


import java.io.IOException;
import hello.springmvc.basic.HelloData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResponseBodyController {

    @GetMapping("/response-body-string-v1")
    public void responseViewV1(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.getWriter().write("ok");
    }


    @GetMapping("/response-body-string-v2")
    public ResponseEntity<String> responseViewV2() {
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }


    @GetMapping("/response-body-string-v3")
    public String responseViewV3() {
        return "ok";
    }


    @GetMapping("/response-body-json-v1")
    public ResponseEntity<HelloData> responseViewV4() {

        HelloData helloData = new HelloData("shin", 10);

        return new ResponseEntity<>(helloData, HttpStatus.OK);
    }


    @GetMapping("/response-body-json-v2")
    public HelloData responseViewV5() {

        HelloData helloData = new HelloData("shin", 10);

        return helloData;
    }
}
