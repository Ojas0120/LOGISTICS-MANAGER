package com.logistics.model;

import java.time.LocalDateTime;

public class ShipmentCalculation {
    private int id;
    private int shipmentId;
    private int companyId;
    private double totalCost;
    private int estimatedDeliveryTime; // in hours
    private LocalDateTime createdAt;

    public ShipmentCalculation() {}

    public ShipmentCalculation(int shipmentId, int companyId, double totalCost, int estimatedDeliveryTime) {
        this.shipmentId = shipmentId;
        this.companyId = companyId;
        this.totalCost = totalCost;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(int shipmentId) {
        this.shipmentId = shipmentId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public int getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    public void setEstimatedDeliveryTime(int estimatedDeliveryTime) {
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ShipmentCalculation{" +
                "id=" + id +
                ", shipmentId=" + shipmentId +
                ", companyId=" + companyId +
                ", totalCost=" + totalCost +
                ", estimatedDeliveryTime=" + estimatedDeliveryTime +
                ", createdAt=" + createdAt +
                '}';
    }
}
