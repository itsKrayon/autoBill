package com.ab.autoBill.service;

import com.ab.autoBill.domain.request.BillRequest;
import com.ab.autoBill.domain.response.AssignmentListResponse;
import com.ab.autoBill.domain.response.AssignmentSituation;
import com.ab.autoBill.domain.Bill;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.LinkedList;
import java.util.Map;


/**
 * @author Keyan
 */
public interface IBillService {
    /**
     * 根据id获取用户账单
     * @param userId
     * @return
     */
    Map<String,Object> getBill(int userId);

    /**
     * 删除账单
     * @param userId
     * @return
     */
    Map<String,Object> deleteBill(int userId);

    /**
     * 增加账单
     * @param request
     * @return
     */
    Map<String,Object> addBill(BillRequest request);

    /**
     * 编辑账单
     * @param request
     * @return
     */
    Map<String,Object> editBill(BillRequest request);

    /**
     * 关键词搜索
     * @param keyword
     * @return
     */
    Map<String,Object> searchBill(String keyword);

}
