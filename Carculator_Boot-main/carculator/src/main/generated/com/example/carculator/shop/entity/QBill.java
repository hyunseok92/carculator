package com.example.carculator.shop.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBill is a Querydsl query type for Bill
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBill extends EntityPathBase<Bill> {

    private static final long serialVersionUID = -1464707237L;

    public static final QBill bill = new QBill("bill");

    public final StringPath billAddress = createString("billAddress");

    public final StringPath billAddressNo = createString("billAddressNo");

    public final NumberPath<Long> billAmount = createNumber("billAmount", Long.class);

    public final StringPath billCancle = createString("billCancle");

    public final DateTimePath<java.time.LocalDateTime> billDate = createDateTime("billDate", java.time.LocalDateTime.class);

    public final StringPath billEmail = createString("billEmail");

    public final StringPath billKind = createString("billKind");

    public final StringPath billName = createString("billName");

    public final StringPath billNo = createString("billNo");

    public final StringPath billNow = createString("billNow");

    public final StringPath billPhone = createString("billPhone");

    public final StringPath billRequest = createString("billRequest");

    public final StringPath invNo = createString("invNo");

    public final StringPath memberCode = createString("memberCode");

    public final StringPath paymentkey = createString("paymentkey");

    public final StringPath payName = createString("payName");

    public final StringPath recipient = createString("recipient");

    public QBill(String variable) {
        super(Bill.class, forVariable(variable));
    }

    public QBill(Path<? extends Bill> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBill(PathMetadata metadata) {
        super(Bill.class, metadata);
    }

}

