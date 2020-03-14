package ru.maxmorev.eshop.commodity.api.rest.response;

import lombok.Builder;
import lombok.Getter;
import ru.maxmorev.eshop.commodity.api.entities.CommodityType;

@Builder
@Getter
public class CommodityTypeDto {

    private Long id;
    private String name;
    private String description;

    public static CommodityTypeDto of(CommodityType ct){
        return CommodityTypeDto.builder()
                .description(ct.getDescription())
                .id(ct.getId())
                .name(ct.getName())
                .build();
    }

}
