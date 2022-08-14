package com.jik.batchtest.job.parallel;

import com.jik.batchtest.dto.AmountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import java.io.File;
import java.io.IOException;

/*
* 단일 프로세서에서 청크 단위로 병렬 처리한다.
* */

@Configuration
@RequiredArgsConstructor
public class MultiThreadJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job multiThreadStepJob(Step multiThreadStep) {
        return jobBuilderFactory.get("multiThreadStepJob")
                .incrementer(new RunIdIncrementer())
                .start(multiThreadStep)
                .build();
    }


    @JobScope
    @Bean
    public Step multiThreadStep(
            FlatFileItemReader<AmountDto> amountFileItemReader,
            ItemProcessor<AmountDto,  AmountDto> amountFileItemProcessor,
            FlatFileItemWriter<AmountDto> amountFlatFileItemWriter,
            TaskExecutor taskExecutor
    ) {
        return stepBuilderFactory.get("multiThreadStep")
                .<AmountDto, AmountDto>chunk(10)
                .reader(amountFileItemReader)
                .processor(amountFileItemProcessor)
                .writer(amountFlatFileItemWriter)
                .taskExecutor(taskExecutor)
                .build();
    }

    // TaskExecutor 가 일반 task 를 multiThreadTask 로 만들어준다
    // 아래의 클레스를 하나의 Thread Pool 로 이해하면 좋다
    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor("spring-batch-task-executor");
        taskExecutor.setConcurrencyLimit(4);    // Thread Pool 의 개수를 4개로 제한해서 과부화가 일어나지 않게
        return taskExecutor;
    }

    @StepScope
    @Bean
    public FlatFileItemReader<AmountDto> amountFileItemReader() {
        return new FlatFileItemReaderBuilder<AmountDto>()
                .name("amountFileItemReader")
                .fieldSetMapper(new AmountFieldSetMapper())
                .lineTokenizer(new DelimitedLineTokenizer(DelimitedLineTokenizer.DELIMITER_TAB))
                .resource(new FileSystemResource("data/input.txt"))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<AmountDto,  AmountDto> amountFileItemProcessor() {
        return item -> {
            System.out.println(item + "\tThread " + Thread.currentThread().getName());
            item.setAmount(item.getAmount() * 100);
            return item;
        };
    }

    @StepScope
    @Bean
    public FlatFileItemWriter<AmountDto> amountFlatFileItemWriter() throws IOException {
        BeanWrapperFieldExtractor<AmountDto> fieldExtractor  = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[] {"index", "name", "amount"});
        fieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<AmountDto> lineAggregator = new DelimitedLineAggregator();
        lineAggregator.setFieldExtractor(fieldExtractor);

        String filePath = "data/output.txt";
        new File(filePath).createNewFile();

        return new FlatFileItemWriterBuilder<AmountDto>()
                .name("amountFlatFileItemWriter")
                .resource(new FileSystemResource(filePath))
                .lineAggregator(lineAggregator)
                .build();
    }

}