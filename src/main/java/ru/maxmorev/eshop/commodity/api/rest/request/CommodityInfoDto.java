package ru.maxmorev.eshop.commodity.api.rest.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.maxmorev.eshop.commodity.api.entities.CommodityImage;
import ru.maxmorev.eshop.commodity.api.entities.CommodityInfo;
import ru.maxmorev.eshop.commodity.api.rest.response.CommodityDto;
import ru.maxmorev.eshop.commodity.api.rest.response.CommodityTypeDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommodityInfoDto {
    protected static final ObjectMapper mapper = new ObjectMapper();
    @NotNull(message = "{branch.commodityId.notNull}")
    private Long id;
    @NotBlank(message = "{validation.commodity.name.NotBlank.message}")
    @Size(min=8, max=256, message = "{validation.commodity.name.size.message}")
    private String name;
    @NotBlank(message = "{validation.commodity.shortDescription.NotBlank.message}")
    @Size(min=16, max=256, message = "{validation.commodity.shortDescription.size.message}")
    private String shortDescription;
    @NotBlank(message = "{validation.commodity.overview.NotBlank.message}")
    @Size(min=64, max=2048, message = "{validation.commodity.overview.size.message}")
    private String overview;
    @PastOrPresent(message = "{validation.commodity.dateOfCreation.pastOrPresent}")
    private Date dateOfCreation;
    private CommodityTypeDto type;
    @Size(min=1, message = "{validation.commodity.images.size.message}")
    private List<String> images;

    public static CommodityInfoDto of(CommodityInfo c){
            return CommodityDto.builder()
                    .id(c.getId())
                    .name(jsonStr(c.getName()))
                    .shortDescription(jsonStr(c.getShortDescription()))
                    .overview(jsonStr(c.getOverview()))
                    .dateOfCreation(c.getDateOfCreation())
                    .type(CommodityTypeDto.of(c.getType()))
                    .images(c.getImages()
                            .stream()
                            .sorted()
                            .map(CommodityImage::getUri)
                            .collect(Collectors.toList()))
                    .build();
    }

    protected static String jsonStr(String s){
        return s.replace("'", "");
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
