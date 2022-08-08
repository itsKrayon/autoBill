package com.ab.autoBill.service.impl;

import com.ab.autoBill.domain.request.BillRequest;
import com.ab.autoBill.domain.Bill;
import com.ab.autoBill.domain.User;
import com.ab.autoBill.repository.BillRepository;
import com.ab.autoBill.repository.UserRepository;
import com.ab.autoBill.service.IBillService;
import com.ab.autoBill.security.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

@Service
public class BillService implements IBillService {
    private BillRepository billRepository;


    private UserRepository userRepository;

    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public BillService(BillRepository billRepository,
                       UserRepository userRepository,
                       JwtTokenUtil jwtTokenUtil) {
        this.billRepository = billRepository;
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }


    public Map<String, Object> getBill(int userId) {
        List<Bill> bills = billRepository.findBillsByUser(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("msg", "获取信息成功");
        result.put("data", bills);
        //todo:还有收支结余的信息需要计算
        return result;
    }

    public Map<String, Object> deleteBill(int billId) {
        Map<String, Object> map = new HashMap<>();
        String msg;
        billRepository.deleteBillById(billId);
        if (billRepository.findBillById(billId) == null) {
            msg = "删除账单成功";
        } else {
            msg = "删除账单失败";
        }
        map.put("msg", msg);
        return map;
    }

    public Map<String, Object> addBill(BillRequest request) {
        Bill bill = new Bill();
        requestToBill(request, bill);
        bill = billRepository.save(bill);
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "添加账单记录成功");
        map.put("data", bill);
        return map;
    }

    public Map<String, Object> editBill(BillRequest request) {
        Bill bill = billRepository.findBillById(request.getId());
        requestToBill(request, bill);
        bill = billRepository.save(bill);
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "编辑账单记录成功");
        map.put("data", bill);
        return map;
    }

    private Bill requestToBill(BillRequest request, Bill bill) {
        bill.setTransType(request.getTransType());
        bill.setTransParty(request.getTransParty());
        bill.setGoods(request.getGoods());
        bill.setAmount(request.getAmount());
        bill.setSpending(request.isSpending());
        bill.setPayMethod(request.getPayMethod());
        bill.setCurrState(request.getCurrState());
        bill.setRemark(request.getRemark());
        bill.setTransTime(request.getTransTime());
        bill.setMonth(request.getMonth());
        bill.setType(request.getType());
        User user = userRepository.findUserById(request.getUserId());
        bill.setUser(user);
        return bill;

    }

    /**
     * 条件包括备注 分类
     * @param keyword
     * @return
     */
    public Map<String,Object> searchBill(String keyword){
        List<Bill> bills = billRepository.findBillsByTypeContaining(keyword);
        Map<String, Object> map = new HashMap<>();
        map.put("data",bills);
        return map;
    }

    /**
     * 上传csv文件，保存之后读表头实现数据库持久化
     *
     * @param assignmentListId
     * @param file
     * @param request
     * @return
     */
    public String uploadCsvFile(String assignmentListId, MultipartFile file, HttpServletRequest request) {
        //todo:读取csvFile文件，新建bill条目，循环插入数据库实现持久化
        String message = "";
        return message;
    }

    /**
     * 生成下载csv文件，文件命名以时间，支出的形式，如2022/7/20-2022/8/20.csv的形式
     *
     * @param assignmentId
     * @return
     */
    public File getCsvFile(String assignmentId) {
       // File file = new File(assignmentRepository.findAssignmentById(Long.parseLong(assignmentId)).getFile());
        File file = new File("");
        return file;
    }


}
