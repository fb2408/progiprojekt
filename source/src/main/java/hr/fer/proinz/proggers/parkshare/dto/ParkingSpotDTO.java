package hr.fer.proinz.proggers.parkshare.dto;

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
}
