package com.easted.sy.user.archieves.uaa.repository;

import com.easted.sy.user.archieves.uaa.domain.ClientDetails;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientDetailsRepository extends DataTablesRepository<ClientDetails,Integer> {
    Optional<ClientDetails> findAllByAppId(String appId);
}
