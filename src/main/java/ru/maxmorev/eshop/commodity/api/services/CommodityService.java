package ru.maxmorev.eshop.commodity.api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.maxmorev.eshop.commodity.api.entities.Commodity;
import ru.maxmorev.eshop.commodity.api.entities.CommodityAttribute;
import ru.maxmorev.eshop.commodity.api.entities.CommodityBranch;
import ru.maxmorev.eshop.commodity.api.rest.request.RequestAttributeValue;
import ru.maxmorev.eshop.commodity.api.rest.request.RequestCommodity;
import ru.maxmorev.eshop.commodity.api.entities.CommodityType;

import java.util.List;
import java.util.Optional;

public interface CommodityService {

    List<String> getAvailebleAttributeDataTypes();

    List<CommodityType> findAllTypes();

    void addType(CommodityType type); 

    CommodityType updateType(CommodityType type);

    Optional<CommodityType> findTypeById(Long id);

    Optional<CommodityType> findTypeByName(String name);

    void deleteTypeById(Long id);

    void addAttribute(RequestAttributeValue attribute);

    List<CommodityAttribute> findAttributesByTypeId(Long typeId);

    void deleteAttributeValueById(Long valueId);

    void addCommodity(RequestCommodity requestCommodity);

    void updateCommodity(RequestCommodity requestCommodity);

    List<CommodityBranch> findAllBranches();

    Optional<CommodityBranch> findBranchById(Long branchId);

    List<Commodity> findAllCommodities();

    Page<Commodity> findAllCommoditiesByPage(Pageable pageable);

    List<Commodity> findAllCommoditiesByTypeName(String typeName);

    Optional<Commodity> findCommodityById(Long id);

}
