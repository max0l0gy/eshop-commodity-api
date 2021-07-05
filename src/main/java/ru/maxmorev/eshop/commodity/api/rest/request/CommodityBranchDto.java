package ru.maxmorev.eshop.commodity.api.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.maxmorev.eshop.commodity.api.entities.CommodityBranch;
import ru.maxmorev.eshop.commodity.api.rest.response.AttributeDto;
import ru.maxmorev.eshop.commodity.api.rest.validation.AttributeDuplicationValues;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommodityBranchDto {
    protected static final ObjectMapper mapper = new ObjectMapper();
    @NotNull(message = "{branch.id.notNull}")
    private Long id;
    @NotNull(message = "{branch.commodityId.notNull}")
    private Long commodityId;
    @NotNull(message = "{validation.commodity.amount.NotNull.message}")
    @Min(value = 0, message = "{validation.commodity.amount.min.message}")
    private Integer amount;
    @NotNull(message = "{validation.commodity.price.NotNull.message}")
    @Positive(message = "{validation.commodity.price.gt.zero}")
    private Float price;
    private String currency;
    @NotNull(message = "{validation.commodity.propertyValues.NotNull.message}")
    @Size(min=1, message = "{validation.commodity.propertyValues.size.message}")
    @AttributeDuplicationValues(message = "{validation.commodity.attribute.duplication.values}")
    private List<@Valid AttributeDto> attributes;

    @JsonIgnore
    @AssertTrue(message = "{validation.commodity.currencyCode.exist}")
    public boolean isValidCurrencyCode() {
        if (Currency.getAvailableCurrencies().stream().anyMatch(c -> c.getCurrencyCode().equals(currency))) {
            return true;
        }
        return false;
    }

    public static CommodityBranchDto of(CommodityBranch b) {
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
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

}
