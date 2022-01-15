package hr.fer.proinz.proggers.parkshare.model.osrm;

import java.util.List;
import java.awt.*;

public class Waypoint {
    private String hint, name;
    private double distance;
    private List<Double> location;

    public Waypoint() {
    }

    public Waypoint(String hint, String name, double distance, java.util.List<Double> location) {
        this.hint = hint;
        this.name = name;
        this.distance = distance;
        this.location = location;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }
}
