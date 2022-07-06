package com.example.webBank.Repository;

import com.example.webBank.Entity.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepo extends CrudRepository<Account,Long> {
}
