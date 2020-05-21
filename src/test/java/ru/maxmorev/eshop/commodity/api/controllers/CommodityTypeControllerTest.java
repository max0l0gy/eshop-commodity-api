package ru.maxmorev.eshop.commodity.api.controllers;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.maxmorev.eshop.commodity.api.config.TestConfig;
import ru.maxmorev.eshop.commodity.api.entities.CommodityType;
import ru.maxmorev.eshop.commodity.api.rest.response.Message;
import ru.maxmorev.eshop.commodity.api.services.CommodityTypeService;


import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@DisplayName("Integration controller (CommodityTypeController) test")
@SpringBootTest(classes = TestConfig.class)
public class CommodityTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CommodityTypeService commodityTypeService;

    @Test
    @DisplayName("Should return commodity type info")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void getCommodityTypeTest() throws Exception {

        mockMvc.perform(get("/type/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("TypeTest")))
                .andExpect(jsonPath("$.description", is("test")));
    }

    @Test
    @DisplayName("Should delete commodity type by id")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void deleteCommodityTypeTest() throws Exception {

        mockMvc.perform(delete("/type/24"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.message", is("Success")));
    }

    @Test
    @DisplayName("Should expect error while deleting commodity type by id")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void deleteCommodityTypeTestError() throws Exception {

        mockMvc.perform(delete("/type/1"))
                .andDo(print())
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.message", is("Internal storage error")));
    }


    @Test
    @DisplayName("Should update commodity type by id")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void updateCommodityTypeTest() throws Exception {
        CommodityType type = commodityTypeService.findTypeById(24L).get();
        assertEquals("test delete", type.getDescription());
        type.setDescription("Type Update test controller");
        mockMvc.perform(put("/type/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(type.toString()))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(24)))
                .andExpect(jsonPath("$.description", is("Type Update test controller")));
    }

    @Test
    @DisplayName("Should expect error while update commodity type by id")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void updateCommodityTypeValidationTest() throws Exception {
        CommodityType type = commodityTypeService.findTypeById(24L).get();
        assertEquals("test delete", type.getDescription());
        type.setDescription("Type");
        mockMvc.perform(put("/type/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(type.toString()))
                .andDo(print())
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.message", is("Validation error")))
                .andExpect(jsonPath("$.errors[0].field", is("description")));
    }

    @Test
    @DisplayName("Should create new commodity type")
    public void createCommodityTypeTest() throws Exception {
        CommodityType type = new CommodityType();
        type.setName("Create Test");
        type.setDescription("Test Creation of type");
        mockMvc.perform(post("/type/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(type.toString()))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.status", is(Message.SUCCES)));

    }

    @Test
    @DisplayName("Should except Validation error while create new commodity type")
    public void createCommodityTypeTestValidationError() throws Exception {
        CommodityType type = new CommodityType();
        type.setName("CT");
        type.setDescription("TD");
        mockMvc.perform(post("/type/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(type.toString()))
                .andDo(print())
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.status", is(Message.ERROR)))
                .andExpect(jsonPath("$.message", is("Validation error")))
                .andExpect(jsonPath("$.errors").isArray());
    }

}
