package ru.maxmorev.eshop.commodity.api.rest.validation;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.maxmorev.eshop.commodity.api.entities.CommodityType;
import ru.maxmorev.eshop.commodity.api.repository.CommodityAttributeRepository;
import ru.maxmorev.eshop.commodity.api.repository.CommodityTypeRepository;
import ru.maxmorev.eshop.commodity.api.rest.request.RequestAttributeValue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
@RequiredArgsConstructor
public class CheckAttributeValueDuplicationForTypeValidator implements ConstraintValidator<CheckAttributeValueDuplicationForType, RequestAttributeValue> {

    private final CommodityTypeRepository commodityTypeRepository;
    private final CommodityAttributeRepository commodityAttributeRepository;

    @Override
    @SneakyThrows
    public boolean isValid(RequestAttributeValue value, ConstraintValidatorContext context) {
        CommodityType type = commodityTypeRepository.findById(value.getTypeId())
                .orElseThrow(() -> new NotFoundException("cant find type"));
        return  commodityAttributeRepository
                .findByNameAndCommodityType(value.getName(), type)
                .map(commodityAttribute -> commodityAttribute
                .getValues()
                .stream()
                .noneMatch(v -> v.getValue().toString().equals(value.getValue())))
                .orElse(true);
    }

    @Override
    public void initialize(CheckAttributeValueDuplicationForType constraintAnnotation) {
        //do nothing
    }
}
