package ru.maxmorev.eshop.commodity.api.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.maxmorev.eshop.commodity.api.rest.response.CommodityDto;
import ru.maxmorev.eshop.commodity.api.rest.response.CommodityTypeDto;
import ru.maxmorev.eshop.commodity.api.services.CommodityDtoService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/")
public class CommodityCustomerController {

    private final CommodityDtoService commodityDtoService;

    @RequestMapping(path = "branches/amount/gt/0", method = RequestMethod.GET)
    @ResponseBody
    List<CommodityDto> findWithBranchesAmountGt0() {
        return commodityDtoService.findWithBranchesAmountGt0();
    }

    @RequestMapping(path = "branches/amount/gt/0/type/name/{typeName}", method = RequestMethod.GET)
    @ResponseBody
    List<CommodityDto> findWithBranchesAmountGt0AndType(@PathVariable(name = "typeName") String typeName) {
        return commodityDtoService.findWithBranchesAmountGt0AndType(typeName);
    }

    @RequestMapping(path = "commodity/{id}", method = RequestMethod.GET)
    @ResponseBody
    CommodityDto findCommodityById(@PathVariable(name = "id") Long id) {
        return commodityDtoService
                .findCommodityById(id)
                .orElseThrow(() -> new IllegalArgumentException("commodity not found"));
    }

    @RequestMapping(path = "type/list", method = RequestMethod.GET)
    @ResponseBody
    List<CommodityTypeDto> findAllTypes() {
        return commodityDtoService.findAllTypes();
    }

    @RequestMapping(path = "type/name/{name}", method = RequestMethod.GET)
    @ResponseBody
    CommodityTypeDto findTypeByName(@PathVariable(name="name") String name) {
        return commodityDtoService
                .findTypeByName(name)
                .orElseThrow(() -> new IllegalArgumentException("type not found"));
    }

    @RequestMapping(path = "branches/amount/eq/0/type/name/{typeName}", method = RequestMethod.GET)
    @ResponseBody
    List<CommodityDto> findWithBranchesAmountEq0AndType(@PathVariable(name = "typeName") String typeName) {
        return commodityDtoService.findWithBranchesAmountEq0AndType(typeName);
    }

}
