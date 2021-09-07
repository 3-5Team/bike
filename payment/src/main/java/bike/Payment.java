package bike;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="Payment_table")
public class Payment {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long payId;
    private Long reserveId;
    private Long bikeId;
    private Integer price;
    private String payStatus;

    // @PostPersist
    // public void onPostPersist()
    // {
    //     PaymentApproved paymentApproved = new PaymentApproved();
    //     //paymentApproved.setPayStatus("Payment.java : 결제완료"); //결제완료
    //     BeanUtils.copyProperties(this, paymentApproved);
        
    //     paymentApproved.publishAfterCommit();
    // }

    @PrePersist
    public void onPrePersist()
    {
        try 
        {
            Thread.currentThread();
            Thread.sleep((long) (800 + Math.random() * 220));
        }   
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
    }

    @PostUpdate
    public void onPostUpdate()
    {
        PaymentApproved paymentApproved = new PaymentApproved();        
        BeanUtils.copyProperties(this, paymentApproved);        
        paymentApproved.publishAfterCommit();
    }

    @PostRemove
    public void onPostRemove()
    {
        PaymentCancelled paymentCancelled = new PaymentCancelled();
        BeanUtils.copyProperties(this, paymentCancelled);
        paymentCancelled.publishAfterCommit();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getPayId() {
        return payId;
    }

    public void setPayId(Long payId) {
        this.payId = payId;
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
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }




}