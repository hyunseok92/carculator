package com.example.carculator.repairshop_Item.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRepairshopItemDto is a Querydsl query type for RepairshopItemDto
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRepairshopItemDto extends EntityPathBase<RepairshopItemDto> {

    private static final long serialVersionUID = 1574761982L;

    public static final QRepairshopItemDto repairshopItemDto = new QRepairshopItemDto("repairshopItemDto");

    public final StringPath name_product = createString("name_product");

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final NumberPath<Long> repairshop_code = createNumber("repairshop_code", Long.class);

    public QRepairshopItemDto(String variable) {
        super(RepairshopItemDto.class, forVariable(variable));
    }

    public QRepairshopItemDto(Path<? extends RepairshopItemDto> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRepairshopItemDto(PathMetadata metadata) {
        super(RepairshopItemDto.class, metadata);
    }

}

