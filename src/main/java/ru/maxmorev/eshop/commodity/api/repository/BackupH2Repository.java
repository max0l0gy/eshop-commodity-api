package ru.maxmorev.eshop.commodity.api.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.Instant;

/**
 * The service is intended for backing up online a stateful service
 * when using h2 on production with small amounts of data
 *
 * https://www.h2database.com/html/commands.html#script
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BackupH2Repository {
    private final JdbcTemplate jdbcTemplate;

    private static final String CompressionAndEncryption = "COMPRESSION ZIP";

    /**
     * Creates a SQL script from the database to file
     *
     * @return fileName with backup script
     */
    public String createBackupScriptToFile() {
        Instant instant = Instant.now();
        String fileName = "eshop-commodity-api-" + instant.toEpochMilli() + ".zip";
        String scriptSQL = "SCRIPT DROP TO '" + fileName + "' " + CompressionAndEncryption + " CHARSET 'UTF-8'";
        log.info("EXECUTE SCRIPT {}", scriptSQL);
        jdbcTemplate.execute(scriptSQL);
        return fileName;
    }

    public void restoreFromFile(String fileName) {
        String runScriptSQL = "RUNSCRIPT FROM '" + fileName + "' " + CompressionAndEncryption;
        jdbcTemplate.execute(runScriptSQL);
    }

    public boolean deleteFile(String fileName) {
        File file = new File(fileName);
        boolean response = file.delete();
        if (response) {
            System.out.println("Deleted the file: " + file.getName());
        } else {
            System.out.println("Failed to delete " + file.getName() + " the file.");
        }
        return response;
    }


}
