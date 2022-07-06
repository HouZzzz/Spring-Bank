package com.example.webBank.Controllers;

import com.example.webBank.Entity.Account;
import com.example.webBank.Repository.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.YearMonth;
import java.util.Locale;
import java.util.Random;

@Controller
public class AccountController {

    @Autowired
    AccountRepo accountRepo;

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registrationLogic(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("passwordConfirm") String passwordConfirm, @RequestParam("name") String name, @RequestParam("surname") String surname) {
        Random random = new Random();

        // проверка на существование аккаунта с таким же именем пользователя
        Iterable<Account> accounts = accountRepo.findAll();
        for (Account account : accounts) {
            if(account.getUsername().equals(username)) {
                return "redirect:/";
            }
        }
        if (password.equals(passwordConfirm)) {
            // создания нового аккаунта
            Account newAccount = new Account();

            // генерация номера карты
            String cardNumber = "";

            for (int i = 1;i <= 4;i++) {
                int num = random.nextInt(9999);
                if (Integer.toString(num).length() < 4) {
                    while (Integer.toString(num).length() != 4) {
                        num = num * 10;
                    }
                }
                cardNumber += Integer.toString(num);
                cardNumber += " ";
            }
            cardNumber.trim();

            // генерация срока карты
            String cardTerm = Integer.parseInt(YearMonth.now().toString().split("-")[0]) - 2000 + 4 + "/" + YearMonth.now().toString().split("-")[1];

            // генерация cvc
            int cvc = random.nextInt(999);
            while (Integer.toString(cvc).length() != 3) {
                cvc = cvc * 10;
            }

            // заполнение аккаунта данными
            name = name.toLowerCase();
            surname = surname.toLowerCase();

            String capitalizeFullname = "";
            for (int i = 0; i < name.length(); i++) {
                if (i == 0) {
                    capitalizeFullname += name.split("")[0].toUpperCase();
                } else {
                    capitalizeFullname += name.split("")[i];
                }
            }
            capitalizeFullname += " ";

            for (int i = 0; i < surname.length(); i++) {
                if (i == 0) {
                    capitalizeFullname += surname.split("")[0].toUpperCase();
                } else {
                    capitalizeFullname += surname.split("")[i];
                }
            }

            newAccount.setCvc(cvc);
            newAccount.setFullname(capitalizeFullname);
            newAccount.setUsername(username);
            newAccount.setPassword(password);
            newAccount.setCardNumber(cardNumber.trim());
            newAccount.setCardTerm(cardTerm);

            // активность аккаунта
            Iterable<Account> updateActive = accountRepo.findAll();
            for (Account account : updateActive) {
                if (account.getActive() == 1) {
                    account.setActive(0);
                    break;
                }
            }
            newAccount.setActive(1);

            accountRepo.save(newAccount);


            return "redirect:/menu";
        }

        return "redirect:/registration";
    }

    @PostMapping("/")
    public String loginLogic(@RequestParam("username") String username,@RequestParam("password") String password) {
        Iterable<Account> accounts = accountRepo.findAll();

        for (Account account : accounts) {
            if (account.getActive() == 1) {
                account.setActive(0);
            }
        }
        for (Account account : accounts) {
            if (account.getUsername().equals(username)) {
                account.setActive(1);
                accountRepo.save(account);
            }
        }

        for (Account account : accounts) {
            if (account.getUsername().equals(username)) {
                if (account.getPassword().equals(password)) {
                    return "redirect:/menu";
                }
            }
        }


        return "redirect:/";
    }



}
