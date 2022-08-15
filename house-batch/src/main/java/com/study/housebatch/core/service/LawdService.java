package com.study.housebatch.core.service;

import com.study.housebatch.core.entity.Lawd;
import com.study.housebatch.core.repository.LawdRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LawdService {

    private final LawdRepository lawdRepository;

    @Transactional
    public void upsert(Lawd lawd) {
        // 데이터가 존재하면 수정, 없으면 생성

        Lawd saved = lawdRepository.findByLawdCd(lawd.getLawdCd())
                .orElseGet(Lawd::new);

        saved = Lawd.builder()
                .lawdCd(lawd.getLawdCd())
                .lawdDong(lawd.getLawdDong())
                .exist(lawd.getExist())
                .build();

        lawdRepository.save(saved);
    }
}
