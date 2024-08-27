package org.daniel.controller;

import co.elastic.clients.elasticsearch._types.CoordsGeoBounds;
import org.daniel.dto.FoodTruckDto;
import org.daniel.params.SearchParams;
import org.daniel.service.FoodTruckService;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FoodTruckController {
    private FoodTruckService foodTruckService;

    public FoodTruckController(FoodTruckService foodTruckService) {
        this.foodTruckService = foodTruckService;
    }

    @GetMapping("/greet")
    public String greeting() {
        return "Greet";
    }

    @GetMapping("/search-food-truck")
    public Page<FoodTruckDto> searchFoodTruck(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon,
            @RequestParam(required = false) Double distance,
            @RequestParam(required = false) Double top,
            @RequestParam(required = false) Double left,
            @RequestParam(required = false) Double bottom,
            @RequestParam(required = false) Double right,
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        SearchParams params = new SearchParams();
        if (lat != null && lon != null) {
            params.setCenter(new GeoPoint(lat, lon));
            params.setDistanceMeter(distance);
        }
        if (top != null && left != null && bottom != null && right != null) {
            params.setBoundingBox(CoordsGeoBounds.of(cgb -> cgb.top(top).left(left).bottom(bottom).right(right)));
        }
        params.setQuery(query);
        params.setPageNumber(pageNo);
        params.setPageSize(pageSize);
        return foodTruckService.searchFoodTruck(params);
    }
}
