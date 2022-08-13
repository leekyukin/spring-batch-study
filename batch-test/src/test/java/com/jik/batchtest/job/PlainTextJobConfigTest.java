package com.jik.batchtest.job;

import com.jik.batchtest.BatchTestConfig;
import com.jik.batchtest.core.domain.PlainText;
import com.jik.batchtest.core.domain.ResultText;
import com.jik.batchtest.core.repository.PlainTextRepository;
import com.jik.batchtest.core.repository.ResultTextRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.IntStream;

@SpringBootTest
@SpringBatchTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test") // yml 에 profiles: test 활성화
@ContextConfiguration(classes = {PlainTextJobConfig.class, BatchTestConfig.class})
class PlainTextJobConfigTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private PlainTextRepository plainTextRepository;

    @Autowired
    private ResultTextRepository resultTextRepository;

    @AfterEach
    public void tearDown() {
        plainTextRepository.deleteAll();
        resultTextRepository.deleteAll();
    }


    @Test
    public void success_givenNoPlainText () throws Exception {
        // Given
        // no plainText

        // When
        JobExecution execution = jobLauncherTestUtils.launchJob();

        // Then
        Assertions.assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
        Assertions.assertEquals(resultTextRepository.count(), 0  );
    }

    @Test
    public void success_givenPlainText () throws Exception {
        // Given
        givenPlainText(12);

        // When
        JobExecution execution = jobLauncherTestUtils.launchJob();

        // Then
        Assertions.assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
        Assertions.assertEquals(resultTextRepository.count(), 12);
    }

    private void givenPlainText(Integer  count ) {
        IntStream.range(0, count)
                .forEach(
                        num -> plainTextRepository.save(new PlainText(null, "text" + num))
                );
    }
}