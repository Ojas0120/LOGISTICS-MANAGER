package com.logistics.service;

import com.logistics.dao.CourierCompanyDAO;
import com.logistics.dao.ShipmentCalculationDAO;
import com.logistics.model.CourierCompany;
import com.logistics.model.Shipment;
import com.logistics.model.ShipmentCalculation;

import java.util.ArrayList;
import java.util.List;

public class CalculationService {
    private final CourierCompanyDAO companyDAO;
    private final ShipmentCalculationDAO calculationDAO;

    public CalculationService() {
        this.companyDAO = new CourierCompanyDAO();
        this.calculationDAO = new ShipmentCalculationDAO();
    }

    public List<ShipmentCalculation> calculateShipmentCosts(Shipment shipment) {
        List<ShipmentCalculation> calculations = new ArrayList<>();
        
        // Get all companies that support the requested transport mode
        List<CourierCompany> companies = companyDAO.findByTransportMode(shipment.getTransportMode());
        
        for (CourierCompany company : companies) {
            double totalCost = calculateTotalCost(shipment, company);
            int estimatedDeliveryTime = calculateDeliveryTime(shipment, company);
            
            ShipmentCalculation calculation = new ShipmentCalculation(
                shipment.getId(), 
                company.getId(), 
                totalCost, 
                estimatedDeliveryTime
            );
            
            calculations.add(calculation);
        }
        
        // Sort by total cost (ascending)
        calculations.sort((c1, c2) -> Double.compare(c1.getTotalCost(), c2.getTotalCost()));
        
        return calculations;
    }

    private double calculateTotalCost(Shipment shipment, CourierCompany company) {
        // Base cost calculation: (price_per_km * distance) + (price_per_kg * weight)
        double baseCost = (company.getPricePerKm() * shipment.getDistance()) + 
                         (company.getPricePerKg() * shipment.getWeight());
        
        // Add priority surcharge
        double prioritySurcharge = shipment.getPriority().getSurcharge();
        
        return baseCost + prioritySurcharge;
    }

    private int calculateDeliveryTime(Shipment shipment, CourierCompany company) {
        // Base delivery time calculation
        double baseTime = calculateBaseDeliveryTime(shipment.getDistance(), shipment.getTransportMode());
        
        // Add company's base handling time
        double totalTime = baseTime + company.getBaseHandlingTime();
        
        // Apply traffic delay factor
        double trafficDelay = totalTime * shipment.getTrafficCondition().getDelayFactor();
        totalTime += trafficDelay;
        
        // Round up to the nearest hour
        return (int) Math.ceil(totalTime);
    }

    private double calculateBaseDeliveryTime(double distance, CourierCompany.TransportMode transportMode) {
        // Average speeds for different transport modes (km/h)
        double speed;
        switch (transportMode) {
            case AIR:
                speed = 800.0; // Average commercial aircraft speed
                break;
            case RAIL:
                speed = 100.0; // Average freight train speed
                break;
            case ROAD:
                speed = 60.0;  // Average truck speed
                break;
            default:
                speed = 60.0;
        }
        
        return distance / speed;
    }

    public void saveCalculations(List<ShipmentCalculation> calculations) {
        for (ShipmentCalculation calculation : calculations) {
            calculationDAO.create(calculation);
        }
    }

    public List<ShipmentCalculation> getCalculationsForShipment(int shipmentId) {
        return calculationDAO.findByShipmentId(shipmentId);
    }

    public void deleteCalculationsForShipment(int shipmentId) {
        calculationDAO.deleteByShipmentId(shipmentId);
    }
}
