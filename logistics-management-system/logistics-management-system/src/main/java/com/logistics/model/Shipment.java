package com.logistics.model;

import java.time.LocalDateTime;

public class Shipment {
    private int id;
    private int userId;
    private String senderCity;
    private String receiverCity;
    private double distance;
    private double weight;
    private Priority priority;
    private TrafficCondition trafficCondition;
    private CourierCompany.TransportMode transportMode;
    private Integer selectedCompanyId; // NEW: Selected company for this shipment
    private Status status; // NEW: Shipment status
    private LocalDateTime createdAt;

    public enum Priority {
        LOW(0.0), NORMAL(0.0), HIGH(50.0), URGENT(100.0);
        
        private final double surcharge;
        
        Priority(double surcharge) {
            this.surcharge = surcharge;
        }
        
        public double getSurcharge() {
            return surcharge;
        }
    }

    public enum TrafficCondition {
        LOW(0.0), NORMAL(0.2), HIGH(0.5);
        
        private final double delayFactor;
        
        TrafficCondition(double delayFactor) {
            this.delayFactor = delayFactor;
        }
        
        public double getDelayFactor() {
            return delayFactor;
        }
    }

    public enum Status {
        PENDING, CONFIRMED, IN_TRANSIT, DELIVERED, CANCELLED
    }

    public Shipment() {}

    public Shipment(int userId, String senderCity, String receiverCity, 
                   double distance, double weight, Priority priority, 
                   TrafficCondition trafficCondition, CourierCompany.TransportMode transportMode) {
        this.userId = userId;
        this.senderCity = senderCity;
        this.receiverCity = receiverCity;
        this.distance = distance;
        this.weight = weight;
        this.priority = priority;
        this.trafficCondition = trafficCondition;
        this.transportMode = transportMode;
        this.status = Status.PENDING;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSenderCity() {
        return senderCity;
    }

    public void setSenderCity(String senderCity) {
        this.senderCity = senderCity;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public TrafficCondition getTrafficCondition() {
        return trafficCondition;
    }

    public void setTrafficCondition(TrafficCondition trafficCondition) {
        this.trafficCondition = trafficCondition;
    }

    public CourierCompany.TransportMode getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(CourierCompany.TransportMode transportMode) {
        this.transportMode = transportMode;
    }

    public Integer getSelectedCompanyId() {
        return selectedCompanyId;
    }

    public void setSelectedCompanyId(Integer selectedCompanyId) {
        this.selectedCompanyId = selectedCompanyId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Shipment{" +
                "id=" + id +
                ", userId=" + userId +
                ", senderCity='" + senderCity + '\'' +
                ", receiverCity='" + receiverCity + '\'' +
                ", distance=" + distance +
                ", weight=" + weight +
                ", priority=" + priority +
                ", trafficCondition=" + trafficCondition +
                ", transportMode=" + transportMode +
                ", selectedCompanyId=" + selectedCompanyId +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
