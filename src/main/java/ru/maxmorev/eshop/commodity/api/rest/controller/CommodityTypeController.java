package ru.maxmorev.eshop.commodity.api.rest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.maxmorev.eshop.commodity.api.entities.CommodityType;
import ru.maxmorev.eshop.commodity.api.rest.response.Message;
import ru.maxmorev.eshop.commodity.api.services.CommodityTypeService;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommodityTypeController {

    private final CommodityTypeService commodityTypeService;
    private final MessageSource messageSource;

    @RequestMapping(path = "/types", method = RequestMethod.GET)
    @ResponseBody
    public List<CommodityType> getCommodityTypes() throws Exception {

        return commodityTypeService.findAllTypes();

    }

    @RequestMapping(path = "/type", method = RequestMethod.POST)
    @ResponseBody
    public Message createCommodityType(@RequestBody @Valid CommodityType type, Locale locale){
        log.info("type : {} ", type);
        commodityTypeService.addType(type);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }

    @RequestMapping(path = "/type", method = RequestMethod.PUT)
    @ResponseBody
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public CommodityType updateCommodityType(@RequestBody @Valid CommodityType type, Locale locale ){
        log.info("updateCommodityType {}", type);
        return commodityTypeService.updateType(type);
    }


    @RequestMapping(path = "/type/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Message deleteCommodityType(@PathVariable(name = "id", required = true) Long id, Locale locale){
        commodityTypeService.deleteTypeById(id);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }

    @RequestMapping(path = "/type/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommodityType getCommodityType(@PathVariable(name = "id", required = true) Long id, Locale locale){
        return commodityTypeService.findTypeById(id)
                .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage("commodity.type.error.id", new Object[]{id}, locale)));
    }

}
