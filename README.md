# 온라인 자전거 판매점 (Road Bike Online-Center)

![image](https://user-images.githubusercontent.com/89397401/132269421-93061e34-7f56-4170-9377-290f883d93e5.png)

 # 서비스 시나리오

기능적 요구사항
 1. 관리자는 자전거 재고정보를 추가, 수정, 삭제 한다.
 2. 구매자는 자전거 홈페이지를 통해 사전 예약을 한다.
 3. 구매자는 예약 이후 결제를 진행해야 한다.
 4. 결제가 완료된 정보에 대해서 배송이 이루어진다.
 5. 구매자는 자전거 구매 예약을 취소 할 수 있다.
 6. 예약이 취소가 되면 결제가 취소된다.
 7. 결제가 취소되면 배송도 취소되어진다.
 8. 구매자는 자전거 구매 진행정보에 대해 확인 할 수 있다.

비기능적 요구사항
1. 트랜잭션
    1. 결제가 이루어지지 않으면 예약을 등록 할 수 없다. (Sync)
2. 장애격리
    1. 재고관리 기능이 수행되지 않더라도 예약은 24시간 동안 받을 수 있어야 한다. Async (event-driven), Eventual Consistency
    2. 예약시스템이 과중되면 잠시동안 예약 되지 않고 잠시 후에 하도록 유도한다. Circuit breaker, fallback
3. 성능
    1. 구매자는 모든 구매 진행정보를 확인 할 수 있어야 한다.(CQRS)

# 분석/설계

## Event Storming 결과
* MSAEz 로 모델링한 이벤트스토밍 결과: http://labs.msaez.io/#/storming/RlIcWPwK7EWnLeZIefkRchpnt272/c812c1241e39c7e19f3cd98f88409e6b

### 이벤트 도출
작성예정

### 부적격 이벤트 탈락
작성예정

### 완성된 1차 모형
![image](https://user-images.githubusercontent.com/89397401/132272997-acadd044-2756-4557-9b86-239f1d2e66fe.png)

### 완성된 최종 모형
![image](https://user-images.githubusercontent.com/89397401/132273023-8e4c11ba-5c0f-41f6-8fcf-76d6eda6a5d0.png)

## Hexagonal Architecture Diagram 도출 
![image](https://user-images.githubusercontent.com/89397401/132273039-c813fad6-51a7-484d-8d7c-2c64fb491346.png)

# 구현

분석/설계 단계에서 도출된 헥사고날 아키텍처에 따라, 각 BC별로 대변되는 마이크로 서비스들을 Spring Boot로 구현하였다. 구현한 각 서비스를 로컬에서 실행하는 방법은 아래와 같다. (각자의 포트넘버는 8081 ~ 8084, 8088 이다)


```
   cd stock
   mvn spring-boot:run

   cd reservation
   mvn spring-boot:run

   cd payment
   mvn spring-boot:run

   cd delivery
   mvn spring-boot:run
   
   cd mypage
   mvn spring-boot:run

   cd gateway
   mvn spring-boot:run
  ```

## DDD(Domain Driven Design) 적용

- msaez.io 를 통해 구현한 Aggregate 단위로 Entity 를 선언 후, 구현을 진행하였다.
 Entity Pattern 과 Repository Pattern 을 적용하기 위해 Spring Data REST 의 RestRepository 를 적용하였다.

### Reservation 서비스의 Reservation.java
 
 ```java
 package bike;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="Reservation_table")
public class Reservation {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long reserveId;
    private Long bikeId;
    private String bikeNm;
    private String userId;
    private String userNm;
    private String reserveStore;
    private String reserveDt;
    private String reserveStatus;
    private int price; 

    @PrePersist
    public void onPrePersist()
    {
        if(System.getenv("G_STORE") != null)
        {
            this.setReserveStore(System.getenv("G_STORE"));
        }
        else
        {
            this.setReserveStore("분당야탑점");
        }
    }

    @PostPersist
    public void onPostPersist(){
        ReservationResistered reservationResistered = new ReservationResistered();        
        BeanUtils.copyProperties(this, reservationResistered);   
        reservationResistered.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        bike.external.Payment payment = new bike.external.Payment();
        
        // mappings goes here
        payment.setPayId(this.getId());
        payment.setReserveId(this.getReserveId());
        payment.setBikeId(this.getBikeId());
        payment.setPrice(this.getPrice());
        payment.setPayStatus("결제예정");

        ReservationApplication.applicationContext.getBean(bike.external.PaymentService.class).payment(payment);

    }
    @PostRemove
    public void onPostRemove()
    {
        ReservationCancelled reservationCancelled = new ReservationCancelled();
        BeanUtils.copyProperties(this, reservationCancelled);
        reservationCancelled.publishAfterCommit();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getReserveId() {
        return reserveId;
    }

    public void setReserveId(Long reserveId) {
        this.reserveId = reserveId;
    }
    public Long getBikeId() {
        return bikeId;
    }

    public void setBikeId(Long bikeId) {
        this.bikeId = bikeId;
    }
    public String getBikeNm() {
        return bikeNm;
    }

    public void setBikeNm(String bikeNm) {
        this.bikeNm = bikeNm;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserNm() {
        return userNm;
    }

    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }
    public String getReserveStore() {
        return reserveStore;
    }

    public void setReserveStore(String reserveStore) {
        this.reserveStore = reserveStore;
    }
    public String getReserveDt() {
        return reserveDt;
    }

    public void setReserveDt(String reserveDt) {
        this.reserveDt = reserveDt;
    }
    public String getReserveStatus() {
        return reserveStatus;
    }

    public void setReserveStatus(String reserveStatus) {
        this.reserveStatus = reserveStatus;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
} 
 ```

### Payment 시스템의 PolicyHandler.java

```java
package bike;

import bike.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

@Service
public class PolicyHandler{
    @Autowired PaymentRepository paymentRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverReservationCancelled_PaymentCancellation(@Payload ReservationCancelled reservationCancelled){

        if(!reservationCancelled.validate()) return;

        System.out.println("\n\n##### listener PaymentCancellation : " + reservationCancelled.toJson() + "\n\n");

        List<Payment> payments = paymentRepository.findByReserveId(reservationCancelled.getReserveId());
        payments.forEach(payment -> {
            paymentRepository.delete(payment);
        });
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}
}
```

## Gateway 적용
API Gateway를 통하여 마이크로 서비스들의 진입점을 통일하였다.

```yaml
server:
  port: 8088

---

spring:
  profiles: default
  cloud:
    gateway:
      routes:
        - id: stock
          uri: http://localhost:8081
          predicates:
            - Path=/stocks/** 
        - id: reservation
          uri: http://localhost:8082
          predicates:
            - Path=/reservations/** 
        - id: payment
          uri: http://localhost:8083
          predicates:
            - Path=/payments/** 
        - id: delivery
          uri: http://localhost:8084
          predicates:
            - Path=/deliveries/** 
        - id: myPage
          uri: http://localhost:8085
          predicates:
            - Path= /myPages/**
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins:
              - "*"
            allowedMethods:
              - "*"
            allowedHeaders:
              - "*"
            allowCredentials: true
            
---

spring:
  profiles: docker
  cloud:
    gateway:
      routes:
        - id: stock
          uri: http://stock:8080
          predicates:
            - Path=/stocks/** 
        - id: reservation
          uri: http://reservation:8080
          predicates:
            - Path=/reservations/** 
        - id: payment
          uri: http://payment:8080
          predicates:
            - Path=/payments/** 
        - id: delivery
          uri: http://delivery:8080
          predicates:
            - Path=/deliveries/** 
        - id: myPage
          uri: http://myPage:8080
          predicates:
            - Path= /myPages/**
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins:
              - "*"
            allowedMethods:
              - "*"
            allowedHeaders:
              - "*"
            allowCredentials: true

server:
  port: 8080
```

## Polyglot Persistence

- Delivery 서비스의 경우, 다른 마이크로 서비스와 달리 HSQL로 구현하였다.
- 이를 통해 서비스 간 다른 종류의 데이터베이스를 사용하여도 문제 없이 동작하여 Polyglot Persistence를 충족하였다.

|서비스|DB|pom.xml|
| :--: | :--: | :--: |
|Stock| H2 |![image](https://user-images.githubusercontent.com/89397401/132274624-e0b3eee5-49ee-4274-a09e-b10ccef45485.png)|
|Reservation| H2 |![image](https://user-images.githubusercontent.com/89397401/132274629-658fb41a-b754-415c-b6e4-37968052d593.png)|
|Payment| H2 |![image](https://user-images.githubusercontent.com/89397401/132274633-996f5810-29aa-4dbf-801d-b13282a830c7.png)|
|Delivery| HSQL |![image](https://user-images.githubusercontent.com/89397401/132274708-d02a9907-b1a1-42e2-bca1-e9e7e1c917b1.png)|
|MyPage| H2 |![image](https://user-images.githubusercontent.com/89397401/132274646-2acc0dd4-e358-4008-a169-ec9b2a9c7112.png)|

## 동기식 호출(Req/Res 방식)과 Fallback 처리

- Reservation 서비스의 external/PaymentService.java 내에 결제 서비스를 호출하기 위하여 FeignClient를 이용하여 Service 대행 인터페이스(Proxy)를 구현하였다.

### Reservation 서비스의 reservation/external/PaymentService.java

```java
package bike.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

//@FeignClient(name="payment", url="http://payment:8080")
@FeignClient(name="payment", url="${api.url.payment}")
public interface PaymentService {
    @RequestMapping(method= RequestMethod.GET, path="/payments")
    public void payment(@RequestBody Payment payment);

}
```


### Reservation 서비스의 Reservation.java
```java
package bike;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="Reservation_table")
public class Reservation {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long reserveId;
    private Long bikeId;
    private String bikeNm;
    private String userId;
    private String userNm;
    private String reserveStore;
    private String reserveDt;
    private String reserveStatus;
    private int price; 

    @PrePersist
    public void onPrePersist()
    {
        if(System.getenv("G_STORE") != null)
        {
            this.setReserveStore(System.getenv("G_STORE"));
        }
        else
        {
            this.setReserveStore("분당야탑점");
        }
    }

    @PostPersist
    public void onPostPersist(){
        ReservationResistered reservationResistered = new ReservationResistered();        
        BeanUtils.copyProperties(this, reservationResistered);   
        reservationResistered.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        bike.external.Payment payment = new bike.external.Payment();
        
        // mappings goes here
        payment.setPayId(this.getId());
        payment.setReserveId(this.getReserveId());
        payment.setBikeId(this.getBikeId());
        payment.setPrice(this.getPrice());
        payment.setPayStatus("결제예정");

        ReservationApplication.applicationContext.getBean(bike.external.PaymentService.class).payment(payment);

    }
    @PostRemove
    public void onPostRemove()
    {
        ReservationCancelled reservationCancelled = new ReservationCancelled();
        BeanUtils.copyProperties(this, reservationCancelled);
        reservationCancelled.publishAfterCommit();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    /** 이하 생략 **/
```

동작 확인

- Payment서비스를 내림

![image](https://user-images.githubusercontent.com/89397401/132276716-a1485e02-cf44-4b21-8a02-e0e36493cbfa.png)

- 예약(Reservation) 요청 및 에러 난 화면 표시

![image](https://user-images.githubusercontent.com/89397401/132276856-6151b908-2b49-484c-ab46-0b106055c0fd.png)

- Payment 서비스 재기동 후 다시 예약 요청

![image](https://user-images.githubusercontent.com/89397401/132277241-78cf0bb3-6d62-4740-964a-889503a8b09f.png)

- Payment 서비스에 저장 확인

![image](https://user-images.githubusercontent.com/89397401/132277350-7a529258-bfed-4ba9-8921-78e63cd0b7e1.png)





