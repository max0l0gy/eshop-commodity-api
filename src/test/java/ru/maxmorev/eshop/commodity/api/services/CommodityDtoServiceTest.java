package ru.maxmorev.eshop.commodity.api.services;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.maxmorev.eshop.commodity.api.entities.Commodity;
import ru.maxmorev.eshop.commodity.api.entities.CommodityBranch;
import ru.maxmorev.eshop.commodity.api.entities.CommodityBranchAttributeSet;
import ru.maxmorev.eshop.commodity.api.entities.CommodityImage;
import ru.maxmorev.eshop.commodity.api.repository.CommodityBranchRepository;
import ru.maxmorev.eshop.commodity.api.repository.CommodityRepository;
import ru.maxmorev.eshop.commodity.api.rest.request.RequestCommodity;
import ru.maxmorev.eshop.commodity.api.rest.response.CommodityBranchDto;
import ru.maxmorev.eshop.commodity.api.rest.response.CommodityDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
@Slf4j
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@DisplayName("Integration CommodityDtoService Test")
public class CommodityDtoServiceTest {

    @Autowired
    private CommodityDtoService commodityService;
    @Autowired
    private CommodityRepository commodityRepository;
    @Autowired
    private CommodityBranchRepository commodityBranchRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("should add commodity")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    @Transactional
    public void testAddCommodity() throws Exception {

        //void addCommodity(RequestCommodity requestCommodity);
        RequestCommodity rc = new RequestCommodity();
        rc.setAmount(1);
        rc.setPrice(3500f);
        rc.setName("Commodity1");
        rc.setOverview("Overview size <= 2048");
        rc.setShortDescription("Short Description");
        rc.setTypeId(1L);
        rc.setPropertyValues(Arrays.asList(3L));
        rc.setImages(ImmutableList.of("https://upload.wikimedia.org/wikipedia/commons/0/06/Coffee_Beans_Photographed_in_Macro.jpg"));
        rc.setCurrencyCode("EUR");
        commodityService.addCommodity(rc);
        em.flush();

        List<Commodity> commodities = commodityRepository.findAll();
        assertEquals(2, commodities.size());

        List<CommodityBranch> branches = commodityBranchRepository.findAll();
        assertEquals(2, branches.size());

        Commodity cm = commodities.get(0);
        assertEquals(1, cm.getImages().size());

    }

    @Test
    @DisplayName("should find all commodities")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void testFinaAllCommodities() throws Exception {
        List<Commodity> commodities = commodityRepository.findAll();
        assertEquals(1, commodities.size());
        log.info("commodities {}", commodities);
    }

    @Test
    @DisplayName("should update commodity")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    @Transactional
    public void testUpdateCommodity() throws Exception {
        //updateCommodity(RequestCommodity requestCommodity )
        RequestCommodity rc = new RequestCommodity();
        CommodityBranch branch = commodityBranchRepository.findById(5L).get();
        assertNotNull(branch);
        List<String> images = new ArrayList<>();
        for (CommodityImage image : branch.getCommodity().getImages()) {
            images.add(image.getUri());
        }
        rc.setImages(images);
        //update propertyValueId
        List<Long> values = Arrays.asList(9L);
        rc.setPropertyValues(values);
        rc.setTypeId(branch.getCommodity().getType().getId());
        rc.setShortDescription(branch.getCommodity().getShortDescription());
        rc.setOverview(branch.getCommodity().getOverview());
        rc.setName("BOMBER MA-1");//update name was t-shirt
        rc.setAmount(3);//was 5
        rc.setPrice(9000F);//was 3500f
        rc.setBranchId(branch.getId());//if present - update in controller
        commodityService.updateCommodity(rc);
        em.flush();

        CommodityBranch branchUpdate = commodityBranchRepository.findById(5L).get();
        assertEquals((long) rc.getAmount(), (long) branchUpdate.getAmount());
        assertEquals(rc.getPrice(), branchUpdate.getPrice(), 2);
        assertEquals(rc.getName(), branchUpdate.getCommodity().getName());
        List<CommodityBranchAttributeSet> list = Lists.newArrayList(branch.getAttributeSet());
        assertEquals((long) 9, (long) list.get(0).getAttributeValue().getId());

    }

    @Test
    @DisplayName("should find commodity by Type name")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void testFindCommoditiesByTypeName() throws Exception {
        List<CommodityDto> commodityList = commodityService.findAllCommoditiesByTypeName("TypeTest");
        assertTrue(commodityList.size() > 0);
    }

    @Test
    @DisplayName("should find commodity by ID")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void testFindCommodityById() throws Exception {
        Optional<CommodityDto> commodity = commodityService.findCommodityByIdWithBranchesAmountGt0(4L);
        assertTrue(commodity.isPresent());
    }

//    @Test
//    @DisplayName("should return all branches")
//    @SqlGroup({
//            @Sql(value = "classpath:db/commodity/test-data.sql",
//                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
//                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
//            @Sql(value = "classpath:db/commodity/clean-up.sql",
//                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
//                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
//    })
//    public void findAllBranches() {
//        List<CommodityBranch> branches = commodityService.findAllBranches();
//        assertTrue(branches.size() == 1);
//    }

    @Test
    @DisplayName("should find branche by ID")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void testFindBranchById() throws Exception {
        CommodityBranchDto branch = commodityService.findBranchById(5L).get();
        assertNotNull(branch);
    }
}
