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
    @Autowired DeliveryRepository deliveryRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaymentApproved_Delivery(@Payload PaymentApproved paymentApproved){

        if(!paymentApproved.validate()) return;

        System.out.println("\n\n##### listener Delivery :::: " + paymentApproved.toJson() + "\n\n");

        Delivery delivery = new Delivery();
        delivery.setBikeId(paymentApproved.getBikeId());
        delivery.setReserveId(paymentApproved.getReserveId());
        delivery.setPayId(paymentApproved.getPayId());

        delivery.setDeliveryStatus("배송준비");    
        deliveryRepository.save(delivery);

    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaymentCancelled_DeliveryCancellation(@Payload PaymentCancelled paymentCancelled){

        if(!paymentCancelled.validate()) return;

        System.out.println("\n\n##### listener DeliveryCancellation : " + paymentCancelled.toJson() + "\n\n");

        List<Delivery> deliveries = deliveryRepository.findByPayId(paymentCancelled.getPayId());
        deliveries.forEach(delivery -> {
            deliveryRepository.delete(delivery);
        });

    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}


}