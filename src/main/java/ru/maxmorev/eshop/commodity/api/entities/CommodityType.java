package ru.maxmorev.eshop.commodity.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "commodity_type")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommodityType {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR_COMMODITY_TYPE)
    @Column(updatable = false)
    protected Long id;

    @Version
    @Column(name = "VERSION")
    private int version;

    @NotBlank(message = "{validation.CommodityType.name.NotBlank.message}")
    @Column(unique = true, updatable = false)
    private String name;

    @NotBlank(message = "{validation.CommodityType.description.NotBlank.message}")
    @Size(min = 8, max = 128, message = "{validation.CommodityType.description.size.message}")
    @Column(nullable = false, length = 128)
    private String description;

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof CommodityType)) return false;
        CommodityType that = (CommodityType) object;
        return Objects.equals(getId(), that.getId()) &&
                version == that.version &&
                getName().equals(that.getName()) &&
                getDescription().equals(that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), version, getName(), getDescription());
    }
}

