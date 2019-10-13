package com.itbcafrica.service.UserServiceImpl;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itbcafrica.Dao.PrimaryAccountDao;
import com.itbcafrica.Dao.SavingsAccountDao;
import com.itbcafrica.domain.PrimaryAccount;
import com.itbcafrica.domain.PrimaryTransaction;
import com.itbcafrica.domain.SavingsAccount;
import com.itbcafrica.domain.SavingsTransaction;
import com.itbcafrica.domain.User;
import com.itbcafrica.service.AccountService;
import com.itbcafrica.service.TransactionService;
import com.itbcafrica.service.UserService;

@Service
public class AccountServiceImpl implements AccountService {
	private static int nextAccountNumber = 11223145;
	@Autowired
	private PrimaryAccountDao primaryAccountDao;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private SavingsAccountDao savingsAccountDao;
	@Autowired
	private UserService userService;

	@Override
	public PrimaryAccount createPrimaryAccount() {

		PrimaryAccount primaryAccount = new PrimaryAccount();
		primaryAccount.setAccountBalance(new BigDecimal(0.0));
		primaryAccount.setAccountNumber(accountGen());
		primaryAccountDao.save(primaryAccount);
		PrimaryAccount primaryAccountFromDB = primaryAccountDao.findByAccountNumber(primaryAccount.getAccountNumber());
		return primaryAccountFromDB;
	}

	@Override
	public SavingsAccount createSavingAccount() {

		SavingsAccount savingsAccount = new SavingsAccount();
		savingsAccount.setAccountBalance(new BigDecimal(0.0));
		savingsAccount.setAccountNumber(accountGen());
		savingsAccountDao.save(savingsAccount);
		SavingsAccount savingsAccountFromDB = savingsAccountDao.findByAccountNumber(savingsAccount.getAccountNumber());
		return savingsAccountFromDB;
	}

	private int accountGen() {
		return ++nextAccountNumber;
	}

	@Override
	public void deposit(String accountType, double amount, Principal principal) {
		User user = userService.findByUsername(principal.getName());

		if (accountType.equalsIgnoreCase("Primary")) {
			PrimaryAccount primaryAccount = user.getPrimaryAccount();
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);

			Date date = new Date();

			PrimaryTransaction primaryTransaction = new PrimaryTransaction("Deposit to Primary Account", date,
					"Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);
			transactionService.savePrimaryDepositTransaction(primaryTransaction);

		} else if (accountType.equalsIgnoreCase("Savings")) {
			SavingsAccount savingsAccount = user.getSavingsAccount();
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
			savingsAccountDao.save(savingsAccount);

			Date date = new Date();
			SavingsTransaction savingsTransaction = new SavingsTransaction("Deposit to savings Account", date,
					"Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
			transactionService.saveSavingsDepositTransaction(savingsTransaction);
		}
	}

	@Override
	public void withdraw(String accountType, double amount, Principal principal) {
		User user = userService.findByUsername(principal.getName());

		if (accountType.equalsIgnoreCase("Primary")) {
			PrimaryAccount primaryAccount = user.getPrimaryAccount();
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);

			Date date = new Date();

			PrimaryTransaction primaryTransaction = new PrimaryTransaction("Withdraw from Primary Account", date,
					"Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);
			transactionService.savePrimaryWithdrawTransaction(primaryTransaction);
		} else if (accountType.equalsIgnoreCase("Savings")) {
			SavingsAccount savingsAccount = user.getSavingsAccount();
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			savingsAccountDao.save(savingsAccount);

			Date date = new Date();
			SavingsTransaction savingsTransaction = new SavingsTransaction("Withdraw from savings Account", date,
					"Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
			transactionService.saveSavingsWithdrawTransaction(savingsTransaction);
		}
	}

}
