package co.edu.udea.android.omrgrader2_0.activity.session.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import co.edu.udea.android.omrgrader2_0.R;

public class SessionNameDialogFragment extends DialogFragment {

	private EditText sessionNameEditText;
	private NoticeDialogListener noticeDialogListener;

	public SessionNameDialogFragment() {
		super();
	}

	@Override()
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		this.noticeDialogListener = (NoticeDialogListener) activity;
	}

	@Override()
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				super.getActivity());

		LayoutInflater layoutInflater = super.getActivity().getLayoutInflater();
		View view = layoutInflater.inflate(R.layout.dialog_session_name, null);

		alertDialogBuilder.setView(view);
		alertDialogBuilder.setTitle(R.string.session_name_title);
		alertDialogBuilder.setPositiveButton(
				super.getString(R.string.accept_button_label),
				new DialogInterface.OnClickListener() {

					@Override()
					public void onClick(DialogInterface dialog, int id) {
						noticeDialogListener
								.onDialogFragmentPositiveClick(SessionNameDialogFragment.this);
					}
				});
		alertDialogBuilder.setNegativeButton(
				super.getString(R.string.cancel_button_label),
				new DialogInterface.OnClickListener() {

					@Override()
					public void onClick(DialogInterface dialog, int id) {
						noticeDialogListener
								.onDialogFragmentNegativeClick(SessionNameDialogFragment.this);
					}
				});

		this.setSessionNameEditText((EditText) view
				.findViewById(R.id.session_name_edit_text));

		return (alertDialogBuilder.create());
	}

	public EditText getSessionNameEditText() {

		return (this.sessionNameEditText);
	}

	private void setSessionNameEditText(EditText sessionNameEditText) {
		this.sessionNameEditText = sessionNameEditText;
	}

	public interface NoticeDialogListener {

		public void onDialogFragmentNegativeClick(DialogFragment dialogFragment);

		public void onDialogFragmentPositiveClick(DialogFragment dialogFragment);
	}
}