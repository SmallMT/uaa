package com.easted.sy.user.archieves.uaa.repository;

import com.easted.sy.user.archieves.uaa.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.time.Instant;

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long>,DataTablesRepository<User,Long> {

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    User findOneWithAuthoritiesById(Long id);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    Page<User> findAllByLoginNot(Pageable pageable, String login);

    /*认证状态*/
    @Modifying
    @Query("update User set verified=:verified,name=:name,identity=:identity where login=:login")
    void updateVerified(@Param("verified")Boolean verified,@Param("login")String  login,@Param("name") String name,@Param("identity") String identity);


    /**
     * 设置企业
     * @param login
     * @param enterpriseName
     */
    @Modifying
    @Query("update User set creditCode=:creditCode,enterpriseName=:enterpriseName,isLegalPserson=:isLegalPserson where login=:login")
    void setMyEnterprise(@Param("login") String login,@Param("enterpriseName") String enterpriseName,@Param("creditCode") String creditCode,@Param("isLegalPserson") Boolean isLegalPserson);

    /**
     * 根据用户身份ID检测用户是否存在
     * @param identity
     * @return
     */
    User findUserByIdentity(String identity);


    /**
     * 设置微信的unionId
     * @param login
     * @param unionId
     */
    @Modifying
    @Query("update User set weChat=:unionId where login=:login")
    void setWeChat(@Param("login") String login,@Param("unionId") String unionId);
}
