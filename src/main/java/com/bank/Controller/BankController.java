package com.bank.Controller;

import com.bank.entity.Customer;
import com.bank.service.bank.BankService;
import com.bank.utils.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bank")
public class BankController {
    @Autowired
    private BankService bankService;

    // Onboard a new customer
    @PostMapping("/onboard")
    public ResponseEntity<Response<Customer>> onboardCustomer(@RequestParam String name) {

          return bankService.onboardCustomer(name);
    }

    // Transfer money between current and savings account
    @PostMapping("/transfer")
    public ResponseEntity<Response<String>> transferCurrentToSaving(@RequestParam Long customerId, @RequestParam double amount) {
      return  bankService.transferBetweenAccounts(customerId, amount);
    }
//    transfer from saving to current
    @PostMapping("/saving/current")
    public ResponseEntity<Response<String>> transferSavingToCurrentAccounts(@RequestParam Long customerId, @RequestParam double amount) {
     ;
        return    bankService.transferSavingToCurrent(customerId, amount);
    }

    // Make a payment from the current account
    @PostMapping("/payment")
    public ResponseEntity<Response<String>> makePayment(@RequestParam Long customerId, @RequestParam double amount) {

        return bankService.makePayment(customerId, amount);
    }

    // Make a payment from the current account
    @PostMapping("/between/account/")
    public ResponseEntity<Response<String>> makePayment(@RequestParam Long senderId, @RequestParam double amount,@RequestParam Long receiverId) {
        return bankService.accountToAnotherAccount(senderId, amount,receiverId);
    }

    // Get customer details (for viewing transactions)
    @GetMapping("/customer/{id}")
    public ResponseEntity<Response<Customer>> getCustomer(@PathVariable Long id) {
        return bankService.getCustomer(id);
    }
}
