package ru.maxmorev.eshop.commodity.api.rest.controller;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.maxmorev.eshop.commodity.api.rest.request.RequestAddCommodity;
import ru.maxmorev.eshop.commodity.api.rest.response.CommodityBranchDto;
import ru.maxmorev.eshop.commodity.api.rest.response.CommodityDto;
import ru.maxmorev.eshop.commodity.api.rest.response.CommodityGridDto;
import ru.maxmorev.eshop.commodity.api.rest.response.CommodityInfoDto;
import ru.maxmorev.eshop.commodity.api.rest.response.Message;
import ru.maxmorev.eshop.commodity.api.services.CommodityDtoService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Locale;
import java.util.Objects;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/manager/")
public class CommodityManagmentController {

    private final CommodityDtoService commodityService;
    private final MessageSource messageSource;

    @RequestMapping(path = "commodity/", method = RequestMethod.POST)
    @ResponseBody
    public Message createCommodityFromRequset(@RequestBody @Valid RequestAddCommodity requestCommodity, Locale locale) {
        commodityService.addCommodity(requestCommodity);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }

    @RequestMapping(path = "commodity/", method = RequestMethod.PUT)
    @ResponseBody
    public Message updateCommodity(@RequestBody @Valid CommodityInfoDto cmInfo, Locale locale) {
        commodityService.updateCommodity(cmInfo);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }

    @RequestMapping(path = "commodity/id/{id}/any/branches", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public CommodityDto getCommodityAnyBranches(@PathVariable(name = "id", required = true) Long id, Locale locale) throws Exception {
        return commodityService.findCommodityById(id)
                .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage("commodity.error.id", new Object[]{id}, locale)));

    }

    @RequestMapping(path = "commodity/list", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public CommodityGridDto listCommodity(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "rows", required = false) Integer rows,
            @RequestParam(value = "sort", required = false) String sortBy,
            @RequestParam(value = "order", required = false) String order
    ) {
        // Process order by
        Sort sort = null;
        String orderBy = sortBy;
        if (orderBy != null && orderBy.equals("dateOfCreation")) {
            orderBy = "dateOfCreation";
        } else {
            orderBy = "id";
        }
        if (Objects.isNull(order)) {
            order = "desc";
        }
        if (Objects.isNull(page)) {
            page = 1;
        }
        if (Objects.isNull(rows)) {
            rows = 10;
        }

        if (orderBy != null && order != null) {
            if (order.equals("desc")) {
                sort = Sort.by(Sort.Direction.DESC, orderBy);
            } else
                sort = Sort.by(Sort.Direction.ASC, orderBy);
        }

        // Constructs page request for current page
        // Note: page number for Spring Data JPA starts with 0, while jqGrid starts with 1
        PageRequest pageRequest = null;

        if (sort != null) {
            pageRequest = PageRequest.of(page - 1, rows, sort);
        } else {
            pageRequest = PageRequest.of(page - 1, rows);
        }
        // Construct the grid data that will return as JSON data
        return commodityService.findAllCommoditiesByPage(pageRequest);
    }

    @RequestMapping(path = "branch/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommodityBranchDto getCommodityBranch(@PathVariable(name = "id") Long branchId, Locale locale) {
        return commodityService
                .findBranchById(branchId)
                .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage("commodity.branch.error.id", new Object[]{branchId}, locale)));

    }

    @RequestMapping(path = "branch/{id}/amount/inc/{amount}", method = RequestMethod.PUT)
    @ResponseBody
    public CommodityBranchDto addAmountToBranch(@PathVariable(name = "id", required = true) @NotNull Long branchId,
                                             @PathVariable(name = "amount", required = true) @NotNull Integer amount,
                                             Locale locale) {
        log.info("addAmountToBranch id={}, amount={}", branchId, amount);
        return commodityService.addAmount(branchId, amount);
    }

    @PutMapping(path = "branch")
    @ResponseBody
    @SneakyThrows
    public Message updateBranch(@RequestBody @Valid CommodityBranchDto branchDto,
                                Locale locale) {
        log.info("update branch id={}, branch={}", branchDto.getId(), branchDto);
        commodityService.updateCommodityBranch(branchDto);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }

}
