package ru.maxmorev.eshop.commodity.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.maxmorev.eshop.commodity.api.entities.CommodityBranchAttributeSet;

@Repository
public interface CommodityBranchPropertySetRepository extends CrudRepository<CommodityBranchAttributeSet, Long> {
}
