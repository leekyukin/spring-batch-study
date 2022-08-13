package com.jik.batchtest.core.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "plain_text")
@DynamicUpdate // 테이블의 일부값만 바뀌었을 때 일부값만 업데이트하는 쿼리를 날리게
public class PlainText {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer  id;

    @Column(nullable = false)
    private String text;
}
