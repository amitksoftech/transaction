package com.db.awmd.challenge.MAIN.Services;

import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.db.awmd.challenge.MAIN.Models.Account;
import io.swagger.annotations.ApiOperation;

@CrossOrigin("*")
@RestController
public class AccountsController {

  @Autowired
  AccountsService accountsService;




  @PostMapping("/createAccount")
  @ApiOperation(value = "create account")
  public ResponseEntity<Object> createAccount(@RequestBody Account account) {



    try {
      accountsService.createAccount(account);
    } catch (DuplicateAccountIdException daie) {
      return new ResponseEntity<>(daie.getMessage(), HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping(path = "getaccountdetails/{accountId}")
  public Optional<Account> getAccount(@PathVariable String accountId) {

    System.out.println("here for account details");
    System.out.println("get account details:"+accountsService.getAccount(accountId).toString());

    return accountsService.getAccount(accountId);
  }

  //controller for making transfers

  @GetMapping(path = "getINITtransfer/{fromAccountId}/{toAccountId}/{amount}")
  @ApiOperation(value = "initiate transfer")
  public String getTrans(@PathVariable("fromAccountId") String fromAccountId, 
      @PathVariable("toAccountId") String toAccountId,@PathVariable("amount") BigDecimal amount ) {

    return accountsService.transfer(fromAccountId,toAccountId,amount);
  }

}
