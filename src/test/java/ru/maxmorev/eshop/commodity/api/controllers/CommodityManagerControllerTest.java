package ru.maxmorev.eshop.commodity.api.controllers;

import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.maxmorev.eshop.commodity.api.TestUtil;
import ru.maxmorev.eshop.commodity.api.rest.response.Message;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class CommodityManagerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    @DisplayName("Should update commodity")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void updateCommodity() {
        mockMvc.perform(put("/api/manager/commodity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.getBody("requests/commodityUpdate.json")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(Message.SUCCES)));
    }

    @Test
    @SneakyThrows
    @DisplayName("Should update commodity branch")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void updateBranch() {
        mockMvc.perform(put("/api/manager/branch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.getBody("requests/commodityBranchUpdate.json")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(Message.SUCCES)));
    }

    @Test
    @SneakyThrows
    @DisplayName("Should expect validation error")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void updateCommodityValidationError() {
        mockMvc.perform(put("/api/manager/commodity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.getBody("requests/commodityUpdate.Validation.Error.json")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(Message.ERROR)))
                .andExpect(jsonPath("$.message", is("Validation error")))
                .andExpect(content().json(TestUtil.getBody("responses/commodityUpdate.Validation.Error.json")));
    }

    @Test
    @SneakyThrows
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void updateBranchValidationError() {
        mockMvc.perform(put("/api/manager/branch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.getBody("requests/commodityBranchUpdate.Validation.Error.json")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(Message.ERROR)))
                .andExpect(content().json(TestUtil.getBody("responses/commodityBranchUpdate.Validation.Error.json")));
    }

    @Test
    @SneakyThrows
    @DisplayName("Should update commodity branch")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void updateBranchValidationEmptyAttributesError() {
        mockMvc.perform(put("/api/manager/branch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.getBody("requests/commodityBranchUpdate.Validation.Empty.Attributes.Error.json")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(Message.ERROR)))
                .andExpect(content().json(TestUtil.getBody("responses/commodityBranchUpdate.Validation.Empty.Attributes.Error.json")));
    }

    @Test
    @SneakyThrows
    @DisplayName("Should update commodity branch")
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void updateBranchValidationAttributesDuplicationValuesError() {
        mockMvc.perform(put("/api/manager/branch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.getBody("requests/commodity-branch-update-validation-duplication-values.json")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(Message.ERROR)));
                //.andExpect(content().json(TestUtil.getBody("responses/commodityBranchUpdate.Validation.Empty.Attributes.Error.json")));
    }


    @Test
    @SneakyThrows
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void  getCommodityBranch() {
        mockMvc.perform(get("/api/manager/branch/5"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.commodityId", is(4)))
                .andExpect(jsonPath("$.amount", is(5)))
                .andExpect(jsonPath("$.price", is(3500.0)))
                .andExpect(jsonPath("$.currency", is("EUR")))
                .andExpect(jsonPath("$.attributes").isArray())

        ;
    }

}
