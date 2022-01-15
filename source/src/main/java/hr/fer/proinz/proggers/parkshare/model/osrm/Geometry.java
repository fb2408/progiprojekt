package hr.fer.proinz.proggers.parkshare.model.osrm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.List;

public class Geometry {
    private String type;
    private List<List<Double>> coordinates;

    public Geometry() {
    }

    public Geometry(String type, List<List<Double>> coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<List<Double>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<Double>> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return "Geometry{" +
                "type='" + type + '\'' +
                ", coordinates=" + coordinates +
                '}';
    }
}
