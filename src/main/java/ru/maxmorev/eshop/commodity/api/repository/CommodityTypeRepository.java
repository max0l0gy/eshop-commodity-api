package ru.maxmorev.eshop.commodity.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.maxmorev.eshop.commodity.api.entities.CommodityType;

import java.util.List;
import java.util.Optional;

@Repository("commodityTypeRepository")
public interface CommodityTypeRepository extends CrudRepository<CommodityType, Long> {

    @Override
    List<CommodityType> findAll();

    Optional<CommodityType> findByName(String name);

}



