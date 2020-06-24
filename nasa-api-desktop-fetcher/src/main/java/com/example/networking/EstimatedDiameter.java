package com.example.networking;

import java.util.Map;

public class EstimatedDiameter {
    private final Double minInKm;
    private final Double maxInKm;

    public Double getMinInKm() {
        return minInKm;
    }

    public Double getMaxInKm() {
        return maxInKm;
    }

    public EstimatedDiameter(Map<String, Object> dataMap) {
        Map<String, Object> dataMapInKm = (Map<String, Object>) dataMap.get("kilometers");
        minInKm = (Double) dataMapInKm.get("estimated_diameter_min");
        maxInKm = (Double) dataMapInKm.get("estimated_diameter_max");
    }
}
