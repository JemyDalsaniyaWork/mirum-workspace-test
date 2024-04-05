package mirum.commerce.checkout.web.fragment.sdk.types;

/**
 * Provides the allowed languages on FORT.
 * Default is ENGLISH
 *
 */
public enum FortLanguage {

	Arabic("ar"), English("en");

	public String getCode() {
		return code;
	}

	private FortLanguage(String code) {
		this.setCode(code);
	}

	private void setCode(String code) {
		this.code = code;
	}

	private String code;

}