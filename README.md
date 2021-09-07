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
![image](https://user-images.githubusercontent.com/79756040/129881425-3b9d3209-16b3-4d8a-a565-c82a85056980.png)

### 부적격 이벤트 탈락


### 완성된 1차 모형
![image](https://user-images.githubusercontent.com/89397401/132272997-acadd044-2756-4557-9b86-239f1d2e66fe.png)

### 완성된 최종 모형
![image](https://user-images.githubusercontent.com/89397401/132273023-8e4c11ba-5c0f-41f6-8fcf-76d6eda6a5d0.png)

## Hexagonal Architecture Diagram 도출 
![image](https://user-images.githubusercontent.com/89397401/132273039-c813fad6-51a7-484d-8d7c-2c64fb491346.png)

# 구현

