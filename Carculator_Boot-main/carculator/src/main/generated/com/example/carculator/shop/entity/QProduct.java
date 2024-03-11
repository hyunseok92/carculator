package com.example.carculator.shop.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 1574889755L;

    public static final QProduct product = new QProduct("product");

    public final StringPath category1 = createString("category1");

    public final StringPath detailImg = createString("detailImg");

    public final StringPath detailRmg = createString("detailRmg");

    public final NumberPath<Long> productAmount = createNumber("productAmount", Long.class);

    public final NumberPath<Long> productCode = createNumber("productCode", Long.class);

    public final NumberPath<Long> productCount = createNumber("productCount", Long.class);

    public final StringPath productDetail = createString("productDetail");

    public final StringPath productImg = createString("productImg");

    public final StringPath productName = createString("productName");

    public final StringPath productRmg = createString("productRmg");

    public final NumberPath<Long> productSum = createNumber("productSum", Long.class);

    public final NumberPath<Long> productView = createNumber("productView", Long.class);

    public QProduct(String variable) {
        super(Product.class, forVariable(variable));
    }

    public QProduct(Path<? extends Product> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProduct(PathMetadata metadata) {
        super(Product.class, metadata);
    }

}

