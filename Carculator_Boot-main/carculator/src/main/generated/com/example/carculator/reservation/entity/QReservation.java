package com.example.carculator.reservation.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QReservation is a Querydsl query type for Reservation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReservation extends EntityPathBase<Reservation> {

    private static final long serialVersionUID = 272292036L;

    public static final QReservation reservation = new QReservation("reservation");

    public final StringPath car_number = createString("car_number");

    public final StringPath content = createString("content");

    public final StringPath membercode = createString("membercode");

    public final StringPath repair_item = createString("repair_item");

    public final NumberPath<Long> repairshop_code = createNumber("repairshop_code", Long.class);

    public final NumberPath<Long> reserv_code = createNumber("reserv_code", Long.class);

    public final DateTimePath<java.sql.Timestamp> reserv_date = createDateTime("reserv_date", java.sql.Timestamp.class);

    public final StringPath reserv_yn = createString("reserv_yn");

    public QReservation(String variable) {
        super(Reservation.class, forVariable(variable));
    }

    public QReservation(Path<? extends Reservation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReservation(PathMetadata metadata) {
        super(Reservation.class, metadata);
    }

}

