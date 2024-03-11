package com.example.carculator.notice.controller;

import com.example.carculator.notice.entity.Notice;
import com.example.carculator.notice.service.NoticeService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Slf4j
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@CrossOrigin
@RestController
public class NoticeController {
    @Autowired
    private final NoticeService noticeService;

    //공지 리스트 조회

    @GetMapping("/notice")
    public ResponseEntity<List<Notice>> noticeList() {
        try {
            List<Notice> notices = noticeService.noticeList();

            // Sorting notices in descending order based on notice_no
            notices.sort(Comparator.comparing(Notice::getNotice_no).reversed());

            return new ResponseEntity<>(notices, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving notice list", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //상세조회

    @GetMapping("/notice/{notice_no}")
    public ResponseEntity<Notice> selectRepairshopByCode(@PathVariable("notice_no") Long notice_no) throws Exception {
        log.info("ss"+notice_no);

        Notice notice = noticeService.getNoticeByCode(notice_no);
        notice.setNotice_count(notice.getNotice_count()+1);
        noticeService.updateAddReadCount(notice);

        return new ResponseEntity<>(notice, HttpStatus.OK);
    }



    //회원 등록
    @PostMapping("/notice")
    public ResponseEntity<Notice> noticeInsert(@RequestBody Notice notice) {

        notice.setNotice_no(noticeService.selectLast()+1);
        notice.setNotice_count(0L);
        log.info(notice.toString());
        if(noticeService.insertNotice(notice)!= null) {
            return new ResponseEntity<Notice>(notice, HttpStatus.OK);
        }else{
            return new ResponseEntity<Notice>(notice, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    //회원수정
    @PatchMapping("/notice")
    public Notice noticeUpdate(@RequestBody Notice notice) {
        return noticeService.updateNotice(notice);
    }

    //회원삭제
    @DeleteMapping("/notice/{notice_no}")
    public ResponseEntity<Notice> noticeDelete(@PathVariable("notice_no") Long notice_no) {
        log.info("dd"+notice_no);
        Notice notice = noticeService.getNoticeByCode(notice_no);
       if (noticeService.deleteNotice(notice_no) > 0){
           return new ResponseEntity<Notice>(notice, HttpStatus.OK);
       }else{
           return new ResponseEntity<Notice>(notice, HttpStatus.BAD_REQUEST);
       }

    }
}
