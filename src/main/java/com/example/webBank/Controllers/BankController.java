package com.example.webBank.Controllers;

import com.example.webBank.Entity.Account;
import com.example.webBank.Entity.Transfer;
import com.example.webBank.Repository.AccountRepo;
import com.example.webBank.Repository.TransferRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class BankController {
    @Autowired
    AccountRepo accountRepo;

    @Autowired
    TransferRepo transferRepo;

    @GetMapping("/menu")
    public String menu(Model model) {
        long activeID = 0;
        Iterable<Account> findCurrentAccount = accountRepo.findAll();
        for (Account account : findCurrentAccount) {
            if (account.getActive() == 1) {
                model.addAttribute("account",Long.toString(account.getId()));
                break;
            }
        }

        return "main-page";
    }

    @GetMapping("/profile/{id}")
    public String profile(@PathVariable(value = "id") long id, Model model) {
        Optional<Account> account = accountRepo.findById(id);
        ArrayList<Account> res = new ArrayList<>();
        account.ifPresent(res::add);
        model.addAttribute("account",res);
        return "profile";
    }
    @GetMapping("/transfer")
    public String transfer() {
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transferData(@RequestParam("cardNumber") String cardNumber, @RequestParam("summ") int summ) {
        Iterable<Account> accounts = accountRepo.findAll();

        for (Account getterAccount : accounts) {
            // поиск аккаунта получателя
            if (getterAccount.getCardNumber().equals(cardNumber)) {

                for (Account senderAccount : accounts) {
                    // поиск аккаунта отправителя
                    if (senderAccount.getActive() == 1) {
                        // обновление данных аккаунтов

                        senderAccount.setMoney(senderAccount.getMoney() - summ);
                        getterAccount.setMoney(getterAccount.getMoney() + summ);

                        accountRepo.save(senderAccount);
                        accountRepo.save(getterAccount);

                        // создание объекта транзакции
                        Transfer transfer = new Transfer();

                        transfer.setGetterFullname(getterAccount.getFullname());
                        transfer.setSenderFullname(senderAccount.getFullname());

                        transfer.setSummSended(summ);

                        transferRepo.save(transfer);

                        break;
                    }
                }
                break;
            }
        }




        return "redirect:/menu";
    }

    @GetMapping("/history")
    public String history(Model model) {
        Iterable<Transfer> transfers = transferRepo.findAll();
        Iterable<Account> accounts = accountRepo.findAll();

        // поиск активного аккаунта
        long activeAccountID = 0;

        for (Account findActive : accounts) {
            if (findActive.getActive() == 1) {
                activeAccountID = findActive.getId();
                break;
            }
        }

        // поиск переводов которые связанны с активным пользователем
        List<Transfer> sendedTransfersList = new ArrayList<Transfer> ();
        List<Transfer> gettedTransfersList = new ArrayList<Transfer> ();

        for (Transfer sendedTransfer : transfers) {

            // отправленные
            if (sendedTransfer.getSenderFullname().equals(accountRepo.findById(activeAccountID).get().getFullname())) {
                sendedTransfersList.add(sendedTransfer);
            }

            // полученные
            if (sendedTransfer.getGetterFullname().equals(accountRepo.findById(activeAccountID).get().getFullname())) {
                gettedTransfersList.add(sendedTransfer);
            }
        }

        model.addAttribute("sendedTransfers",sendedTransfersList);
        model.addAttribute("gettedTransfers",gettedTransfersList);


        return "transfer-history";
    }

}
