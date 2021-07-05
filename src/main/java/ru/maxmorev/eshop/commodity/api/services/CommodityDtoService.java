package ru.maxmorev.eshop.commodity.api.services;

import org.springframework.data.domain.Pageable;
import ru.maxmorev.eshop.commodity.api.rest.request.RequestAddCommodity;
import ru.maxmorev.eshop.commodity.api.rest.request.CommodityBranchDto;
import ru.maxmorev.eshop.commodity.api.rest.response.CommodityDto;
import ru.maxmorev.eshop.commodity.api.rest.response.CommodityGridDto;
import ru.maxmorev.eshop.commodity.api.rest.request.CommodityInfoDto;
import ru.maxmorev.eshop.commodity.api.rest.response.CommodityTypeDto;

import java.util.List;
import java.util.Optional;

public interface CommodityDtoService {

    List<CommodityDto> findWithBranchesAmountGt0();
    Optional<CommodityDto> findCommodityByIdWithBranchesAmountGt0(Long id);
    List<CommodityTypeDto> findAllTypes();
    List<CommodityDto> findWithBranchesAmountGt0AndType(String typeName);
    Optional<CommodityTypeDto> findTypeByName(String name);


    void addCommodity(RequestAddCommodity requestCommodity);
    void updateCommodity(CommodityInfoDto commodityInfoDto);
    void updateCommodityBranch(CommodityBranchDto requestCommodity);
    CommodityBranchDto addAmount(Long branchId, int amount);
    Optional<CommodityBranchDto> findBranchById(Long branchId);
    CommodityGridDto findAllCommoditiesByPage(Pageable pageable);
    List<CommodityDto> findAllCommoditiesByTypeName(String typeName);
    Optional<CommodityDto> findCommodityById(Long id);

    List<CommodityDto> findWithBranchesAmountEq0AndType(String typeName);

}
