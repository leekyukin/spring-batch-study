package com.jik.batchtest.core.service;

import com.jik.batchtest.dto.PlayerDto;
import com.jik.batchtest.dto.PlayerSalaryDto;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
public class PlayerSalaryService {

    public PlayerSalaryDto calcSalary(PlayerDto playerDto) {
        int salary = (Year.now().getValue() - playerDto.getBirthYear()) * 1000000;
        return PlayerSalaryDto.of(playerDto, salary);
    }
}
