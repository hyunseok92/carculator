package com.example.carculator.car.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMyCar is a Querydsl query type for MyCar
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMyCar extends EntityPathBase<MyCar> {

    private static final long serialVersionUID = -2124358792L;

    public static final QMyCar myCar = new QMyCar("myCar");

    public final NumberPath<Long> car_id = createNumber("car_id", Long.class);

    public final StringPath car_model = createString("car_model");

    public final StringPath car_number = createString("car_number");

    public final StringPath car_original_img = createString("car_original_img");

    public final StringPath car_rename_img = createString("car_rename_img");

    public final NumberPath<Integer> car_year = createNumber("car_year", Integer.class);

    public final StringPath membercode = createString("membercode");

    public QMyCar(String variable) {
        super(MyCar.class, forVariable(variable));
    }

    public QMyCar(Path<? extends MyCar> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMyCar(PathMetadata metadata) {
        super(MyCar.class, metadata);
    }

}

