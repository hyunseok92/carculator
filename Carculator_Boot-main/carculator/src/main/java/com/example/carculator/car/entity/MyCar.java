package com.example.carculator.car.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Builder
@SequenceGenerator(
        name = "CAR_ID_GEN",
        sequenceName = "CAR_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Table(name="MYCAR")
public class MyCar {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "CAR_ID_GEN"
    )
    @Column(name = "CAR_ID")
    private Long car_id;

    @Column(name="MEMBERCODE")
    private String membercode;

    @Column(name = "CAR_MODEL")
    private String car_model;

    @Column(name = "CAR_YEAR")
    private int car_year;

    @Column(name = "CAR_NUMBER")
    private String car_number;

    @Column(name = "CAR_ORIGINAL_IMG")
    private String car_original_img;

    @Column(name = "CAR_RENAME_IMG")
    private String car_rename_img;
}
