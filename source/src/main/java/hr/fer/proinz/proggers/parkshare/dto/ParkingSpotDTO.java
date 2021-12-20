package hr.fer.proinz.proggers.parkshare.dto;

import java.math.BigDecimal;

public class ParkingSpotDTO {
        private String parkingSpotType;
        private Boolean canBeReserved = false;
        private BigDecimal point1x;
        private BigDecimal point1y;
        private BigDecimal point2x;
        private BigDecimal point2y;
        private BigDecimal point3x;
        private BigDecimal point3y;
        private BigDecimal point4x;
        private BigDecimal point4y;


        public ParkingSpotDTO(String parkingSpotType, Boolean canBeReserved, BigDecimal point1x, BigDecimal point1y, BigDecimal point2x, BigDecimal point2y, BigDecimal point3x, BigDecimal point3y, BigDecimal point4x, BigDecimal point4y) {
            this.parkingSpotType = parkingSpotType;
            this.canBeReserved = canBeReserved;
            this.point1x = point1x;
            this.point1y = point1y;
            this.point2x = point2x;
            this.point2y = point2y;
            this.point3x = point3x;
            this.point3y = point3y;
            this.point4x = point4x;
            this.point4y = point4y;
        }

        public ParkingSpotDTO() {
        }

        public String getParkingSpotType() {
            return parkingSpotType;
        }

        public void setParkingSpotType(String parkingSpotType) {
            this.parkingSpotType = parkingSpotType;
        }

        public Boolean getCanBeReserved() {
            return canBeReserved;
        }

        public void setCanBeReserved(Boolean canBeReserved) {
            this.canBeReserved = canBeReserved;
        }

        public BigDecimal getPoint1x() {
            return point1x;
        }

        public void setPoint1x(BigDecimal point1x) {
            this.point1x = point1x;
        }

        public BigDecimal getPoint1y() {
            return point1y;
        }

        public void setPoint1y(BigDecimal point1y) {
            this.point1y = point1y;
        }

        public BigDecimal getPoint2x() {
            return point2x;
        }

        public void setPoint2x(BigDecimal point2x) {
            this.point2x = point2x;
        }

        public BigDecimal getPoint2y() {
            return point2y;
        }

        public void setPoint2y(BigDecimal point2y) {
            this.point2y = point2y;
        }

        public BigDecimal getPoint3x() {
            return point3x;
        }

        public void setPoint3x(BigDecimal point3x) {
            this.point3x = point3x;
        }

        public BigDecimal getPoint3y() {
            return point3y;
        }

        public void setPoint3y(BigDecimal point3y) {
            this.point3y = point3y;
        }

        public BigDecimal getPoint4x() {
            return point4x;
        }

        public void setPoint4x(BigDecimal point4x) {
            this.point4x = point4x;
        }

        public BigDecimal getPoint4y() {
            return point4y;
        }

        public void setPoint4y(BigDecimal point4y) {
            this.point4y = point4y;
        }
}
