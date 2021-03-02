package com.db.awmd.challenge.MAIN;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin("*")
public class HCLChallange {

  private static final Logger LOGGER = LoggerFactory.getLogger(HCLChallange.class);



  public static void main(String[] args) {
    SpringApplication.run(HCLChallange.class, args);
    
  }
}
