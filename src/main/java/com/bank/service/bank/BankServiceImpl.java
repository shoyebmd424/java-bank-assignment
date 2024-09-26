package com.bank.service.bank;

import com.bank.entity.Account;
import com.bank.entity.Customer;
import com.bank.repository.CustomerRepository;
import com.bank.utils.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankServiceImpl implements BankService {
    @Autowired
    private CustomerRepository customerRepository;

    public ResponseEntity<Response<Customer>> onboardCustomer(String name) {
        try {
            Customer customer = new Customer(name);
       Customer saved=     customerRepository.save(customer);
            return  new ResponseEntity<>( new Response<Customer>(saved,""),HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return  new ResponseEntity<>( new Response<Customer>(null,"something went wrong"),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

//    current to saving
    public ResponseEntity<Response<String>> transferBetweenAccounts(Long customerId, double amount) {
        try {
            Optional<Customer> customerOpt = customerRepository.findById(customerId);
            if (customerOpt.isPresent()) {
                Customer customer = customerOpt.get();
                Account current = customer.getCurrentAccount();
                Account savings = customer.getSavingAccount();
                if (current.getBalance() >= amount) {
                    double fee=amount*0.005;
                    current.debit(amount-fee, fee);
                    savings.credit(amount-fee);
//                    this fees will send to ownee
//                    savings.credit(savings.getBalance() * 0.005); //  0.5% interest
                    customerRepository.save(customer);
                    return new ResponseEntity<>(new Response<String>("amount transfer successfully", ""), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new Response<String>("", "Insufficient funds in the current account."), HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(new Response<String>("", "Account Not Found."), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>( new Response<String>("", "Something went wrong"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //     saving to current
    public ResponseEntity<Response<String>> transferSavingToCurrent(Long customerId, double amount) {
        try {
            Optional<Customer> customerOpt = customerRepository.findById(customerId);
            if (customerOpt.isPresent()) {
                Customer customer = customerOpt.get();
                Account current = customer.getCurrentAccount();
                Account savings = customer.getSavingAccount();
                if (savings.getBalance() >= amount) {
                    double fee=amount*0.0005;
                    savings.debit(amount-fee, fee);
                    current.credit(amount-fee);
//                    this is for fees
//                    current.credit(savings.getBalance() * 0.0005); //  0.05% interest
                    customerRepository.save(customer);
                    return new ResponseEntity<>(new Response<String>("amount transfer successfully", ""), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new Response<String>("", "Insufficient funds in the current account."), HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(new Response<String>("", "Account Not Found."), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>( new Response<String>("", "Something went wrong"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<Response<String>> accountToAnotherAccount(Long senderId, double amount,Long receiverId) {
        try {
            Optional<Customer> sender = customerRepository.findById(senderId);
            Optional<Customer> reciever = customerRepository.findById(receiverId);

            if (sender.isPresent()&&reciever.isPresent()) {
                Customer sender1 = sender.get();
                Customer recever1 = reciever.get();
                Account current = sender1.getCurrentAccount();
                Account savings = recever1.getSavingAccount();
            System.out.println(savings);
                if (current.getBalance() >= amount) {
                    double fee=amount*0.0005;
                    current.debit(amount-fee, fee);
                    savings.credit(amount-fee);
//                    this is for fees
//                    current.credit(savings.getBalance() * 0.0005); //  0.05% interest
                    customerRepository.save(sender1);
                    customerRepository.save(recever1);
                    return new ResponseEntity<>(new Response<String>("amount transfer successfully", ""), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new Response<String>("", "Insufficient funds in the current account."), HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(new Response<String>("", "Account Not Found."), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>( new Response<String>("", "Something went wrong"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Response<String>> makePayment(Long customerId, double amount) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            Account current = customer.getCurrentAccount();
            double fee = amount * 0.0005;
            if (current.getBalance() >= (amount + fee)) {
                current.debit(amount, fee);
                notifyCustomer(customer, amount, fee);
                customerRepository.save(customer);
                return new ResponseEntity<>(new Response<String>("payment successfully", ""), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Response<String>("", "Insufficient funds in the current account."), HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new Response<String>("", "Account Not Found."), HttpStatus.NOT_FOUND);
        }
    }

    private void notifyCustomer(Customer customer, double amount, double fee) {
        System.out.println("Notification sent to " + customer.getName() + ": Payment of R" + amount + " made. Fee: R" + fee);
    }

    public ResponseEntity<Response<Customer>> getCustomer(Long customerId) {
        try{
            Optional<Customer>cs = customerRepository.findById(customerId);
            if(cs.isEmpty()){
                return  new ResponseEntity<>(new Response<>(null,"Invalid customer Id"),HttpStatus.NOT_FOUND);
            }
           System.out.println(cs.get());
            return  new ResponseEntity<>( new Response<Customer>(cs.get(),""),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return   new ResponseEntity<>(new Response<>(null,"Something went wrong"),HttpStatus.NOT_FOUND);

        }

    }
}
