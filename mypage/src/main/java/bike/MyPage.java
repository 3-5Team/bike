package bike;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="MyPage_table")
public class MyPage {

        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private Long id;
        private Long bikeId;
        private String bikeNm;
        private Long reserveId;
        private String userId;
        private String userNm;
        private String reserveStore;
        private String reserveDt;
        private Long payId;
        private Integer price;
        private String payStatus;
        private Long deliveryId;
        private String deliveryStatus;
        private String deliveryDt;
        private String reserveStatus;


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
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
        public Long getReserveId() {
            return reserveId;
        }

        public void setReserveId(Long reserveId) {
            this.reserveId = reserveId;
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
        public Long getPayId() {
            return payId;
        }

        public void setPayId(Long payId) {
            this.payId = payId;
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
        public Long getDeliveryId() {
            return deliveryId;
        }

        public void setDeliveryId(Long deliveryId) {
            this.deliveryId = deliveryId;
        }
        public String getDeliveryStatus() {
            return deliveryStatus;
        }

        public void setDeliveryStatus(String deliveryStatus) {
            this.deliveryStatus = deliveryStatus;
        }
        public String getDeliveryDt() {
            return deliveryDt;
        }

        public void setDeliveryDt(String deliveryDt) {
            this.deliveryDt = deliveryDt;
        }
        public String getReserveStatus() {
            return reserveStatus;
        }

        public void setReserveStatus(String reserveStatus) {
            this.reserveStatus = reserveStatus;
        }

}