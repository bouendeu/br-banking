package com.itbcafrica.service;

import java.security.Principal;

import com.itbcafrica.domain.PrimaryAccount;
import com.itbcafrica.domain.SavingsAccount;

public interface AccountService {

	PrimaryAccount createPrimaryAccount();

	SavingsAccount createSavingAccount();

	void deposit(String accountType, double parseDouble, Principal principal);

	void withdraw(String accountType, double parseDouble, Principal principal);
}
