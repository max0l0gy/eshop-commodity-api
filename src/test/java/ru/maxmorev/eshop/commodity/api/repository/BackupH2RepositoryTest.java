package ru.maxmorev.eshop.commodity.api.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import ru.maxmorev.eshop.commodity.api.entities.Commodity;

import java.util.List;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class BackupH2RepositoryTest {

    @Autowired
    private BackupH2Repository backupH2Repository;
    @Autowired
    private CommodityRepository commodityRepository;

    @Test
    @SqlGroup({
            @Sql(value = "classpath:db/commodity/test-data.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:db/commodity/clean-up.sql",
                    config = @SqlConfig(encoding = "utf-8", separator = ";", commentPrefix = "--"),
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
    })
    public void backupTest() {
        String fileName = backupH2Repository.createBackupScriptToFile();
        log.info("Backup to file {}", fileName);
        List<Commodity> commoditis = commodityRepository.findAll();
        Assertions.assertEquals(1, commoditis.size());
        backupH2Repository.restoreFromFile(fileName);
        commoditis = commodityRepository.findAll();
        Assertions.assertEquals(1, commoditis.size());
        Assertions.assertTrue(backupH2Repository.deleteFile(fileName));
    }


}
