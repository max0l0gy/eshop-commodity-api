package ru.maxmorev.eshop.commodity.api.rest.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.maxmorev.eshop.commodity.api.entities.CommodityImage;
import ru.maxmorev.eshop.commodity.api.entities.CommodityInfo;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommodityInfoDto {
    protected static final ObjectMapper mapper = new ObjectMapper();
    @NotNull
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String shortDescription;
    @NotEmpty
    private String overview;
    private Date dateOfCreation;
    private CommodityTypeDto type;
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
