package com.example.carculator.notice.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QNotice is a Querydsl query type for Notice
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotice extends EntityPathBase<Notice> {

    private static final long serialVersionUID = 646963534L;

    public static final QNotice notice = new QNotice("notice");

    public final StringPath membercode = createString("membercode");

    public final StringPath notice_content = createString("notice_content");

    public final NumberPath<Long> notice_count = createNumber("notice_count", Long.class);

    public final DateTimePath<java.util.Date> notice_date = createDateTime("notice_date", java.util.Date.class);

    public final NumberPath<Long> notice_no = createNumber("notice_no", Long.class);

    public final StringPath notice_target = createString("notice_target");

    public final StringPath notice_title = createString("notice_title");

    public QNotice(String variable) {
        super(Notice.class, forVariable(variable));
    }

    public QNotice(Path<? extends Notice> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNotice(PathMetadata metadata) {
        super(Notice.class, metadata);
    }

}

