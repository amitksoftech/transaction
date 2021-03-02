package com.db.awmd.challenge.MAIN.Services;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import com.db.awmd.challenge.MAIN.HCLChallange;
import com.db.awmd.challenge.MAIN.Models.Account;

@Service
public class AccountsService {


  @Autowired
  MongoTemplate mongoTemplate;

  @Autowired
  AccountsRepository accountsRepository;

  @Autowired
  EmailNotificationService emailNotificationService;

  @Autowired
  AccountsService accountsService;

  private static final Logger LOGGER = LoggerFactory.getLogger(AccountsService.class);

  private Lock bankLock = new ReentrantLock();


  public void createAccount(Account account) {
    mongoTemplate.save(account);
    LOGGER.info("account created");

  }

  public Optional<Account> getAccount(String accountId) {
    return accountsRepository.findById(accountId);
  }


  /**
   * @author Amit Kumar
   * @since 2 March 2021
   * @throws NullPointerException
   * @param fromAccountId
   * @param toAccountId
   * @param amount
   * @return
   */
  public String transfer(String fromAccountId, String toAccountId, BigDecimal amount) {

    bankLock.lock();
    try {
      Optional<Account> fromAccount = accountsService.getAccount(fromAccountId);
      Optional<Account> toAccount = accountsService.getAccount(toAccountId);
      Boolean checkForPositiveAmount = (amount.compareTo(BigDecimal.ZERO) > 0);
      int checkForSufficientBalance =
          (fromAccount.orElseThrow(NullPointerException::new).getBalance()).compareTo(amount);


      if (fromAccount.isPresent() && toAccount.isPresent()) {
        if ((checkForSufficientBalance == 1) && checkForPositiveAmount) {


          BigDecimal newBalanceForFromAccount = (fromAccount.get().getBalance()).subtract(amount);
          fromAccount.get().setBalance(newBalanceForFromAccount);
          accountsService.createAccount(fromAccount.get());

          BigDecimal newBalanceForToAccount = (toAccount.get().getBalance()).add(amount);
          toAccount.get().setBalance(newBalanceForToAccount);
          accountsService.createAccount(toAccount.get());

          emailNotificationService.notifyAboutTransfer(fromAccount.orElseThrow(NullPointerException::new), "debited ::" + amount);
          emailNotificationService.notifyAboutTransfer(toAccount.orElseThrow(NullPointerException::new), "credited ::" + amount);

          return "transfer successful";
        } else {
          return "over-drafting facility not available";
        }
      } else {
        return "Any account not present";
      }
    } catch (Exception e) {
      LOGGER.error("Exception occured while transaction ::" + e);
      return "transfer fail";
    } finally {
      bankLock.unlock();
    }
  }
}
