package bike;

import bike.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MyPageViewHandler {


    @Autowired
    private MyPageRepository myPageRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenReservationResistered_then_CREATE_1 (@Payload ReservationResistered reservationResistered) {
        try {

            if (!reservationResistered.validate()) return;

            // view 객체 생성
            MyPage myPage = new MyPage();
            // view 객체에 이벤트의 Value 를 set 함
            myPage.setBikeId(reservationResistered.getBikeId());
            myPage.setBikeNm(reservationResistered.getBikeNm());
            myPage.setReserveId(reservationResistered.getReserveId());
            myPage.setUserId(reservationResistered.getUserId());
            myPage.setUserNm(reservationResistered.getUserNm());
            myPage.setReserveStore(reservationResistered.getReserveStore());
            myPage.setReserveDt(reservationResistered.getReserveDt());
            myPage.setReserveStatus(reservationResistered.getReserveStatus());
            // view 레파지 토리에 save
            myPageRepository.save(myPage);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenReservationCancelled_then_UPDATE_1(@Payload ReservationCancelled reservationCancelled) {
        try {
            if (!reservationCancelled.validate()) return;
                // view 객체 조회
            Optional<MyPage> myPageOptional = myPageRepository.findById(reservationCancelled.getReserveId());

            if( myPageOptional.isPresent()) 
            {
                MyPage myPage = myPageOptional.get();
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                //myPage.setReserveStatus(reservationCancelled.getReserveStatus());
                myPage.setReserveStatus("예약취소"); //예약취소
                // view 레파지 토리에 save
                myPageRepository.save(myPage);
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenPaymentApproved_then_UPDATE_2(@Payload PaymentApproved paymentApproved) {
        try {
            if (!paymentApproved.validate()) return;
                // view 객체 조회
            Optional<MyPage> myPageOptional = myPageRepository.findById(paymentApproved.getPayId());

            if( myPageOptional.isPresent()) 
            {
                MyPage myPage = myPageOptional.get();
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                //myPage.setPayStatus(paymentApproved.getPayStatus());
                myPage.setPayStatus("결제완료");
                // view 레파지 토리에 save
                myPageRepository.save(myPage);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenPaymentCancelled_then_UPDATE_3(@Payload PaymentCancelled paymentCancelled) {
        try {
            if (!paymentCancelled.validate()) return;
                // view 객체 조회
            Optional<MyPage> myPageOptional = myPageRepository.findById(paymentCancelled.getPayId());

            if( myPageOptional.isPresent()) 
            {
                MyPage myPage = myPageOptional.get();
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                //myPage.setPayStatus("Payment Cancellation"); //결제취소
                myPage.setPayStatus("결제취소"); //결제취소
                // view 레파지 토리에 save
                myPageRepository.save(myPage);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryStarted_then_UPDATE_4(@Payload DeliveryStarted deliveryStarted) {
        try {
            if (!deliveryStarted.validate()) return;
                // view 객체 조회
            Optional<MyPage> myPageOptional = myPageRepository.findById(deliveryStarted.getDeliveryId());

            if( myPageOptional.isPresent()) 
            {
                MyPage myPage = myPageOptional.get();
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                //myPage.setDeliveryStatus(deliveryStarted.getDeliveryStatus());
                myPage.setDeliveryStatus("배송시작");
                // view 레파지 토리에 save
                myPageRepository.save(myPage);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryCancelled_then_UPDATE_5(@Payload DeliveryCancelled deliveryCancelled) {
        try {
            if (!deliveryCancelled.validate()) return;
                // view 객체 조회
            Optional<MyPage> myPageOptional = myPageRepository.findById(deliveryCancelled.getDeliveryId());

            if( myPageOptional.isPresent()) 
            {
                MyPage myPage = myPageOptional.get();
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                //myPage.setDeliveryStatus("Cancel Delivery!"); //배송취소
                myPage.setDeliveryStatus("배송취소"); //배송취소
                // view 레파지 토리에 save
                myPageRepository.save(myPage);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

