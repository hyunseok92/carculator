package com.example.carculator.notice.service;

import com.example.carculator.notice.entity.Notice;
import com.example.carculator.notice.repository.NoticeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class NoticeService {
    private final NoticeRepository noticeRepository;


    //Notice 전체 조회
    public List<Notice> noticeList() {
        return noticeRepository.findAll();
    }



    //상세보기
    public Notice getNoticeByCode(Long notice_no) {
        Notice noticeEntity = noticeRepository.findById(notice_no).orElseThrow(() -> new RuntimeException("정비업체를 찾을 수 없습니다."));
        return Notice.builder()
                .notice_no(noticeEntity.getNotice_no())
                .membercode(noticeEntity.getMembercode())
                .notice_title(noticeEntity.getNotice_title())
                .notice_date(noticeEntity.getNotice_date())
                .notice_content(noticeEntity.getNotice_content())
                .notice_count(noticeEntity.getNotice_count())
                .notice_target(noticeEntity.getNotice_target())
                .build();

    }



    //Notice 등록
    @Transactional
    public Notice insertNotice(Notice notice){

        return noticeRepository.save(notice);
    }

    //Notice 수정
    @Transactional
    public Notice updateNotice(Notice notice){
        Notice noticeEntity = noticeRepository.findById(notice.getNotice_no()).orElseThrow(() -> new RuntimeException("없음"));
        noticeEntity.setNotice_title(notice.getNotice_title());
        noticeEntity.setNotice_content(notice.getNotice_content());


        return noticeRepository.save(noticeEntity);
    }

    //Notice 삭제
    @Transactional
    public int deleteNotice(Long notice_no) {
        try {
            noticeRepository.deleteById(notice_no);
            return 1;
        }catch(Exception e) {
            log.info(e.getMessage());
            return 0;
        }
    }



    @Transactional
    public Notice updateAddReadCount(Notice notice) {
        //추가 메서드
        return noticeRepository.save(notice);
    }


    public long selectLast() {
        //추가 메서드
        return noticeRepository.findLast();
    }
}
