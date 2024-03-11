package com.example.carculator.repairshop_Item.repository;

import com.example.carculator.repairshop_Item.entity.QRepairshopItem;
import com.example.carculator.repairshop_Item.entity.RepairshopItemDto;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class RepairshopItemRepositoryCustomImpl implements RepairshopItemRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<RepairshopItemDto> getInventoryList(Long repairshop_code) {
        QRepairshopItem r = QRepairshopItem.repairshopItem;

        JPAQuery<Tuple> query = queryFactory.select(
                r.repairshop_code,
                r.name_product,
                r.quantity,
                r.price)
                .from(r)
                .where(r.repairshop_code.eq(repairshop_code));

        return query.fetch().stream().map(tuple -> RepairshopItemDto.builder()
                .repairshop_code(tuple.get(r.repairshop_code))
                .name_product(tuple.get(r.name_product))
                .quantity(tuple.get(r.quantity))
                .price(tuple.get(r.price))
                .build()).toList();
    }
}
