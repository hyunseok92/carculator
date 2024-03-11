package com.example.carculator.damage.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDamage is a Querydsl query type for Damage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDamage extends EntityPathBase<Damage> {

    private static final long serialVersionUID = 1779332668L;

    public static final QDamage damage = new QDamage("damage");

    public final StringPath carNo = createString("carNo");

    public final StringPath damaged = createString("damaged");

    public final StringPath memberNo = createString("memberNo");

    public final StringPath menteEndDate = createString("menteEndDate");

    public final StringPath menteGrade = createString("menteGrade");

    public final StringPath menteNo = createString("menteNo");

    public final StringPath menteStartDate = createString("menteStartDate");

    public final NumberPath<Integer> pay = createNumber("pay", Integer.class);

    public QDamage(String variable) {
        super(Damage.class, forVariable(variable));
    }

    public QDamage(Path<? extends Damage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDamage(PathMetadata metadata) {
        super(Damage.class, metadata);
    }

}

