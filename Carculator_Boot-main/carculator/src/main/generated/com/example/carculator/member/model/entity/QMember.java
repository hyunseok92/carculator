package com.example.carculator.member.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1928924787L;

    public static final QMember member = new QMember("member1");

    public final StringPath birth = createString("birth");

    public final StringPath email = createString("email");

    public final StringPath gender = createString("gender");

    public final StringPath grade = createString("grade");

    public final StringPath id = createString("id");

    public final StringPath membercode = createString("membercode");

    public final StringPath name = createString("name");

    public final EnumPath<com.example.carculator.enums.OAuthProvider> oAuthProvider = createEnum("oAuthProvider", com.example.carculator.enums.OAuthProvider.class);

    public final StringPath phone = createString("phone");

    public final StringPath picture = createString("picture");

    public final StringPath pwd = createString("pwd");

    public final StringPath shopaddress = createString("shopaddress");

    public final StringPath shopname = createString("shopname");

    public final StringPath shopnumber = createString("shopnumber");

    public final StringPath shopphone = createString("shopphone");

    public final StringPath shoprate = createString("shoprate");

    public final StringPath status = createString("status");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

