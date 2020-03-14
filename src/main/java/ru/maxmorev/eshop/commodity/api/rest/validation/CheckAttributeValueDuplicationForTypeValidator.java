package ru.maxmorev.eshop.commodity.api.rest.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.maxmorev.eshop.commodity.api.rest.request.RequestAttributeValue;
import ru.maxmorev.eshop.commodity.api.entities.CommodityAttribute;
import ru.maxmorev.eshop.commodity.api.entities.CommodityType;
import ru.maxmorev.eshop.commodity.api.repository.CommodityAttributeRepository;
import ru.maxmorev.eshop.commodity.api.repository.CommodityTypeRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.Optional;

public class CheckAttributeValueDuplicationForTypeValidator implements ConstraintValidator<CheckAttributeValueDuplicationForType, RequestAttributeValue> {

    private static final Logger logger = LoggerFactory.getLogger(CheckAttributeValueDuplicationForTypeValidator.class);
    private CommodityTypeRepository commodityTypeRepository;
    private CommodityAttributeRepository commodityAttributeRepository;

    @Autowired public void setCommodityTypeRepository(CommodityTypeRepository commodityTypeRepository) {
        this.commodityTypeRepository = commodityTypeRepository;
    }

    @Autowired public void setCommodityAttributeRepository(CommodityAttributeRepository commodityAttributeRepository) {
        this.commodityAttributeRepository = commodityAttributeRepository;
    }

    @Override
    public boolean isValid(RequestAttributeValue value, ConstraintValidatorContext context) {
        CommodityType type = commodityTypeRepository.findById(value.getTypeId()).get();
        logger.info("CHECK TYPE attributes: " + type);
        if(!Objects.isNull(type)){
            Optional<CommodityAttribute> ca = commodityAttributeRepository.findByNameAndCommodityType(value.getName(), type);
            if(ca.isPresent()){
                CommodityAttribute attribute = ca.get();
                if(attribute.getValues().stream().anyMatch(v->v.getValue().toString().equals(value.getValue()))){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void initialize(CheckAttributeValueDuplicationForType constraintAnnotation) {
        //do nothing
    }
}
