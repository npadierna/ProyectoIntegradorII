package co.edu.udea.android.omrgrader2_0.util;

import java.util.ArrayList;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import co.edu.udea.android.omrgrader2_0.util.validator.RegexValidator;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public final class EMailAccountManager {

	private EMailAccountManager() {
		super();
	}

	public static List<CharSequence> findAllEMailsAccount(Context context) {
		AccountManager accountManager = AccountManager.get(context);
		Account[] accounts = accountManager.getAccounts();
		List<CharSequence> eMailAccoutsList = new ArrayList<CharSequence>();

		for (Account account : accounts) {
			if ((RegexValidator.isValidEMail(account.name))
					&& (!eMailAccoutsList.contains(account.name))) {
				//eMailAccoutsList.add(account.name);
			}
		}

		return (eMailAccoutsList);
	}
}