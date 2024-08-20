package com.spring.security.repository;

import com.spring.security.model.Account;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Mapper
public interface AccountRepository {
    Optional<Account> getAccountByUsername(String username);

    boolean insertAccount(Account acc);

    Optional<Account> getAccountByGmail(String gmail);

    Optional<Account> getAccountById(long id);
}
