package org.daniel.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import co.elastic.clients.elasticsearch._types.GeoBounds;
import co.elastic.clients.elasticsearch._types.GeoLocation;
import io.micrometer.common.util.StringUtils;
import org.daniel.common.FoodTruckConverter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.util.ObjectBuilder;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import org.daniel.dto.FoodTruckDto;
import org.daniel.entity.FoodTruck;
import org.daniel.params.SearchParams;
import org.daniel.service.FoodTruckService;


@Slf4j
@Service
public class FoodTruckServiceImpl implements FoodTruckService {
    private List<String> TEXT_FIELDS = List.of("applicant", "food_items");

    private String LOCATION = "point_location";

    private ElasticsearchTemplate esTemplate;

    public FoodTruckServiceImpl(ElasticsearchTemplate esTemplate) {
        this.esTemplate = esTemplate;
    }

    private Function<Query.Builder, ObjectBuilder<Query>> composedQueryBuilder(SearchParams params) {
        return (Query.Builder builder) -> {
            BoolQuery.Builder rootQuery = QueryBuilders.bool();
            if (StringUtils.isNotBlank(params.getQuery())) {
                rootQuery.must(QueryBuilders.multiMatch(q -> q
                        .fields(TEXT_FIELDS)
                        .query(params.getQuery())
                ));
            }
            if (params.isDistanceMeter()) {
                GeoPoint center = params.getCenter();
                String distance = String.valueOf(params.getDistanceMeter());
                rootQuery.must(QueryBuilders.geoDistance(q -> q
                        .field(LOCATION)
                        .location(new GeoLocation.Builder()
                                .coords(Arrays.asList(center.getLon(), center.getLat()))
                                .build()
                        )
                        .distance(distance)
                ));
            }
            if (params.isBoundingBox()) {
                rootQuery.must(QueryBuilders.geoBoundingBox(q -> q
                        .field(LOCATION)
                        .boundingBox(GeoBounds.of(gb -> gb.coords(params.getBoundingBox())))
                ));
            }
            builder.bool(rootQuery.build());
            return builder;
        };
    }

    @Override
    public Page<FoodTruckDto> searchFoodTruck(SearchParams params) {
        Pageable page = PageRequest.of(params.getPageNumber(), params.getPageSize());
        NativeQueryBuilder searchQueryBuilder = new NativeQueryBuilder()
                .withQuery(composedQueryBuilder(params))
                .withPageable(page);
        NativeQuery query = searchQueryBuilder.build();
        SearchHits<FoodTruck> result = esTemplate.search(query, FoodTruck.class);
        List<FoodTruckDto> content = new ArrayList<>();
        result.forEach(ft -> content.add(FoodTruckConverter.INST.toDto(ft.getContent())));
        return new PageImpl<>(content, page, result.getTotalHits());
    }
}
