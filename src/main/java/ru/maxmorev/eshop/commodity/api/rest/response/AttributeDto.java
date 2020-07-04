package ru.maxmorev.eshop.commodity.api.rest.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;
import ru.maxmorev.eshop.commodity.api.entities.CommodityBranchAttributeSet;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AttributeDto {
    private String name;
    private String value;
    private String measure;

    public static AttributeDto of(CommodityBranchAttributeSet a) {
        return new AttributeDto(a.getAttribute().getName(),
                a.getAttributeValue().getValue().toString(), a.getAttribute().getMeasure());
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
