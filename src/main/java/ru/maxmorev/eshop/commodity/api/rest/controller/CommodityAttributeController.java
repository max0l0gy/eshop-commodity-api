package ru.maxmorev.eshop.commodity.api.rest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.maxmorev.eshop.commodity.api.entities.CommodityAttribute;
import ru.maxmorev.eshop.commodity.api.rest.request.RequestAttributeValue;
import ru.maxmorev.eshop.commodity.api.rest.response.Message;
import ru.maxmorev.eshop.commodity.api.services.CommodityService;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
@Slf4j
@RestController
@RequiredArgsConstructor
public class CommodityAttributeController {

    private final CommodityService commodityService;
    private final MessageSource messageSource;

    @RequestMapping(path = "/attribute/", method = RequestMethod.POST)
    @ResponseBody
    public Message createAttribute(@RequestBody @Valid RequestAttributeValue property, Locale locale ){
        //to prevent duplicated properties
        commodityService.addAttribute(property);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }

    @RequestMapping(path = "/attributes/{typeId}", method = RequestMethod.GET)
    @ResponseBody
    public List<CommodityAttribute> getAttributes(@PathVariable(name = "typeId", required = true) Long typeId){
        return commodityService.findAttributesByTypeId(typeId);
    }

    @RequestMapping(path = "/attribute/value/dataTypes/", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getAvailebleAttributeDataTypes(){
        return commodityService.getAvailebleAttributeDataTypes();
    }

    @RequestMapping(path = "/attribute/value/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Message deletePropertyValue(@PathVariable(name = "id", required = true) Long valueId, Locale locale){
        commodityService.deleteAttributeValueById(valueId);
        return new Message(Message.SUCCES, messageSource.getMessage("message_success", new Object[]{}, locale));
    }



}
