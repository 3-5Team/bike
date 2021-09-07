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