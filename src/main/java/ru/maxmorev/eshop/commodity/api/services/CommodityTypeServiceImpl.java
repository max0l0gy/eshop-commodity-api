package ru.maxmorev.eshop.commodity.api.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maxmorev.eshop.commodity.api.annotation.AttributeDataType;
import ru.maxmorev.eshop.commodity.api.entities.CommodityAttribute;
import ru.maxmorev.eshop.commodity.api.entities.CommodityAttributeValue;
import ru.maxmorev.eshop.commodity.api.entities.CommodityType;
import ru.maxmorev.eshop.commodity.api.repository.CommodityAttributeRepository;
import ru.maxmorev.eshop.commodity.api.repository.CommodityAttributeValueRepository;
import ru.maxmorev.eshop.commodity.api.repository.CommodityTypeRepository;
import ru.maxmorev.eshop.commodity.api.rest.request.RequestAttributeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@Service("commodityService")
@RequiredArgsConstructor
public class CommodityTypeServiceImpl implements CommodityTypeService {

    private final CommodityTypeRepository commodityTypeRepository;
    private final CommodityAttributeRepository commodityAttributeRepository;
    private final CommodityAttributeValueRepository commodityAttributeValueRepository;

    @Override
    public List<String> getAvailebleAttributeDataTypes() {
        List<String> types = new ArrayList<>();
        for (AttributeDataType dt : AttributeDataType.values()) {
            types.add(dt.name());
        }
        return types;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommodityType> findAllTypes() {
        return commodityTypeRepository.findAll();
    }

    @Override
    public void addType(CommodityType type) {
        commodityTypeRepository.save(type);
    }

    @Override
    public CommodityType updateType(CommodityType type) {
        return commodityTypeRepository
                .findById(type.getId())
                .map(t -> {
                    t.setName(type.getName());
                    t.setDescription(type.getDescription());
                    return commodityTypeRepository.save(t);
                })
                .orElseThrow(() -> new IllegalArgumentException("Type not found type.id=" + type.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommodityType> findTypeById(Long id) {
        return commodityTypeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommodityType> findTypeByName(String name) {
        return commodityTypeRepository.findByName(name);
    }

    @Override
    public void deleteTypeById(Long id) {
        commodityTypeRepository.deleteById(id);
    }

    private void createValue(CommodityAttribute prop, String valString) {
        CommodityAttributeValue newValue = new CommodityAttributeValue(prop);
        Object val = newValue.createValueFrom(valString);
        newValue.setAttribute(prop);
        newValue.setValue(val);
        commodityAttributeValueRepository.save(newValue);
    }

    @Override
    public void addAttribute(RequestAttributeValue attribute) {
        log.info("Property : {}", attribute);
        Optional<CommodityType> type = commodityTypeRepository.findById(attribute.getTypeId());
        if (!type.isPresent()) {

            throw new IllegalArgumentException("Type not found type.id=" + attribute.getTypeId());

        }
        Optional<CommodityAttribute> propertyOptional = commodityAttributeRepository.findByNameAndCommodityType(attribute.getName(), type.get());
        if (propertyOptional.isPresent()) {
            //check: if value exist? true: do nothing esle create new value
            CommodityAttribute existProperty = propertyOptional.get();
            CommodityAttributeValue newValue = new CommodityAttributeValue(existProperty);
            Object val = newValue.createValueFrom(attribute.getValue());
            if (existProperty.getValues().stream().noneMatch(v -> v.getValue().equals(val))) {
                //create value
                createValue(existProperty, attribute.getValue());
            }

        } else {
            CommodityAttribute newProperty = new CommodityAttribute();
            newProperty.setDataType(AttributeDataType.valueOf(attribute.getDataType()));
            newProperty.setName(attribute.getName());
            newProperty.setCommodityType(type.get());
            newProperty.setMeasure(attribute.getMeasure());
            commodityAttributeRepository.save(newProperty);
            createValue(newProperty, attribute.getValue());
        }

    }

    @Override
    @Transactional(readOnly = true)
    public List<CommodityAttribute> findAttributesByTypeId(Long typeId) {
        return commodityAttributeRepository.findByCommodityType(commodityTypeRepository.findById(typeId).get());
    }

    @Override
    public void deleteAttributeValueById(Long valueId) {
        Optional<CommodityAttributeValue> av =
                commodityAttributeValueRepository.findById(valueId);
        if (av.isPresent()) {
            CommodityAttribute ca = av.get().getAttribute();

            log.info("remove value {} : {}", av.get().getValue(), ca.getValues().remove(av.get()));

            if (ca.getValues().isEmpty()) {
                //delete empty property
                commodityAttributeRepository.delete(ca);
                return;
            }
            commodityAttributeRepository.save(ca);
        }

    }



}
