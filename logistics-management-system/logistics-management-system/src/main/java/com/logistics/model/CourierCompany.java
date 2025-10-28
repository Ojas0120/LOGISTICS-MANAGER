package com.logistics.model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class CourierCompany {
    private int id;
    private String name;
    private double pricePerKm;
    private double pricePerKg;
    private int baseHandlingTime; // in hours
    private String transportModes; // comma-separated string
    private boolean isActive;
    private LocalDateTime createdAt;

    public enum TransportMode {
        AIR, ROAD, RAIL
    }

    public CourierCompany() {}

    public CourierCompany(String name, double pricePerKm, double pricePerKg, 
                         int baseHandlingTime, String transportModes) {
        this.name = name;
        this.pricePerKm = pricePerKm;
        this.pricePerKg = pricePerKg;
        this.baseHandlingTime = baseHandlingTime;
        this.transportModes = transportModes;
        this.isActive = true;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPricePerKm() {
        return pricePerKm;
    }

    public void setPricePerKm(double pricePerKm) {
        this.pricePerKm = pricePerKm;
    }

    public double getPricePerKg() {
        return pricePerKg;
    }

    public void setPricePerKg(double pricePerKg) {
        this.pricePerKg = pricePerKg;
    }

    public int getBaseHandlingTime() {
        return baseHandlingTime;
    }

    public void setBaseHandlingTime(int baseHandlingTime) {
        this.baseHandlingTime = baseHandlingTime;
    }

    public String getTransportModes() {
        return transportModes;
    }

    public void setTransportModes(String transportModes) {
        this.transportModes = transportModes;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<TransportMode> getTransportModeList() {
        return Arrays.stream(transportModes.split(","))
                .map(String::trim)
                .map(TransportMode::valueOf)
                .toList();
    }

    public boolean supportsTransportMode(TransportMode mode) {
        return getTransportModeList().contains(mode);
    }

    @Override
    public String toString() {
        return "CourierCompany{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pricePerKm=" + pricePerKm +
                ", pricePerKg=" + pricePerKg +
                ", baseHandlingTime=" + baseHandlingTime +
                ", transportModes='" + transportModes + '\'' +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}
