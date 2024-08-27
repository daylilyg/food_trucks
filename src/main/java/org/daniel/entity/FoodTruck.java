package org.daniel.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Data
@Document(indexName = "food_truck")
public class FoodTruck {
    @Id
    private String id;

    private Integer locationId;

    private String applicant;

    @Field(type = FieldType.Keyword, value = "facility_type")
    private String facilityType;

    private String cnn;

    @Field(type = FieldType.Keyword, value = "location_description")
    private String locationDescription;

    private String address;

    private String blocklot;

    private String block;

    private String lot;

    private String permit;

    private String status;

    @Field(type = FieldType.Text, value = "food_items")
    private String foodItems;

    private Double x;

    private Double y;

    private Double latitude;

    private Double longitude;

    @GeoPointField
    private GeoPoint pointLocation;

    private String schedule;

    private String dayshours;

    private String zipCode;
}