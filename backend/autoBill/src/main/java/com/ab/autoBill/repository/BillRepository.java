package com.ab.autoBill.repository;

import com.ab.autoBill.domain.Bill;
import com.ab.autoBill.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Keyan
 */
@Repository
public interface BillRepository extends CrudRepository<Bill, Integer>{
    List<Bill> findBillsByUser(int use_id);

    void deleteBillById(int billId);

    Bill findBillById(int billId);

    Bill save(Bill bill);

    List<Bill> findBillsByTypeContaining(String keyword);


}
