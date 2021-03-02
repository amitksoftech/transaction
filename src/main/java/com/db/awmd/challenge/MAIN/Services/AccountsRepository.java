package com.db.awmd.challenge.MAIN.Services;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.db.awmd.challenge.MAIN.Models.Account;



@Repository
public interface AccountsRepository extends MongoRepository<Account, String> {
  
  Optional<Account> findById(String accountId);
  
  
  
}
