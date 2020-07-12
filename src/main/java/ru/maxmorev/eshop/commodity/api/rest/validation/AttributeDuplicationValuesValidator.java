package ru.maxmorev.eshop.commodity.api.rest.validation;

import ru.maxmorev.eshop.commodity.api.rest.response.AttributeDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;

public class AttributeDuplicationValuesValidator implements ConstraintValidator<AttributeDuplicationValues, List<AttributeDto>> {

    @Override
    public void initialize(AttributeDuplicationValues constraintAnnotation) {
        //do nothing
    }

    @Override
    public boolean isValid(List<AttributeDto> attributeDtoList, ConstraintValidatorContext constraintValidatorContext) {
        return attributeDtoList.stream()
                .filter(attributeDto -> attributeDto.getName()!=null)
                .collect(Collectors.groupingBy(AttributeDto::getName, Collectors.counting()))
                .values()
                .stream()
                .noneMatch(v -> v > 1);
    }


}
