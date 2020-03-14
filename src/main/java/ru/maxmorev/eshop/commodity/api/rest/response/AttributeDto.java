package ru.maxmorev.eshop.commodity.api.rest.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Value;
import ru.maxmorev.eshop.commodity.api.entities.CommodityBranchAttributeSet;

@Value
@Builder
public class AttributeDto {
    private String name;
    private String value;
    private String measure;

    public static AttributeDto of(CommodityBranchAttributeSet a) {
        return AttributeDto.builder()
                .name(a.getAttribute().getName())
                .value(a.getAttributeValue().getValue().toString())
                .measure(a.getAttribute().getMeasure())
                .build();
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
