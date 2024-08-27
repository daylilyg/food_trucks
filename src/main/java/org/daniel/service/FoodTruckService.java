package org.daniel.service;

import org.daniel.dto.FoodTruckDto;
import org.daniel.params.SearchParams;
import org.springframework.data.domain.Page;

public interface FoodTruckService {
    Page<FoodTruckDto> searchFoodTruck(SearchParams params);
}
