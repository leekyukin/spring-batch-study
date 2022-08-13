package com.jik.batchtest.core.repository;

import com.jik.batchtest.core.domain.PlainText;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlainTextRepository extends JpaRepository<PlainText, Integer> {
    Page<PlainText> findBy(Pageable pageable);
}
