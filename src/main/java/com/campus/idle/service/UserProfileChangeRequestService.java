package com.campus.idle.service;

import com.campus.idle.common.PageResult;
import com.campus.idle.dto.UserProfileChangeRequestCreateDTO;
import com.campus.idle.vo.UserProfileChangeRequestVO;

public interface UserProfileChangeRequestService {

    void submit(UserProfileChangeRequestCreateDTO dto);

    PageResult<UserProfileChangeRequestVO> myList(int pageNum, int pageSize);

    PageResult<UserProfileChangeRequestVO> adminList(int pageNum, int pageSize);

    void approve(Long id);

    void reject(Long id);
}