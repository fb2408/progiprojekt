package hr.fer.proinz.proggers.parkshare.model;

import hr.fer.proinz.proggers.parkshare.dto.ParkingSpotDTO;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "parkingspot")
@Entity
@NoArgsConstructor
public class ParkingSpot {
    @EmbeddedId
    private ParkingSpotId id;

    @Column(name = "parkingspottype", nullable = false, length = 20)
    private String parkingSpotType;

    @Column(name = "canbereserved", nullable = false)
    private Boolean canBeReserved = false;

    @Column(name = "point1x", precision = 131089)
    private BigDecimal point1x;

    @Column(name = "point1y", precision = 131089)
    private BigDecimal point1y;

    @Column(name = "point2x", precision = 131089)
    private BigDecimal point2x;

    @Column(name = "point2y", precision = 131089)
    private BigDecimal point2y;

    @Column(name = "point3x", precision = 131089)
    private BigDecimal point3x;

    @Column(name = "point3y", precision = 131089)
    private BigDecimal point3y;

    @Column(name = "point4x", precision = 131089)
    private BigDecimal point4x;

    @Column(name = "point4y", precision = 131089)
    private BigDecimal point4y;

    public ParkingSpot(ParkingSpotDTO parkingSpotDTO) {
        this.id = parkingSpotDTO.getId();
        this.parkingSpotType = parkingSpotDTO.getParkingSpotType();
        this.canBeReserved = parkingSpotDTO.isCanBeReserved();
        this.point1x = parkingSpotDTO.getPoint1x();
        this.point1y = parkingSpotDTO.getPoint1y();
        this.point2x = parkingSpotDTO.getPoint2x();
        this.point2y = parkingSpotDTO.getPoint2y();
        this.point3x = parkingSpotDTO.getPoint3x();
        this.point3y = parkingSpotDTO.getPoint3y();
        this.point4x = parkingSpotDTO.getPoint4x();
        this.point4y = parkingSpotDTO.getPoint4y();
    }

    public BigDecimal getPoint4y() {
        return point4y;
    }

    public void setPoint4y(BigDecimal point4y) {
        this.point4y = point4y;
    }

    public BigDecimal getPoint4x() {
        return point4x;
    }

    public void setPoint4x(BigDecimal point4x) {
        this.point4x = point4x;
    }

    public BigDecimal getPoint3y() {
        return point3y;
    }

    public void setPoint3y(BigDecimal point3y) {
        this.point3y = point3y;
    }

    public BigDecimal getPoint3x() {
        return point3x;
    }

    public void setPoint3x(BigDecimal point3x) {
        this.point3x = point3x;
    }

    public BigDecimal getPoint2y() {
        return point2y;
    }

    public void setPoint2y(BigDecimal point2y) {
        this.point2y = point2y;
    }

    public BigDecimal getPoint2x() {
        return point2x;
    }

    public void setPoint2x(BigDecimal point2x) {
        this.point2x = point2x;
    }

    public BigDecimal getPoint1y() {
        return point1y;
    }

    public void setPoint1y(BigDecimal point1y) {
        this.point1y = point1y;
    }

    public BigDecimal getPoint1x() {
        return point1x;
    }

    public void setPoint1x(BigDecimal point1x) {
        this.point1x = point1x;
    }

    public Boolean getCanBeReserved() {
        return canBeReserved;
    }

    public void setCanBeReserved(Boolean canBeReserved) {
        this.canBeReserved = canBeReserved;
    }

    public String getParkingSpotType() {
        return parkingSpotType;
    }

    public void setParkingSpotType(String parkingSpotType) {
        this.parkingSpotType = parkingSpotType;
    }

    public ParkingSpotId getId() {
        return id;
    }

    public void setId(ParkingSpotId id) {
        this.id = id;
    }
}