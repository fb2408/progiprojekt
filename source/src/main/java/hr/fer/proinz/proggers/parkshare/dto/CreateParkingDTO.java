package hr.fer.proinz.proggers.parkshare.dto;

import hr.fer.proinz.proggers.parkshare.model.Parking;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
public class CreateParkingDTO {
    private String parkingName;
    private String parkingPhoto;
    private BigDecimal hourlyPrice;
    private String description;
    private BigDecimal pointX;
    private BigDecimal pointY;

    public CreateParkingDTO(Parking parking) {
        this.parkingName = parking.getParkingName();
        this.parkingPhoto = parking.getParkingPhoto();
        this.hourlyPrice = parking.getHourlyPrice();
        this.description = parking.getDescription();
        this.pointX = parking.getEntrancepointx();
        this.pointY = parking.getEntrancepointy();
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public String getParkingPhoto() {
        return parkingPhoto;
    }

    public void setParkingPhoto(String parkingPhoto) {
        this.parkingPhoto = parkingPhoto;
    }

    public BigDecimal getHourlyPrice() {
        return hourlyPrice;
    }

    public void setHourlyPrice(BigDecimal hourlyPrice) {
        this.hourlyPrice = hourlyPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPointX() {
        return pointX;
    }

    public void setPointX(BigDecimal pointX) {
        this.pointX = pointX;
    }

    public BigDecimal getPointY() {
        return pointY;
    }

    public void setPointY(BigDecimal pointY) {
        this.pointY = pointY;
    }
}
