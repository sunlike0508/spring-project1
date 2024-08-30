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

* `@ResponseBody` 를 사용
    * HTTP의 BODY에 문자 내용을 직접 반환
    * `viewResolver` 대신에 `HttpMessageConverter` 가 동작
    * 기본 문자처리: `StringHttpMessageConverter`
    * 기본 객체처리: `MappingJackson2HttpMessageConverter`
    * byte 처리 등등 기타 여러 HttpMessageConverter가 기본으로 등록되어 있음

```java
//org.springframework.http.converter.HttpMessageConverter
package org.springframework.http.converter;

public interface HttpMessageConverter<T> {

    boolean canRead(Class<?> clazz, @Nullable MediaType mediaType);

    boolean canWrite(Class<?> clazz, @Nullable MediaType mediaType);

    List<MediaType> getSupportedMediaTypes();

    T read(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException;

    void write(T t, @Nullable MediaType contentType, HttpOutputMessage outputMessage);
}
```

HTTP 메시지 컨버터는 HTTP 요청, HTTP 응답 둘 다 사용된다.

* `canRead()` , `canWrite()` : 메시지 컨버터가 해당 클래스, 미디어타입을 지원하는지 체크
* `read()` , `write()` : 메시지 컨버터를 통해서 메시지를 읽고 쓰는 기능


* 주요한 메시지 컨버터 (아래는 우선순위)
    * `ByteArrayHttpMessageConverter` : `byte[]` 데이터를 처리한다.
        * 클래스 타입: `byte[]` , 미디어타입: `*/*` ,
        * 요청 예) `@RequestBody byte[] data`
        * 응답 예) `@ResponseBody return byte[]` 쓰기 미디어타입 `application/octet-stream`

    * `StringHttpMessageConverter` : `String` 문자로 데이터를 처리한다.
        * 클래스 타입: `String` , 미디어타입: `*/*`
        * 요청 예) `@RequestBody String data`
        * 응답 예) `@ResponseBody return "ok"` 쓰기 미디어타입 `text/plain`

    * `MappingJackson2HttpMessageConverter` : application/json
        * 클래스 타입: 객체 또는 `HashMap` , 미디어타입 `application/json` 관련
        * 요청 예) `@RequestBody HelloData data`
        * 응답 예) `@ResponseBody return helloData` 쓰기 미디어타입 `application/json` 관련

**작동 순서 예**

* HTTP 요청 데이터 읽기

1) HTTP 요청이 오고, 컨트롤러에서 `@RequestBody` , `HttpEntity` 파라미터를 사용한다.
2) 메시지 컨버터가 메시지를 읽을 수 있는지 확인하기 위해 `canRead()` 를 호출한다.
    * 대상 클래스 타입을 지원하는가.
        * 예) `@RequestBody` 의 대상 클래스 ( `byte[]` , `String` , `HelloData` )
    * HTTP 요청의 Content-Type 미디어 타입을 지원하는가.
        * 예) `text/plain` , `application/json` , `*/*`
3) `canRead()` 조건을 만족하면 `read()` 를 호출해서 객체 생성하고, 반환한다.


* HTTP 응답 데이터 생성

1) 컨트롤러에서 `@ResponseBody` , `HttpEntity` 로 값이 반환된다.
2) 메시지 컨버터가 메시지를 쓸 수 있는지 확인하기 위해 `canWrite()` 를 호출한다.

    * 대상 클래스 타입을 지원하는가.
      예) return의 대상 클래스 (`byte[]` , `String` , `HelloData`)
    * HTTP 요청의 Accept 미디어 타입을 지원하는가.(더 정확히는 `@RequestMapping` 의 `produces`)   예) `text/plain` , `application/json`
      , `*/*`

3) `canWrite()` 조건을 만족하면 `write()` 를 호출해서 HTTP 응답 메시지 바디에 데이터를 생성한다.

## 요청 매핑 핸들러 어댑터 구조

위에서 메시지 컨버터는 어디에 있는 걸까? 일단 정답은 핸들러 어댑터에 있다.

<img width="917" alt="Screenshot 2024-08-30 at 00 46 58" src="https://github.com/user-attachments/assets/3373c544-9fc2-49ee-b99e-390f9d270966">

### ArgumentResolver

애노테이션 기반의 컨트롤러는 매우 다양한 파라미터를 사용할 수 있다.

httpservletrequest, model, @Requestparam, @ModelAttribute, @ReuqestBody, HttpEntity 등.

RequestMappingHandlerAdapter는 ArgumentResolver를 통해 컨트롤러가 필요로 하는 다양한 파라미터의 값(객체)를 생성해준다. (이때가 바로 메시지 컨버터가 작동한다.
ArgumentResolver 안에 있다기 보다는 그 옆에서 도와주는 역할)

반대로 응답에는 ReturnValueHandler(<->ArgumentResolver)가 있다.



