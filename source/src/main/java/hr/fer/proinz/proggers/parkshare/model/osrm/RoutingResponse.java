package hr.fer.proinz.proggers.parkshare.model.osrm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class RoutingResponse {
    private String code;
    private List<Waypoint> waypoints;
    private List<Route> routes;

    public RoutingResponse() {
    }

    public RoutingResponse(String code, List<Waypoint> waypoints, List<Route> routes) {
        this.code = code;
        this.waypoints = waypoints;
        this.routes = routes;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    @Override
    public String toString() {
        return "RoutingResponse{" +
                "code='" + code + '\'' +
                ", routes=" + routes +
                '}';
    }
}
