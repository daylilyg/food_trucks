package org.daniel.dto;

import lombok.Data;

@Data
public class FoodTruckDto {
    private Integer locationId;

    private String applicant;

    private String facilityType;

    private String locationDescription;

    private String address;

    private String status;

    private String foodItems;

    private Double latitude;

    private Double longitude;

    private String dayshours;
}