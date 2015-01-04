package co.edu.udea.android.omrgrader2_0.activity.session;

import android.app.ProgressDialog;

class UploaderProgressDialogThread extends Thread {

	private boolean running;

	private ProgressDialog progressDialog;

	public UploaderProgressDialogThread(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}

	@Override()
	public void run() {
		while (this.isRunning()) {
		}

		this.progressDialog.dismiss();
	}

	public boolean isRunning() {

		return (this.running);
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}