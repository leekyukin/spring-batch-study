package com.jik.batchtest.job.player;

import com.jik.batchtest.BatchTestConfig;
import com.jik.batchtest.core.service.PlayerSalaryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.AssertFile;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {FlatFileJobConfig.class, BatchTestConfig.class, PlayerSalaryService.class})
class FlatFileJobConfigTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    void contextLoads() throws Exception {
        // When
        JobExecution execution = jobLauncherTestUtils.launchJob();

        // Then
        assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
        AssertFile.assertFileEquals(
                new FileSystemResource("player-salary-list.txt"),
                new FileSystemResource("succeed-player-salary-list.txt")
        );
    }
}