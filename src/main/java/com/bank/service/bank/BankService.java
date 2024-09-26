package com.bank.service.bank;

import com.bank.entity.Customer;
import com.bank.utils.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface BankService {
 public ResponseEntity<Response<Customer>> onboardCustomer(String name);
    public ResponseEntity<Response<String>> transferBetweenAccounts(Long customerId, double amount);
    public ResponseEntity<Response<String>> transferSavingToCurrent(Long customerId, double amount);

    public ResponseEntity<Response<String>> makePayment(Long customerId, double amount);
    ResponseEntity<Response<String>> accountToAnotherAccount(Long senderId, double amount,Long receiverId);
    public ResponseEntity<Response<Customer>> getCustomer(Long customerId);
}
