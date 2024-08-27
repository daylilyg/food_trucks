package org.daniel.common;

import org.daniel.dto.FoodTruckDto;
import org.daniel.entity.FoodTruck;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FoodTruckConverter {
    FoodTruckConverter INST = Mappers.getMapper( FoodTruckConverter.class );

    FoodTruckDto toDto(FoodTruck foodTruck);
}
