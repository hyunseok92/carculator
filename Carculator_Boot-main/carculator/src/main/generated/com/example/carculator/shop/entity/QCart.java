package com.example.carculator.shop.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCart is a Querydsl query type for Cart
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCart extends EntityPathBase<Cart> {

    private static final long serialVersionUID = -1464684940L;

    public static final QCart cart = new QCart("cart");

    public final NumberPath<Long> billCount = createNumber("billCount", Long.class);

    public final StringPath cartCode = createString("cartCode");

    public final StringPath memberCode = createString("memberCode");

    public final NumberPath<Long> productAmount = createNumber("productAmount", Long.class);

    public final NumberPath<Long> productCode = createNumber("productCode", Long.class);

    public final NumberPath<Long> productCount = createNumber("productCount", Long.class);

    public final StringPath productName = createString("productName");

    public final StringPath productRmg = createString("productRmg");

    public QCart(String variable) {
        super(Cart.class, forVariable(variable));
    }

    public QCart(Path<? extends Cart> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCart(PathMetadata metadata) {
        super(Cart.class, metadata);
    }

}

