package com.jik.batchtest.core.service;

import com.jik.batchtest.dto.PlayerDto;
import com.jik.batchtest.dto.PlayerSalaryDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlayerSalaryServiceTest {

    private PlayerSalaryService playerSalaryService;

    @BeforeEach
    public void setUp() {
        playerSalaryService = new PlayerSalaryService();
    }

    @Test
    public void calcSalary() {
        // Given
        Year mockYear = mock(Year.class);
        when(mockYear.getValue()).thenReturn(2020);
        Mockito.mockStatic(Year.class).when(Year::now).thenReturn(mockYear);

        PlayerDto mockPlayer = mock(PlayerDto.class);
        when(mockPlayer.getBirthYear()).thenReturn(1980);

        // When
        PlayerSalaryDto result = playerSalaryService.calcSalary(mockPlayer);

        // Then
        assertEquals(result.getSalary(), 40_000_000);
    }
}