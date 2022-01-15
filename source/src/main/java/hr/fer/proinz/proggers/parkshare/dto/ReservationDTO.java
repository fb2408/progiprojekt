package hr.fer.proinz.proggers.parkshare.dto;

public class ReservationDTO {
    private int ownerUserId;
    private int parkingSpotNumber;
    private String timeOfStart;
    private int duration;
    private boolean recurring;
    private boolean payNow;

    public ReservationDTO(int ownerUserId, int parkingSpotNumber, String timeOfStart, int duration, boolean recurring, boolean payNow) {
        this.ownerUserId = ownerUserId;
        this.parkingSpotNumber = parkingSpotNumber;
        this.timeOfStart = timeOfStart;
        this.duration = duration;
        this.recurring = recurring;
        this.payNow = payNow;
    }

    public ReservationDTO() {
    }

    public int getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(int ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public int getParkingSpotNumber() {
        return parkingSpotNumber;
    }

    public void setParkingSpotNumber(int parkingSpotNumber) {
        this.parkingSpotNumber = parkingSpotNumber;
    }

    public String getTimeOfStart() {
        return timeOfStart;
    }

    public void setTimeOfStart(String timeOfStart) {
        this.timeOfStart = timeOfStart;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public boolean isPayNow() {
        return payNow;
    }

    public void setPayNow(boolean payNow) {
        this.payNow = payNow;
    }

    @Override
    public String toString() {
        return "ReservationDTO{" +
                "ownerUserId=" + ownerUserId +
                ", parkingSpotNumber=" + parkingSpotNumber +
                ", timeOfStart='" + timeOfStart + '\'' +
                ", duration=" + duration +
                ", recurring=" + recurring +
                ", payNow=" + payNow +
                '}';
    }
}
