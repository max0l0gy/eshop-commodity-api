package ru.maxmorev.eshop.commodity.api.services;

import lombok.extern.slf4j.Slf4j;
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
import ru.maxmorev.eshop.commodity.api.annotation.AttributeDataType;
import ru.maxmorev.eshop.commodity.api.entities.Commodity;
import ru.maxmorev.eshop.commodity.api.entities.CommodityAttribute;
import ru.maxmorev.eshop.commodity.api.entities.CommodityBranch;
import ru.maxmorev.eshop.commodity.api.entities.CommodityType;
import ru.maxmorev.eshop.commodity.api.repository.CommodityBranchRepository;
import ru.maxmorev.eshop.commodity.api.rest.request.RequestAttributeValue;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by maxim.morev on 11/08/19.
 */
@Slf4j
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@DisplayName("Integration CommodityService Test")
public class CommodityTypeServiceTest {

    @Autowired
    private CommodityTypeService commodityTypeService;
    @Autowired
    private CommodityBranchRepository commodityBranchRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("should return all types")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void findAllTypesTest() {
        List<CommodityType> types = commodityTypeService.findAllTypes();
        assertNotNull(types);
        assertEquals(2, types.size());
    }

    @Test
    @DisplayName("should add type")
    @Transactional
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void testAddType() throws Exception {
        CommodityType type = new CommodityType();
        type.setName("TestType");
        type.setDescription("Type description");

        commodityTypeService.addType(type);

        em.flush();

        List<CommodityType> types = commodityTypeService.findAllTypes();
        assertEquals(1, types.size());

    }

    @Test
    @DisplayName("should delete type")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    @Transactional
    public void testDeleteTypeById() throws Exception {
        List<CommodityType> types = commodityTypeService.findAllTypes();
        assertEquals(2, types.size());
        commodityTypeService.deleteTypeById(24L);
        em.flush();
        types = commodityTypeService.findAllTypes();
        assertEquals(1, types.size());
    }

    @Test(expected = PersistenceException.class)
    @DisplayName("should delete type exception")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    @Transactional
    public void testDeleteTypeByIdException() throws Exception {

        commodityTypeService.deleteTypeById(1L);
        em.flush();
        List<CommodityType> types = commodityTypeService.findAllTypes();
        assertTrue(types.isEmpty());
    }

    @Test
    @DisplayName("should return type by ID")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void testFindTypeById() throws Exception {
        Optional<CommodityType> result = commodityTypeService.findTypeById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("should return 2 attributes by type 1")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void testFindAttributeByTypeId() throws Exception {
        List<CommodityAttribute> properties = commodityTypeService.findAttributesByTypeId(1L);
        assertEquals(2, properties.size());
    }

    @Test
    @DisplayName("should remove attribute without value")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    @Transactional
    public void testDeleteAttributeValueById() throws Exception {
        List<CommodityAttribute> properties = commodityTypeService.findAttributesByTypeId(1L);
        assertEquals(2, properties.size());

        commodityTypeService.deleteAttributeValueById(9l);
        em.flush();
        /**
         * the method deleteAttributeValueById also delete property without value
         */
        properties = commodityTypeService.findAttributesByTypeId(1L);
        assertEquals(1, properties.size());

    }

    @Test
    @DisplayName("should add attribute for type id=1")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    @Transactional
    public void testAddAttribute() throws Exception {
        //void addProperty(RequestAttributeValue property);
        RequestAttributeValue requestPV = new RequestAttributeValue();
        requestPV.setName("color");
        requestPV.setTypeId(1L);
        requestPV.setValue("#ffffff");
        commodityTypeService.addAttribute(requestPV);
        em.flush();
        List<CommodityAttribute> properties = commodityTypeService.findAttributesByTypeId(1L);

        assertEquals(2, properties.size());

    }

    @Test
    @DisplayName("should throw exception while remove attribute value which is in branch")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    @Transactional
    public void testDeleteAttributeValueByIdError() throws Exception {
        //deletePropertyValueById(Long valueId)
        log.info("BEFORE OPERATION");
        List<CommodityBranch> branches = commodityBranchRepository.findAll();
        assertTrue(branches.size() == 1);

        CommodityBranch branch = branches.get(0);
        log.info("branch.getAttributeSet().size()={}", branch.getAttributeSet().size());
        assertTrue(branch.getAttributeSet().size() == 1);

        branch.getAttributeSet().forEach(as -> {
            log.info("attribute value is {}", as.getAttributeValue());
            assertTrue(as.getAttributeValue().getId() == 3l);
        });

        commodityTypeService.deleteAttributeValueById(3l);

        /**
         * the method deletePropertyValueById will try to delete property witch id used as FK in TABLE commodity_branch_property_set
         */
        assertThrows(javax.persistence.PersistenceException.class, em::flush);
    }

    @Test
    @DisplayName("should check Availeble Attribute Data Types")
    public void testGetAvailebleAttributeDataTypes() {

        assertEquals(AttributeDataType.values().length,
                commodityTypeService.getAvailebleAttributeDataTypes().size());
    }


}
