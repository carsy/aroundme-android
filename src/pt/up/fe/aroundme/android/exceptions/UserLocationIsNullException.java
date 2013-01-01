package pt.up.fe.aroundme.android.exceptions;

public class UserLocationIsNullException extends Exception {
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return super.getMessage() + ": user location null";
	}

}
