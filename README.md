# MVC 프로젝트

## 로깅

SLF4J : 라이브러리. 인터페이스로 제공.

Logback : SLF4J 구현체

@Controller: view 이름을 가지고 뷰라 렌더링 된다.

@RestController : HTTP 메시지 바디에 바로 입력

* 로그 레벨
    1) trace
    2) debug
    3) info
    4) warn
    5) error

## HTTP 요청, 응답

HttpEntity 기본. 요청, 응답 모두에 사용. view 사용 X

RequestEntity, ResponseEntity은 HttpEntity을 상속받아 각각 요청과 응답에 사용.

* RequestEntity
    * HttpMethod, url 정보가 추가, 요청에서 사용

* ResponseEntity
    * HTTP 상태 코드 설정 가능, 응답에서 사용
    * ex) `return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED)`

이거를 한 단계 더 업그레이드

@RequestBody, @ResponseBody

## HTTP 메시지 컨버터

뷰 템플릿으로 HTML을 생성해서 응답하는 것이 아니라 HTTP API처럼 json 데이터를 http 메시지 바디에서 직접 읽거나 쓰는 경우 http 메시지 컨버터를 사용하면 편리하다.


<img width="938" alt="Screenshot 2024-08-30 at 00 31 03" src="https://github.com/user-attachments/assets/15b2b908-4026-4e3b-a8a5-ec3f8559682e">



