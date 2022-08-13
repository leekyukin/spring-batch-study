package com.jik.batchtest.job.parallel;

import com.jik.batchtest.dto.AmountDto;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class AmountFieldSetMapper implements FieldSetMapper<AmountDto> {
    @Override
    public AmountDto mapFieldSet(FieldSet fieldSet) throws BindException {
        return AmountDto.builder()
                .index(fieldSet.readInt(0))
                .name(fieldSet.readString(1))
                .amount(fieldSet.readInt(2))
                .build();
    }
}
