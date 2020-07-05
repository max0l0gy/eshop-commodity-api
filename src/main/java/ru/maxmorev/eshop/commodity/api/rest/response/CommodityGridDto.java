package ru.maxmorev.eshop.commodity.api.rest.response;

import lombok.Builder;
import lombok.Getter;
import ru.maxmorev.eshop.commodity.api.rest.request.CommodityBranchDto;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class CommodityGridDto {
    private int totalPages;
    private int currentPage;
    private long totalRecords;
    private List<CommodityDto> commodityData;

    public static CommodityGridDto of(CommodityGrid commodityGrid) {
        return CommodityGridDto.builder()
                .totalPages(commodityGrid.getTotalPages())
                .currentPage(commodityGrid.getCurrentPage())
                .totalRecords(commodityGrid.getTotalRecords())
                .commodityData(commodityGrid.getCommodityData()
                        .stream()
                        .map(c -> CommodityDto.of(c, c.getBranches()
                                .stream()
                                .map(CommodityBranchDto::of)
                                .collect(Collectors.toList()))
                        ).collect(Collectors.toList())).build();
    }
}
