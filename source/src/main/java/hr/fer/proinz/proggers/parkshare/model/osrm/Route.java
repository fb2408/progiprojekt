package hr.fer.proinz.proggers.parkshare.model.osrm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class Route {
    private double distance, duration, weight;
    private Geometry geometry;

    public Route() {
    }

    public Route(double distance, double duration, double weight, Geometry geometry) {
        this.distance = distance;
        this.duration = duration;
        this.weight = weight;
        this.geometry = geometry;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @Override
    public String toString() {
        return "Route{" +
                "distance=" + distance +
                ", duration=" + duration +
                ", weight=" + weight +
                ", geometry=" + geometry +
                '}';
    }
}
