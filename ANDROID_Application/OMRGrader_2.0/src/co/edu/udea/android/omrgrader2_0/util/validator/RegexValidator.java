package co.edu.udea.android.omrgrader2_0.util.validator;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * 
 * @author Andersson Garc&iacute;a Sotelo
 * @author Miguel &Aacute;ngel Ossa Ruiz
 * @author Neiber Padierna P&eacute;rez
 */
public final class RegexValidator {

	private RegexValidator() {
		super();
	}

	public static boolean isValidEMail(CharSequence eMailCharSequence) {

		return ((!TextUtils.isEmpty(eMailCharSequence)) & (Patterns.EMAIL_ADDRESS
				.matcher(eMailCharSequence).matches()));
	}
}