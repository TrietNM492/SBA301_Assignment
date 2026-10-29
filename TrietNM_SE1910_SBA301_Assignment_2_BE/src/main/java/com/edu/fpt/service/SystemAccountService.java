package com.edu.fpt.service;

import com.edu.fpt.domain.entity.SystemAccount;
import com.edu.fpt.dto.request.AccountRegister;
import com.edu.fpt.dto.request.AccountRequest;
import com.edu.fpt.dto.request.ChangePasswordRequest;
import com.edu.fpt.dto.request.ProfileRequest;
import com.edu.fpt.dto.response.AccountReponse;
import com.edu.fpt.dto.response.ProfileResponse;
import com.edu.fpt.exception.EmailAlreadyExistException;
import com.edu.fpt.exception.InvalidFieldInputException;
import com.edu.fpt.exception.ResourceNotFoundException;
import com.edu.fpt.repository.SystemAccountRepository;
import com.edu.fpt.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class SystemAccountService {
    private final SystemAccountRepository systemAccountRepository;


    public ProfileResponse viewProfile(Long userId) {
        SystemAccount userFounded = systemAccountRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        return toResponse(userFounded);
    }

    @Transactional
    public ProfileResponse changeUserName(ProfileRequest newName, Long userId) {
        int updated = systemAccountRepository.updateUserName(newName.accountName(), userId);
        if (updated < 1) {
            throw new RuntimeException("Can't update or not found user");
        }
        SystemAccount userFounded = systemAccountRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        return toResponse(userFounded);
    }

    @Transactional
    public ProfileResponse changeUserPassword(ChangePasswordRequest request, Long userId) {
        SystemAccount userFounded = systemAccountRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        if (checkPass(userFounded.getAccountPassword(), request.oldPass())) {
            userFounded.setAccountPassword(request.newPass());
            return toResponse(userFounded);
        } else {
            throw new RuntimeException("Password not match");
        }

    }

    public PageResponse<AccountReponse> viewAccounts(String key, Pageable pageable) {
        Page<SystemAccount> systemAccounts = systemAccountRepository
                .findSystemAccountByAccountEmailIgnoreCaseOrderBy(key, pageable);
        Page<AccountReponse> accountReponses = systemAccounts.map(this::toAccounts);
        return new  PageResponse<>(accountReponses);
    }

    @Transactional
    public AccountReponse register(AccountRegister register) {
        if (systemAccountRepository.existsByAccountEmailIgnoreCase(register.accountEmail())) {
            throw new EmailAlreadyExistException("Email Exist!");
        }
        if (register.password().equals(register.password2nd())) {
            throw new RuntimeException("Password not match!");
        }
        SystemAccount account = new SystemAccount();
        account.setAccountName(register.accountName());
        account.setAccountEmail(register.accountEmail());
        account.setAccountRole(2);
        account.setAccountPassword(register.password());
        return toAccounts(account);
    }

    @Transactional
    public AccountReponse createAccount(AccountRequest request) {
        if (systemAccountRepository.existsByAccountEmailIgnoreCase(request.accountEmail())) {
            throw new EmailAlreadyExistException("Email Exist!");
        }
        SystemAccount account = new SystemAccount();
        account.setAccountName(request.accountName());
        account.setAccountRole(request.accountRole());
        account.setAccountEmail(request.accountEmail());
        account.setAccountPassword(randomPassword());
        systemAccountRepository.save(account);
        return toAccounts(account);
    }

    private String randomPassword() {
        String prefix = "@";
        Random random = new Random();
        int number = random.nextInt(100);
        return prefix + number;
    }

    private boolean checkPass(String currentPass, String currentPassInput) {
        return currentPass.equals(currentPassInput);
    }

    private ProfileResponse toResponse(SystemAccount systemAccount) {
        return new ProfileResponse(systemAccount.getId(), systemAccount.getAccountName(), systemAccount.getAccountEmail(), systemAccount.getAccountRole());
    }

    private AccountReponse toAccounts(SystemAccount systemAccount) {
        return new AccountReponse(systemAccount.getId(), systemAccount.getAccountName(), systemAccount.getAccountEmail(), systemAccount.getAccountRole());
    }


}
