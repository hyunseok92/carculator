package com.example.carculator.repairshop_Item.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRepairshopItem is a Querydsl query type for RepairshopItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRepairshopItem extends EntityPathBase<RepairshopItem> {

    private static final long serialVersionUID = 932976673L;

    public static final QRepairshopItem repairshopItem = new QRepairshopItem("repairshopItem");

    public final StringPath name_product = createString("name_product");

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final NumberPath<Long> repairshop_code = createNumber("repairshop_code", Long.class);

    public QRepairshopItem(String variable) {
        super(RepairshopItem.class, forVariable(variable));
    }

    public QRepairshopItem(Path<? extends RepairshopItem> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRepairshopItem(PathMetadata metadata) {
        super(RepairshopItem.class, metadata);
    }

}

