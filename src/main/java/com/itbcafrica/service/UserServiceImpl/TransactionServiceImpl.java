package com.itbcafrica.service.UserServiceImpl;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itbcafrica.Dao.PrimaryAccountDao;
import com.itbcafrica.Dao.PrimaryTransactionDao;
import com.itbcafrica.Dao.RecipientDao;
import com.itbcafrica.Dao.SavingsAccountDao;
import com.itbcafrica.Dao.SavingsTransactionDao;
import com.itbcafrica.domain.PrimaryAccount;
import com.itbcafrica.domain.PrimaryTransaction;
import com.itbcafrica.domain.Recipient;
import com.itbcafrica.domain.SavingsAccount;
import com.itbcafrica.domain.SavingsTransaction;
import com.itbcafrica.domain.User;
import com.itbcafrica.service.TransactionService;
import com.itbcafrica.service.UserService;

@Service
public class TransactionServiceImpl implements TransactionService {
	@Autowired
	private UserService userService;

	@Autowired
	private PrimaryTransactionDao primaryTransactionDao;

	@Autowired
	private SavingsTransactionDao savingsTransactionDao;

	@Autowired
	private PrimaryAccountDao primaryAccountDao;

	@Autowired
	private SavingsAccountDao savingsAccountDao;

	@Autowired
	private RecipientDao recipientDao;

	@Override
	public List<PrimaryTransaction> findPrimaryTransactionList(String username) {
		User user = userService.findByUsername(username);
		List<PrimaryTransaction> primaryTransactionList = user.getPrimaryAccount().getPrimaryTransactionList();

		return primaryTransactionList;
	}

	@Override
	public List<SavingsTransaction> findSavingsTransactionList(String username) {
		User user = userService.findByUsername(username);
		List<SavingsTransaction> savingsTransactionList = user.getSavingsAccount().getSavingsTransactionList();

		return savingsTransactionList;
	}

	public void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction) {
		primaryTransactionDao.save(primaryTransaction);
	}

	public void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction) {
		savingsTransactionDao.save(savingsTransaction);
	}

	public void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction) {
		primaryTransactionDao.save(primaryTransaction);
	}

	public void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction) {
		savingsTransactionDao.save(savingsTransaction);
	}

	public void betweenAccountsTransfer(String transferFrom, String transferTo, String amount,
			PrimaryAccount primaryAccount, SavingsAccount savingsAccount) throws Exception {
		if (transferFrom.equalsIgnoreCase("Primary") && transferTo.equalsIgnoreCase("Savings")) {
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);
			savingsAccountDao.save(savingsAccount);

			Date date = new Date();

			PrimaryTransaction primaryTransaction = new PrimaryTransaction(
					"Between account transfer from " + transferFrom + " to " + transferTo, date, "Account", "Finished",
					Double.parseDouble(amount), primaryAccount.getAccountBalance(), primaryAccount);
			primaryTransactionDao.save(primaryTransaction);
		} else if (transferFrom.equalsIgnoreCase("Savings") && transferTo.equalsIgnoreCase("Primary")) {
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);
			savingsAccountDao.save(savingsAccount);

			Date date = new Date();

			SavingsTransaction savingsTransaction = new SavingsTransaction(
					"Between account transfer from " + transferFrom + " to " + transferTo, date, "Transfer", "Finished",
					Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);
			savingsTransactionDao.save(savingsTransaction);
		} else {
			throw new Exception("Invalid Transfer");
		}
	}

	public List<Recipient> findRecipientList(Principal principal) {
		String username = principal.getName();
		List<Recipient> recipientList = ((List<Recipient>) recipientDao.findAll()).stream()
				.filter(recipient -> username.equals(recipient.getUser().getUsername())) // filters the line, equals to
																							// username
				.collect(Collectors.toList());

		return recipientList;
	}

	public Recipient saveRecipient(Recipient recipient) {
		return recipientDao.save(recipient);
	}

	public Recipient findRecipientByName(String recipientName) {
		return recipientDao.findByName(recipientName);
	}

	public void deleteRecipientByName(String recipientName) {
		recipientDao.deleteByName(recipientName);
	}

	public void toSomeoneElseTransfer(Recipient recipient, String accountType, String amount,
			PrimaryAccount primaryAccount, SavingsAccount savingsAccount) {
		if (accountType.equalsIgnoreCase("Primary")) {
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);

			Date date = new Date();

			PrimaryTransaction primaryTransaction = new PrimaryTransaction(
					"Transfer to recipient " + recipient.getName(), date, "Transfer", "Finished",
					Double.parseDouble(amount), primaryAccount.getAccountBalance(), primaryAccount);
			primaryTransactionDao.save(primaryTransaction);
		} else if (accountType.equalsIgnoreCase("Savings")) {
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			savingsAccountDao.save(savingsAccount);

			Date date = new Date();

			SavingsTransaction savingsTransaction = new SavingsTransaction(
					"Transfer to recipient " + recipient.getName(), date, "Transfer", "Finished",
					Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);
			savingsTransactionDao.save(savingsTransaction);
		}
	}
}
