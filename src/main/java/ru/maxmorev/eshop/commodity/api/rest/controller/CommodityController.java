package ru.maxmorev.eshop.commodity.api.rest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.maxmorev.eshop.commodity.api.entities.Commodity;
import ru.maxmorev.eshop.commodity.api.entities.CommodityBranch;
import ru.maxmorev.eshop.commodity.api.rest.request.RequestCommodity;
import ru.maxmorev.eshop.commodity.api.rest.response.Message;
import ru.maxmorev.eshop.commodity.api.rest.response.CommodityGrid;
import ru.maxmorev.eshop.commodity.api.services.CommodityService;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Slf4j
@RestController
public class CommodityController {

    private CommodityService commodityService;
    private MessageSource messageSource;

    @Autowired
    public void setCommodityService(CommodityService commodityService) {
        this.commodityService = commodityService;
    }
    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @RequestMapping(path = "/commodity/", method=RequestMethod.POST)
    @ResponseBody
    public Message createCommodityFromRequset(@RequestBody @Valid RequestCommodity requestCommodity, Locale locale){
        log.info("POST -> createCommodityFromRequset");
        commodityService.addCommodity(requestCommodity);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }

    @RequestMapping(path = "/commodity/", method = RequestMethod.PUT)
    @ResponseBody
    public Message updateCommodity(@RequestBody @Valid RequestCommodity requestCommodity, Locale locale){
        log.info("PUT -> updateCommodity");
        commodityService.updateCommodity(requestCommodity);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }

    @RequestMapping(path = "/commodity/id/{id}", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Commodity getCommodity(@PathVariable(name="id", required = true) Long id, Locale locale ) throws Exception {
        return commodityService.findCommodityById(id)
                .orElseThrow(()->new IllegalArgumentException(messageSource.getMessage("commodity.error.id", new Object[]{id}, locale)));

    }

    @RequestMapping(path = "/commodities/", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public CommodityGrid listCommodity(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "rows", required = false) Integer rows,
            @RequestParam(value = "sort", required = false) String sortBy,
            @RequestParam(value = "order", required = false) String order
    ) throws Exception {
        // Process order by
        Sort sort = null;
        String orderBy = sortBy;
        if (orderBy != null && orderBy.equals("dateOfCreation")) {
            orderBy = "dateOfCreation";
        }else{
            orderBy = "id";
        }
        if(Objects.isNull(order)){
            order = "desc";
        }
        if(Objects.isNull(page)){
            page = 1;
        }
        if(Objects.isNull(rows)){
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
            pageRequest =  PageRequest.of(page - 1, rows, sort);
        } else {
            pageRequest = PageRequest.of(page - 1, rows);
        }
        // Construct the grid data that will return as JSON data
        return new CommodityGrid( commodityService.findAllCommoditiesByPage(pageRequest) );
    }

    @RequestMapping( path = "/branches/", method = RequestMethod.GET)
    @ResponseBody
    public List<CommodityBranch> getBranches() throws Exception {
        return commodityService.findAllBranches();
    }

    @RequestMapping(path = "/branch/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommodityBranch getCommodityBranch(@PathVariable(name="id", required = true) Long branchId, Locale locale ) throws Exception {
        return commodityService
                .findBranchById(branchId)
                .orElseThrow(()->new IllegalArgumentException(messageSource.getMessage("commodity.branch.error.id", new Object[]{branchId}, locale)));

    }




}
