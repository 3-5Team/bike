package bike;

public class ReservationCancelled extends AbstractEvent {

    private Long id;
    private Long reserveId;
    private Long bikeId;
    private String bikeNm;
    private String userId;
    private String userNm;
    private String reserveStore;
    private String reserveDt;
    private String reserveStatus;
    private int price; //추가

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