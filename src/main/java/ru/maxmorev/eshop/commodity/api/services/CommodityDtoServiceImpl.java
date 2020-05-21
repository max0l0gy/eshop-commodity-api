package ru.maxmorev.eshop.commodity.api.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.maxmorev.eshop.commodity.api.entities.Commodity;
import ru.maxmorev.eshop.commodity.api.entities.CommodityAttributeValue;
import ru.maxmorev.eshop.commodity.api.entities.CommodityBranch;
import ru.maxmorev.eshop.commodity.api.entities.CommodityBranchAttributeSet;
import ru.maxmorev.eshop.commodity.api.entities.CommodityImage;
import ru.maxmorev.eshop.commodity.api.entities.CommodityType;
import ru.maxmorev.eshop.commodity.api.repository.CommodityAttributeValueRepository;
import ru.maxmorev.eshop.commodity.api.repository.CommodityBranchRepository;
import ru.maxmorev.eshop.commodity.api.repository.CommodityRepository;
import ru.maxmorev.eshop.commodity.api.repository.CommodityTypeRepository;
import ru.maxmorev.eshop.commodity.api.rest.request.RequestCommodity;
import ru.maxmorev.eshop.commodity.api.rest.response.CommodityBranchDto;
import ru.maxmorev.eshop.commodity.api.rest.response.CommodityDto;
import ru.maxmorev.eshop.commodity.api.rest.response.CommodityGrid;
import ru.maxmorev.eshop.commodity.api.rest.response.CommodityGridDto;
import ru.maxmorev.eshop.commodity.api.rest.response.CommodityTypeDto;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CommodityDtoServiceImpl implements CommodityDtoService {

    private final CommodityRepository commodityRepository;
    private final CommodityBranchRepository commodityBranchRepository;
    private final CommodityTypeRepository commodityTypeRepository;
    private CommodityAttributeValueRepository commodityAttributeValueRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CommodityDto> findWithBranchesAmountGt0() {
        List<Commodity> cmList = commodityRepository
                .findCommodityWithBranchesWithAmountGt0();
        List<CommodityDto> cmDtoList = new ArrayList<>();
        cmList.forEach(c ->
                cmDtoList.add(CommodityDto.of(c, commodityBranchRepository
                        .findBranchesByIdWhereAmountGt0(c.getId())
                        .stream()
                        .map(CommodityBranchDto::of)
                        .collect(Collectors.toList())
                )));
        return cmDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommodityDto> findCommodityByIdWithBranchesAmountGt0(Long id) {
        return commodityRepository
                .findById(id)
                .map(c -> CommodityDto.of(c, commodityBranchRepository
                        .findBranchesByIdWhereAmountGt0(c.getId())
                        .stream()
                        .map(CommodityBranchDto::of)
                        .collect(Collectors.toList())
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommodityTypeDto> findAllTypes() {
        return commodityTypeRepository
                .findAll()
                .stream()
                .map(CommodityTypeDto::of)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommodityDto> findWithBranchesAmountGt0AndType(String typeName) {
        List<Commodity> cmList = commodityRepository
                .findCommodityWithBranchesWithAmountGt0AndTypeName(typeName);
        List<CommodityDto> cmDtoList = new ArrayList<>();
        cmList.forEach(c ->
                cmDtoList.add(CommodityDto.of(c, commodityBranchRepository
                        .findBranchesByIdWhereAmountGt0(c.getId())
                        .stream()
                        .map(CommodityBranchDto::of)
                        .collect(Collectors.toList())
                )));
        return cmDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommodityTypeDto> findTypeByName(String name) {
        return commodityTypeRepository
                .findByName(name)
                .map(CommodityTypeDto::of);
    }

    private List<CommodityImage> createImageListOf(Commodity commodity, RequestCommodity requestCommodity) {
        List<CommodityImage> commodityImages = new ArrayList<>(requestCommodity.getImages().size());
        Short imageIndex = 0;
        for (String imageUrl : requestCommodity.getImages()) {
            CommodityImage image = new CommodityImage();
            image.setImageOrder(imageIndex);
            image.setUri(imageUrl);
            image.setCommodity(commodity);
            //commodityImageRepository.save( image );
            commodityImages.add(image);
            imageIndex++;
        }
        return commodityImages;
    }

    private Commodity createCommodityFromRequest(RequestCommodity requestCommodity) {
        CommodityType commodityType;
        Optional<CommodityType> commodityTypeExist = commodityTypeRepository.findById(requestCommodity.getTypeId());

        if (commodityTypeExist.isPresent()) {
            commodityType = commodityTypeExist.get();
        } else {
            throw new IllegalArgumentException("Illegal argument typeId=" + requestCommodity.getTypeId());
        }

        Commodity commodity = new Commodity();
        commodity.setName(requestCommodity.getName());
        commodity.setShortDescription(requestCommodity.getShortDescription());
        commodity.setOverview(requestCommodity.getOverview());
        commodity.setType(commodityType);
        //commodityRepository.save( commodity );
        commodity.getImages().addAll(createImageListOf(commodity, requestCommodity));
        //create images of commodity
        return commodity;
    }

    private void createCommodityBranch(RequestCommodity requestCommodity, Commodity commodity) {
        //create branch
        CommodityBranch commodityBranch = new CommodityBranch();
        commodityBranch.setAmount(requestCommodity.getAmount());
        commodityBranch.setPrice(requestCommodity.getPrice());
        commodityBranch.setCommodity(commodity);
        commodityBranch.setCurrency(Currency.getInstance(requestCommodity.getCurrencyCode()));
        //commodityBranchRepository.save( commodityBranch );
        createBranchPropertySet(requestCommodity.getPropertyValues(), commodityBranch);
        commodity.getBranches().add(commodityBranch);
    }

    private boolean createPropertySet(CommodityBranchAttributeSet propertySet, Long valId) {
        Optional<CommodityAttributeValue> commodityPropertyValueExist = commodityAttributeValueRepository.findById(valId);
        if (commodityPropertyValueExist.isPresent()) {
            CommodityAttributeValue commodityAttributeValue = commodityPropertyValueExist.get();
            propertySet.setAttribute(commodityAttributeValue.getAttribute());
            propertySet.setAttributeValue(commodityAttributeValue);
            //commodityBranchPropertySetRepository.save(propertySet);
            return true;
        }
        return false;
    }

    private void createBranchPropertySet(List<Long> valueIdList, CommodityBranch commodityBranch) {
        //create branch properies
        for (Long propertyValueId : valueIdList) {
            CommodityBranchAttributeSet propertySet = new CommodityBranchAttributeSet();
            propertySet.setBranch(commodityBranch);
            if (createPropertySet(propertySet, propertyValueId)) {
                commodityBranch.getAttributeSet().add(propertySet);
            }
        }
    }


    /**
     * @param requestCommodity
     */
    @Override
    public void addCommodity(RequestCommodity requestCommodity) {
        Optional<Commodity> commodityExist = commodityRepository.findByNameAndType(requestCommodity.getName(), commodityTypeRepository.findById(requestCommodity.getTypeId()).get());
        if (commodityExist.isPresent()) {
            //create new branch for existent commodity
            createCommodityBranch(requestCommodity, commodityExist.get());
            commodityRepository.save(commodityExist.get());

        } else {
            //create new commodity and dependent classes
            Commodity commodity = createCommodityFromRequest(requestCommodity);
            createCommodityBranch(requestCommodity, commodity);
            commodityRepository.save(commodity);
        }
    }

    @Override
    public void updateCommodity(RequestCommodity requestCommodity) {
        commodityBranchRepository.findById(requestCommodity.getBranchId()).ifPresent(branch -> {

            List<CommodityBranchAttributeSet> propertySetList = new ArrayList<>(branch.getAttributeSet());

            if (requestCommodity.getPropertyValues().size() > 0) {
                //updateBranch = true;
                propertySetList.forEach(set -> branch.getAttributeSet().remove(set));

                //TODO change attributeSet think bout refactorin
                List<Long> valueIdList = requestCommodity.getPropertyValues();
                for (Long propertyValueId : valueIdList) {
                    Optional<CommodityAttributeValue> commodityPropertyValueExist = commodityAttributeValueRepository.findById(propertyValueId);
                    commodityPropertyValueExist.ifPresent(commodityAttributeValue -> {
                                CommodityBranchAttributeSet newAttribute = new CommodityBranchAttributeSet();
                                newAttribute.setBranch(branch);
                                newAttribute.setAttribute(commodityAttributeValue.getAttribute());
                                newAttribute.setAttributeValue(commodityAttributeValue);
                                branch.getAttributeSet().add(newAttribute);
                            }

                    );

                }

            }
            branch.setAmount(requestCommodity.getAmount());
            branch.setPrice(requestCommodity.getPrice());
            Commodity commodity = branch.getCommodity();
            commodity.setShortDescription(requestCommodity.getShortDescription());
            commodity.setOverview(requestCommodity.getOverview());
            commodity.setName(requestCommodity.getName());
            commodity.getImages().forEach(commodityImage -> commodityImage.setCommodity(null));
            commodity.getImages().clear();
            commodity.getImages().addAll(createImageListOf(commodity, requestCommodity));
            //commodityBranchRepository.save(branch);
            commodityRepository.save(commodity);

        });
    }

    @Override
    public CommodityBranchDto addAmount(Long branchId, int amount) {
        CommodityBranch branch = commodityBranchRepository.findById(branchId)
                .orElseThrow(() -> new IllegalArgumentException("Branch not found"));
        if (branch.getAmount().intValue() + amount < 0) {
            throw new IllegalArgumentException("Branch amount can not be less then zero");
        }
        branch.setAmount(branch.getAmount().intValue() + amount);
        return CommodityBranchDto.of(commodityBranchRepository.save(branch));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommodityBranchDto> findBranchById(Long branchId) {
        return commodityBranchRepository.findById(branchId).map(CommodityBranchDto::of);
    }


    @Override
    @Transactional(readOnly = true)
    public CommodityGridDto findAllCommoditiesByPage(Pageable pageable) {
        return CommodityGridDto.of( new CommodityGrid(commodityRepository.findAll(pageable)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommodityDto> findAllCommoditiesByTypeName(String typeName) {

        Optional<CommodityType> typeExist = commodityTypeRepository.findByName(typeName);
        if (typeExist.isPresent()) {
            return commodityRepository.findByType(typeExist.get())
                    .stream()
                    .map(c->CommodityDto.of(c, c.getBranches()
                            .stream()
                            .map(CommodityBranchDto::of)
                            .collect(Collectors.toList())))
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Optional<CommodityDto> findCommodityById(Long id) {
        return commodityRepository
                .findById(id)
                .map(c -> CommodityDto.of(c, c.getBranches()
                        .stream()
                        .map(CommodityBranchDto::of)
                        .collect(Collectors.toList()))
                );
    }

}
