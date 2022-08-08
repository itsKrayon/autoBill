package com.ab.autoBill.repository;

import com.ab.autoBill.domain.Authority;
import com.ab.autoBill.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;


/**
 * @author Keyan
 */
@Repository
public interface AuthorityRepository extends CrudRepository<Authority, Integer> {
    /**
     * 找到对应权限的条目
     * @param authority
     * @return
     */
    Authority findByAuthority(String authority);

}
