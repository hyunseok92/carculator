package com.example.carculator.shop.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBillDetail is a Querydsl query type for BillDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBillDetail extends EntityPathBase<BillDetail> {

    private static final long serialVersionUID = 1627073420L;

    public static final QBillDetail billDetail = new QBillDetail("billDetail");

    public final NumberPath<Long> BD_NO = createNumber("BD_NO", Long.class);

    public final NumberPath<Long> billCount = createNumber("billCount", Long.class);

    public final StringPath billNo = createString("billNo");

    public final StringPath memberCode = createString("memberCode");

    public final NumberPath<Long> productAmount = createNumber("productAmount", Long.class);

    public final NumberPath<Long> productCode = createNumber("productCode", Long.class);

    public final StringPath productName = createString("productName");

    public QBillDetail(String variable) {
        super(BillDetail.class, forVariable(variable));
    }

    public QBillDetail(Path<? extends BillDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBillDetail(PathMetadata metadata) {
        super(BillDetail.class, metadata);
    }

}

