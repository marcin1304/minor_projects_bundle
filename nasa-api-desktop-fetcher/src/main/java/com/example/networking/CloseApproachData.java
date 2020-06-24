package com.example.networking;

import java.util.Map;

public class CloseApproachData {
    private final String closeApproachDate;
    private final String relativeVelocityInKmPerSecond;
    private final String missDistanceInAstronomicalUnits;
    private final String orbitingBody;

    public String getCloseApproachDate() {
        return closeApproachDate;
    }

    public String getRelativeVelocityInKmPerSecond() {
        return relativeVelocityInKmPerSecond;
    }

    public String getMissDistanceInAstronomicalUnits() {
        return missDistanceInAstronomicalUnits;
    }

    public String getOrbitingBody() {
        return orbitingBody;
    }

    public CloseApproachData(Map<String, Object> dataMap) {

        closeApproachDate = initializeCloseApproachDate(dataMap);
        Map<String, Object> relativeVelocity = (Map<String, Object>) dataMap.get("relative_velocity");
        relativeVelocityInKmPerSecond = relativeVelocity.get("kilometers_per_second").toString();
        Map<String, Object> missDistance = (Map<String, Object>) dataMap.get("miss_distance");
        missDistanceInAstronomicalUnits = missDistance.get("astronomical").toString();
        orbitingBody = dataMap.get("orbiting_body").toString();
    }

    private String initializeCloseApproachDate(Map<String, Object> dataMap) {
        Object localCloseApproachDate = dataMap.get("close_approach_date_full");
        if(localCloseApproachDate == null)
           localCloseApproachDate = dataMap.get("close_approach_date");
        return localCloseApproachDate.toString();
    }
}
