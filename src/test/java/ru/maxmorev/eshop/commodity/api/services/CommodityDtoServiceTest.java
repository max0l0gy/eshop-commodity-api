package ru.maxmorev.eshop.commodity.api.services;

import com.google.common.collect.ImmutableList;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
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
import ru.maxmorev.eshop.commodity.api.rest.request.RequestAddCommodity;
import ru.maxmorev.eshop.commodity.api.rest.response.AttributeDto;
import ru.maxmorev.eshop.commodity.api.rest.response.CommodityBranchDto;
import ru.maxmorev.eshop.commodity.api.rest.response.CommodityDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
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
        RequestAddCommodity rc = new RequestAddCommodity();
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
    @SneakyThrows
    public void testUpdateCommodity() {
        CommodityDto cm = commodityService.findCommodityById(4l).get();
        assertEquals("t-shirt", cm.getName());
        assertEquals(1, cm.getImages().size());
        cm.setName("t-shirt-update");
        cm.setShortDescription("test description update");
        cm.setOverview("Overview t-shirt test update");
        cm.setImages(List.of(
                "https://i.pinimg.com/236x/e9/77/ab/e977abd9949dfafa25431f19dca0c2f6--john-connor-sarah-connor.jpg",
                "https://i.pinimg.com/564x/28/a0/df/28a0dfe3ccb2e0643753c123af6fbdcf.jpg"));
        commodityService.updateCommodity(cm);
        em.flush();
        cm = commodityService.findCommodityById(4l).get();
        assertEquals("t-shirt-update", cm.getName());
        assertEquals("test description update", cm.getShortDescription());
        assertEquals("Overview t-shirt test update", cm.getOverview());
        assertEquals(2, cm.getImages().size());
        assertEquals(
                "https://i.pinimg.com/236x/e9/77/ab/e977abd9949dfafa25431f19dca0c2f6--john-connor-sarah-connor.jpg",
                cm.getImages().get(0)
        );
        assertEquals(
                "https://i.pinimg.com/564x/28/a0/df/28a0dfe3ccb2e0643753c123af6fbdcf.jpg",
                cm.getImages().get(1)
        );
    }

    @Test
    @DisplayName("should update commodity branch")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    @Transactional
    @SneakyThrows
    public void testUpdateCommodityBranch() {
        CommodityBranchDto branch = commodityService.findBranchById(5l).get();
        Assertions.assertEquals(3500.0f, branch.getPrice(), "expected price 3500");
        Assertions.assertEquals("EUR", branch.getCurrency(), "expected currency is EUR");
        Assertions.assertEquals(5, branch.getAmount().intValue(), "amount = 5");
        Assertions.assertEquals(1, branch.getAttributes().size(), "Attributes size 1");
        Assertions.assertTrue(branch.getAttributes().stream().anyMatch(a -> a.getName().equals("size") && a.getValue().equals("s")), "Attribute name 'size' value is 's' ");
        branch.setAmount(1);
        branch.setCurrency("USD");
        branch.setPrice(50.0f);
        branch.setAttributes(List.of(
                AttributeDto.builder()
                        .name("size")
                        .value("m").build(),
                AttributeDto.builder()
                        .name("color")
                        .value("#00fc12").build()
                ));
        commodityService.updateCommodityBranch(branch);
        em.flush();
        branch = commodityService.findBranchById(5l).get();
        Assertions.assertEquals(50.0f, branch.getPrice(), "new price 50");
        Assertions.assertEquals("USD", branch.getCurrency(), "new currency is EUR");
        Assertions.assertEquals(1, branch.getAmount().intValue(), "new amount = 1");
        Assertions.assertEquals(2, branch.getAttributes().size(), "Attributes size 2");
        Assertions.assertTrue(branch.getAttributes().stream().anyMatch(a -> a.getName().equals("size") && a.getValue().equals("m")), "Attribute name 'size' new value is 'm' ");
        Assertions.assertTrue(branch.getAttributes().stream().anyMatch(a -> a.getName().equals("color") && a.getValue().equals("#00fc12")), "New Attribute name 'color' value is '#00fc12' ");
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
