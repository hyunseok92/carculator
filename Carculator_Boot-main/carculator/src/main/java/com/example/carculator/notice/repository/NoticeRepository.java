package com.example.carculator.notice.repository;

import com.example.carculator.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NoticeRepository extends JpaRepository<Notice, Long> {


    @Query(value = "select max(notice_no) from notice", nativeQuery = true)
    long findLast();


}
