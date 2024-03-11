package com.example.carculator.damage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="damage")
public class Damage {

    @Id
    @Column(name="memberNo")
    private String memberNo;

    @Column(name="carNo")
    private String carNo;

    @Column(name="damaged")
    private String damaged;

    @Column(name="menteNo")
    private String menteNo;

    @Column(name="MenteGrade")
    private String menteGrade;

    @Column(name="menteStartDate")
    private String menteStartDate;

    @Column(name="menteEndDate")
    private String menteEndDate;

    @Column(name="pay")
    private int pay;

}
