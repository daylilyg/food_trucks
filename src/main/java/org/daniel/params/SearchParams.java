package org.daniel.params;

import lombok.Data;
import co.elastic.clients.elasticsearch._types.CoordsGeoBounds;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Data
public class SearchParams {
    private int pageNumber;

    private int pageSize;

    private String query;

    private GeoPoint center;

    private Double distanceMeter;

    private CoordsGeoBounds boundingBox;

    public boolean isDistanceMeter() {
        return center != null && distanceMeter != null;
    }

    public boolean isBoundingBox() {
        return boundingBox != null;
    }
}
