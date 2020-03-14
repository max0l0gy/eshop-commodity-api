package ru.maxmorev.eshop.commodity.api.services;

import ru.maxmorev.eshop.commodity.api.rest.response.CommodityDto;
import ru.maxmorev.eshop.commodity.api.rest.response.CommodityTypeDto;

import java.util.List;
import java.util.Optional;

public interface CommodityDtoService {

    List<CommodityDto> findWithBranchesAmountGt0();
    Optional<CommodityDto> findCommodityById(Long id);
    List<CommodityTypeDto> findAllTypes();
    List<CommodityDto> findWithBranchesAmountGt0AndType(String typeName);
    Optional<CommodityTypeDto> findTypeByName(String name);
}
