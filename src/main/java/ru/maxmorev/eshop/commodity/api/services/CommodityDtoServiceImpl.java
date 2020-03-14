package ru.maxmorev.eshop.commodity.api.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.maxmorev.eshop.commodity.api.entities.Commodity;
import ru.maxmorev.eshop.commodity.api.repository.CommodityBranchRepository;
import ru.maxmorev.eshop.commodity.api.repository.CommodityRepository;
import ru.maxmorev.eshop.commodity.api.repository.CommodityTypeRepository;
import ru.maxmorev.eshop.commodity.api.rest.response.CommodityBranchDto;
import ru.maxmorev.eshop.commodity.api.rest.response.CommodityDto;
import ru.maxmorev.eshop.commodity.api.rest.response.CommodityTypeDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CommodityDtoServiceImpl implements CommodityDtoService {

    private final CommodityRepository commodityRepository;
    private final CommodityBranchRepository commodityBranchRepository;
    private final CommodityTypeRepository commodityTypeRepository;
    @Override
    public List<CommodityDto> findWithBranchesAmountGt0() {
        List<Commodity> cmList = commodityRepository
                .findCommodityWithBranchesWithAmountGt0();
        List<CommodityDto> cmDtoList = new ArrayList<>();
        cmList.forEach(c ->
                cmDtoList.add(CommodityDto.of(c, commodityBranchRepository
                        .findBranchesByIdWhereAmountGt0(c.getId())
                        .stream()
                        .map(CommodityBranchDto::of)
                        .collect(Collectors.toList())
                )));
        return cmDtoList;
    }

    @Override
    public Optional<CommodityDto> findCommodityById(Long id) {
        return commodityRepository
                .findById(id)
                .map(c -> CommodityDto.of(c, commodityBranchRepository
                        .findBranchesByIdWhereAmountGt0(c.getId())
                        .stream()
                        .map(CommodityBranchDto::of)
                        .collect(Collectors.toList())
                ));
    }

    @Override
    public List<CommodityTypeDto> findAllTypes() {
        return commodityTypeRepository
                .findAll()
                .stream()
                .map(CommodityTypeDto::of)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommodityDto> findWithBranchesAmountGt0AndType(String typeName) {
        List<Commodity> cmList = commodityRepository
                .findCommodityWithBranchesWithAmountGt0AndTypeName(typeName);
        List<CommodityDto> cmDtoList = new ArrayList<>();
        cmList.forEach(c ->
                cmDtoList.add(CommodityDto.of(c, commodityBranchRepository
                        .findBranchesByIdWhereAmountGt0(c.getId())
                        .stream()
                        .map(CommodityBranchDto::of)
                        .collect(Collectors.toList())
                )));
        return cmDtoList;
    }

    @Override
    public Optional<CommodityTypeDto> findTypeByName(String name) {
        return commodityTypeRepository
                .findByName(name)
                .map(CommodityTypeDto::of);
    }

}
