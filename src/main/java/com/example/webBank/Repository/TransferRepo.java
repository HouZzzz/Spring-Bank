package com.example.webBank.Repository;

import com.example.webBank.Entity.Transfer;
import org.springframework.data.repository.CrudRepository;

public interface TransferRepo extends CrudRepository<Transfer,Long> {
}
