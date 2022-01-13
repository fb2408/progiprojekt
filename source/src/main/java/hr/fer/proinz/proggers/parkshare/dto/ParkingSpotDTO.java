package hr.fer.proinz.proggers.parkshare.dto;

import hr.fer.proinz.proggers.parkshare.model.ParkingSpot;
import hr.fer.proinz.proggers.parkshare.model.ParkingSpotId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ParkingSpotDTO {

    private ParkingSpotId id;
    private String parkingSpotType;
    private boolean canBeReserved = false;
    private BigDecimal point1x;
    private BigDecimal point1y;
    private BigDecimal point2x;
    private BigDecimal point2y;
    private BigDecimal point3x;
    private BigDecimal point3y;
    private BigDecimal point4x;
    private BigDecimal point4y;

    public ParkingSpotDTO(ParkingSpot ps){
        this.id = ps.getId();
        this.parkingSpotType = ps.getParkingSpotType();
        this.canBeReserved = ps.getCanBeReserved();
        this.point1x = ps.getPoint1x();
        this.point2x = ps.getPoint2x();
        this.point3x = ps.getPoint3x();
        this.point4x = ps.getPoint4x();
        this.point1y = ps.getPoint1y();
        this.point2y = ps.getPoint2y();
        this.point3y = ps.getPoint3y();
        this.point4y = ps.getPoint4y();
    }

}
