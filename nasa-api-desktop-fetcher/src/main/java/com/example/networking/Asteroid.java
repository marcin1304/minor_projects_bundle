package com.example.networking;

import java.util.ArrayList;
import java.util.Map;

public class Asteroid {
    private final String id;
    private final String name;
    private final String nasaJplUrl;
    private final Boolean potentiallyHazardous;
    private final CloseApproachData closeApproachData;
    private final EstimatedDiameter estimatedDiameter;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNasaJplUrl() {
        return nasaJplUrl;
    }

    public Boolean getPotentiallyHazardous() {
        return potentiallyHazardous;
    }

    public CloseApproachData getCloseApproachData() {
        return closeApproachData;
    }

    public EstimatedDiameter getEstimatedDiameter() {
        return estimatedDiameter;
    }

    public Asteroid(Map<String, Object> dataMap) {
        id = dataMap.get("id").toString();
        name = dataMap.get("name").toString();
        nasaJplUrl = dataMap.get("nasa_jpl_url").toString();
        potentiallyHazardous = (Boolean) dataMap.get("is_potentially_hazardous_asteroid");
        ArrayList<Object> approachData = (ArrayList<Object>) dataMap.get("close_approach_data");
        Map<String, Object> firstApproachDate = (Map<String, Object>) approachData.get(0);
        closeApproachData = new CloseApproachData(firstApproachDate);
        Map<String, Object> diameterData = (Map<String, Object>) dataMap.get("estimated_diameter");
        estimatedDiameter = new EstimatedDiameter(diameterData);
    }
}
