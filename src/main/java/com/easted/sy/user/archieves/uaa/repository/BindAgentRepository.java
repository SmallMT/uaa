package com.easted.sy.user.archieves.uaa.repository;

import com.easted.sy.user.archieves.uaa.domain.BindAgent;
import com.easted.sy.user.archieves.uaa.domain.BindEnterprise;
import com.easted.sy.user.archieves.uaa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BindAgentRepository extends JpaRepository<BindAgent,Integer> {

    BindAgent findByUserAndBindEnterprise(User user, BindEnterprise bindEnterprise);

    List<BindAgent> findBindAgentsByBindEnterprise(BindEnterprise bindEnterprise);
}
