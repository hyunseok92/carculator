package com.example.carculator.car.entity;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MyCarDto {
    private Long car_id;
    private String membercode;
    private String car_model;
    private int car_year;
    private String car_number;
    private String car_original_img;
    private String car_rename_img;
    private String name;
}
