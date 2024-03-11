package com.example.carculator.repairshop.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRepairShop is a Querydsl query type for RepairShop
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRepairShop extends EntityPathBase<RepairShop> {

    private static final long serialVersionUID = -992012412L;

    public static final QRepairShop repairShop = new QRepairShop("repairShop");

    public final StringPath latitude = createString("latitude");

    public final StringPath longitude = createString("longitude");

    public final StringPath membercode = createString("membercode");

    public final StringPath repairshop_address = createString("repairshop_address");

    public final StringPath repairshop_close = createString("repairshop_close");

    public final NumberPath<Long> repairshop_code = createNumber("repairshop_code", Long.class);

    public final StringPath repairshop_name = createString("repairshop_name");

    public final StringPath repairshop_open = createString("repairshop_open");

    public final StringPath repairshop_phone = createString("repairshop_phone");

    public final StringPath repairshop_rate = createString("repairshop_rate");

    public QRepairShop(String variable) {
        super(RepairShop.class, forVariable(variable));
    }

    public QRepairShop(Path<? extends RepairShop> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRepairShop(PathMetadata metadata) {
        super(RepairShop.class, metadata);
    }

}

