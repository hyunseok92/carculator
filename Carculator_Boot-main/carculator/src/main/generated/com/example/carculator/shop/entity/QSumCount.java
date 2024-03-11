package com.example.carculator.shop.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSumCount is a Querydsl query type for SumCount
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSumCount extends EntityPathBase<SumCount> {

    private static final long serialVersionUID = 790053592L;

    public static final QSumCount sumCount = new QSumCount("sumCount");

    public final StringPath porductCode = createString("porductCode");

    public final StringPath productName = createString("productName");

    public final DatePath<java.sql.Date> productViewdate = createDate("productViewdate", java.sql.Date.class);

    public final NumberPath<Integer> userClickNo = createNumber("userClickNo", Integer.class);

    public final StringPath usercode = createString("usercode");

    public QSumCount(String variable) {
        super(SumCount.class, forVariable(variable));
    }

    public QSumCount(Path<? extends SumCount> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSumCount(PathMetadata metadata) {
        super(SumCount.class, metadata);
    }

}

