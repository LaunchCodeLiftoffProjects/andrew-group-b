package org.launchcode.ouchy.models;

import java.util.Comparator;

public class DistanceSearchData implements Comparable<DistanceSearchData> {

    private String time;

    private Double distance;

    private String companyName;

    private String companyAddress;

    public DistanceSearchData(String time, Double distance, String companyName, String companyAddress) {
        this.time = time;
        this.distance = distance;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    @Override
    public int compareTo(DistanceSearchData o) {
         return this.distance.compareTo(o.getDistance());
    }
}
