package com.busanit501.springproject3.msy.service;


import com.busanit501.springproject3.msy.dto.PageRequestDTO;
import com.busanit501.springproject3.msy.dto.PageResponseDTO;
import com.busanit501.springproject3.msy.dto.ReplyDTO;

public interface ReplyService {
    // 댓글 , crud
    Long register(ReplyDTO replyDTO);
    ReplyDTO read(Long rno);
    void update(ReplyDTO replyDTO);
    void delete(Long rno);
    // 댓글 페이징 처리해서 목록 출력.
    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);
}
