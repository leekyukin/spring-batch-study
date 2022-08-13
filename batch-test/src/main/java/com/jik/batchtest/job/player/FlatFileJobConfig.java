package com.jik.batchtest.job.player;

import com.jik.batchtest.core.service.PlayerSalaryService;
import com.jik.batchtest.dto.PlayerDto;
import com.jik.batchtest.dto.PlayerSalaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemProcessorAdapter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class FlatFileJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job flatFileJob(Step flatFileStep) {
        return jobBuilderFactory.get("flatFileJob")
                .incrementer(new RunIdIncrementer())
                .start(flatFileStep)
                .build();
    }


    @JobScope
    @Bean
    public Step flatFileStep(FlatFileItemReader<PlayerDto> playerFileItemReader,
                             ItemProcessorAdapter<PlayerDto, PlayerSalaryDto> playerSalaryItemProcessorAdapter
                             ) {
        return stepBuilderFactory.get("flatFileStep")
                .<PlayerDto, PlayerSalaryDto>chunk(5)
                .reader(playerFileItemReader)
                .processor(playerSalaryItemProcessorAdapter)
                .writer(new ItemWriter<>() {
                    @Override
                    public void write(List<? extends PlayerSalaryDto> items) throws Exception {
                        items.forEach(System.out::println);
                    }
                })
                .build();
    }

    // 버전 1 (이게 더 좋음, 어댑터 패턴으로 간단함)
    @StepScope
    @Bean
    public ItemProcessorAdapter<PlayerDto, PlayerSalaryDto> playerSalaryItemProcessorAdapter(PlayerSalaryService playerSalaryService) {
        ItemProcessorAdapter<PlayerDto, PlayerSalaryDto> adapter = new ItemProcessorAdapter<>();
        adapter.setTargetObject(playerSalaryService);
        adapter.setTargetMethod("calcSalary");
        return adapter;
    }

    // 버전 2
    @StepScope
    @Bean
    public ItemProcessor<PlayerDto, PlayerSalaryDto> playerSalaryItemProcessor(PlayerSalaryService playerSalaryService) {
        return new ItemProcessor<PlayerDto, PlayerSalaryDto>() {
            @Override
            public PlayerSalaryDto process(PlayerDto item) throws Exception {
                return playerSalaryService.calcSalary(item);
            }
        };
    }

    @StepScope
    @Bean
    public FlatFileItemReader<PlayerDto> playerFileItemReader() {
        return new FlatFileItemReaderBuilder<PlayerDto>()
                .name("playerFileItemReader")
                .lineTokenizer(new DelimitedLineTokenizer())    // 파일의 line 을 기본적으로 콤마(',')로 나눈다.
                .linesToSkip(1) // 한 줄 건너뛰고 읽기
                .fieldSetMapper(new PlayerFieldSetMapper())   // 어떻게 객체로 매핑할지 설정
                .resource(new FileSystemResource("player-list.txt")) // 읽어드릴 파일 설정
                .build();
    }
}
