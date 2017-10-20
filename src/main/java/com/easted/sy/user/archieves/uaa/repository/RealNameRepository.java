package com.easted.sy.user.archieves.uaa.repository;

import com.easted.sy.user.archieves.uaa.domain.ClientDetails;
import com.easted.sy.user.archieves.uaa.domain.RealName;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface RealNameRepository extends DataTablesRepository<RealName,Integer> {

    /*通过实名认证信息*/
    @Modifying
    @Query("update RealName set state='通过' where id=:id")
    void pass(@Param("id") Integer id );

    /*不通过实名认证信息*/

    @Modifying
    @Query("update RealName set state='不通过' where id=:id")
    void notPass(@Param("id") Integer id);


    RealName findByLogin(String login);

}
