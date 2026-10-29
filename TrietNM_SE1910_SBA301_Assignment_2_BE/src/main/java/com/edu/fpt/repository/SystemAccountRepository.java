package com.edu.fpt.repository;

import com.edu.fpt.domain.entity.SystemAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemAccountRepository extends JpaRepository<SystemAccount, Long> {

    @Modifying
    @Query("update SystemAccount sa set sa.accountName = :newName where sa.id = :userId")
    int updateUserName(@Param("newName") String newName , @Param("userId") Long userId);

    Page<SystemAccount> findSystemAccountByAccountEmailIgnoreCaseOrderBy(String key, Pageable pageable);

    boolean existsByAccountEmailIgnoreCase(String accountEmail);

}
