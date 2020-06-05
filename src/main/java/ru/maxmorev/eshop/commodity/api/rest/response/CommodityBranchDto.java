package ru.maxmorev.eshop.commodity.api.rest.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import ru.maxmorev.eshop.commodity.api.entities.CommodityBranch;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class CommodityBranchDto {
    private Long id;
    private Long commodityId;
    private Integer amount;
    private Float price;
    private String currency;
    private List<AttributeDto> attributes;

    public static CommodityBranchDto of(CommodityBranch b){
        return CommodityBranchDto.builder()
                .id(b.getId())
                .commodityId(b.getCommodityId())
                .amount(b.getAmount())
                .price(b.getPrice())
                .currency(b.getCurrency().getCurrencyCode())
                .attributes(b.getAttributeSet()
                        .stream()
                        .map(AttributeDto::of)
                        .collect(Collectors.toList()))
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
