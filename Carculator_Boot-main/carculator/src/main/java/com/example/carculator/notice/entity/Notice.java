
package com.example.carculator.notice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.GregorianCalendar;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="notice")
public class Notice {

    @Id
    @Column(name = "NOTICE_NO")
    private Long notice_no;

    @Column(name = "MEMBERCODE")
    private String membercode;

    @Column(name = "NOTICE_TITLE")
    private String notice_title;


    @Column(name = "NOTICE_DATE")
    private Date notice_date;

    @Column(name = "NOTICE_CONTENT")
    private String notice_content;

    @Column(name="NOTICE_COUNT", nullable = false, columnDefinition = "1")
    private Long notice_count;

    @Column(name = "NOTICE_TARGET")
    private String notice_target;

    @PrePersist  //jpa 로 넘어가지 전에 작동
    public void prePersist() {
        notice_date = new GregorianCalendar().getGregorianChange();


    }
}