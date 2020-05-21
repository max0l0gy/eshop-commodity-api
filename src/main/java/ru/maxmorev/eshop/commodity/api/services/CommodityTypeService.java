package ru.maxmorev.eshop.commodity.api.services;

import ru.maxmorev.eshop.commodity.api.entities.CommodityAttribute;
import ru.maxmorev.eshop.commodity.api.entities.CommodityType;
import ru.maxmorev.eshop.commodity.api.rest.request.RequestAttributeValue;

import java.util.List;
import java.util.Optional;

public interface CommodityTypeService {

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

}
