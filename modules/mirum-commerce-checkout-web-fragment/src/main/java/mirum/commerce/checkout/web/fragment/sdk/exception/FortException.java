package mirum.commerce.checkout.web.fragment.sdk.exception;

public final class FortException extends RuntimeException {

	public FortException() {
	}

	public FortException(String message) {
		super(message);
	}

	public FortException(String message, Throwable cause) {
		super(message, cause);
	}

	public FortException(
		String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {

		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FortException(Throwable cause) {
		super(cause);
	}

}