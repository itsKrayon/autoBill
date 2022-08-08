package com.ab.autoBill.controller;

import com.ab.autoBill.domain.request.BillRequest;
import com.ab.autoBill.domain.Bill;
import com.ab.autoBill.service.impl.BillService;
import com.ab.autoBill.service.IBillService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.Map;


/**
 * @author Keyan
 */
@RestController
public class BillController {

    private static final Logger logger = LoggerFactory.getLogger(BillController.class);

    private IBillService billService;

    @Autowired
    public BillController(BillService billService) {
        this.billService = billService;
    }

    /**
     * 获取本人的账单，同时会计算结余的
     * @param userId
     * @return
     */
    @PostMapping("/bills")
    public ResponseEntity<Map<String, Object>>getMyBills(@RequestParam("userId") int userId) {
        return ResponseEntity.ok(billService.getBill(userId));
    }

    @PostMapping("/bill/delete")
    public ResponseEntity<Map<String, Object>> deleteBill(@RequestParam("billId") int billId) {
        return ResponseEntity.ok(billService.deleteBill(billId));
    }

    @PostMapping("/bill/add")
    public ResponseEntity<Map<String, Object>> addBill(@RequestBody BillRequest billRequest) {
        return ResponseEntity.ok(billService.addBill(billRequest));
    }

    /**
     * 编辑单条账单记录
      * @param
     * @return
     */
    @PostMapping("/bill/edit")
    public ResponseEntity<Map<String, Object>> EditBill(@RequestBody BillRequest billRequest) {
        return ResponseEntity.ok(billService.editBill(billRequest));
    }

    /**
     * 对账单进行条件筛选，关键词的模糊查询，时间/收或支/分类，前端做也可以
     * @param keyword
     * @return
     */
    @PostMapping("/bill/search")
    public ResponseEntity<Map<String, Object>> searchBills(@RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(billService.searchBill(keyword));
    }

    /**
     * @param userId
     * @return
     */
    @PostMapping("/admin/bills")
    @PreAuthorize("hasAuthority('Role_Admin')")
    public ResponseEntity<Map<String, Object>> getBills(@RequestParam("userId") int userId) {
        return ResponseEntity.ok(billService.getBill(userId));
    }


    /**
     * 上传账单文件，账单持久化
     * @param assignmentListId
     * @return
     * @throws IOException
     */
//    @PostMapping(value = "/bill/uploadCsvFile")
//    public ResponseEntity<byte[]> uploadCsvFile(@RequestParam("assignmentListId") String assignmentListId) throws IOException {
//        return ResponseEntity.ok(billService.uploadCsvFile());
//    }

    /**
     * 输出账单，直接是当前表格的账单怎么传
     * @param assignmentListId
     * @return
     * @throws IOException
     */
//    @PostMapping(value = "/bill/getCsvFile")
//    public ResponseEntity<byte[]> getCsvFile(@RequestParam("assignmentListId") String assignmentListId) throws IOException {
//        File file = billService.getCsvFile(assignmentListId);;
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.parseMediaType("application/octet-stream"));
//        String fileName = file.getName();
//        httpHeaders.add("Access-Control-Expose-Headers", "Content-Disposition");
//        httpHeaders.add("content-disposition","attachment;fileName="+ URLEncoder.encode(fileName,"UTF-8"));
//        return new ResponseEntity<>(FileUtils.readFileToByteArray(file),
//                httpHeaders,
//                HttpStatus.OK);
//    }

}
