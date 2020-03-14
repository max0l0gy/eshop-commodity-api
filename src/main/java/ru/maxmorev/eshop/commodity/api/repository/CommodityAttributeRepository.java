package ru.maxmorev.eshop.commodity.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.maxmorev.eshop.commodity.api.entities.CommodityAttribute;
import ru.maxmorev.eshop.commodity.api.entities.CommodityType;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommodityAttributeRepository extends CrudRepository<CommodityAttribute, Long> {

    @Override
    List<CommodityAttribute> findAll();

    Optional<CommodityAttribute> findByNameAndCommodityType(String name, CommodityType type);

    List<CommodityAttribute> findByCommodityType(CommodityType type);

}
