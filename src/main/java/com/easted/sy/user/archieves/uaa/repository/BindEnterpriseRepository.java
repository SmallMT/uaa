package com.easted.sy.user.archieves.uaa.repository;

import com.easted.sy.user.archieves.uaa.domain.BindEnterprise;
import com.easted.sy.user.archieves.uaa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface BindEnterpriseRepository extends JpaRepository<BindEnterprise,Integer> {

    List<BindEnterprise> findBindEnterprisesByUser(User user);


    void deleteBindEnterpriseByCreditCode(String creditCode);


    BindEnterprise findBindEnterpriseByUserAndCreditCode(User user,String creditCode);


}
