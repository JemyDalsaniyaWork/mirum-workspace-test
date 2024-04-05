package mirum.commerce.checkout.web.fragment.sdk;

import static mirum.commerce.checkout.web.fragment.sdk.hash.HashingFunctions.SHA_256;

import mirum.commerce.checkout.web.fragment.sdk.hash.HashingFunction;

/**
 * To hold the FORT account security credentials
 * -   accessCode
 * -   merchantIdentifier
 * -   shaRequestPhrase
 * -   shaResponsePhrase
 */
public class FortAccount {

	/**
	 * with default hashing as SHA_256
	 *
	 * @param accessCode
	 * @param merchantIdentifier
	 * @param shaRequestPhrase
	 * @param shaResponsePhrase
	 */
	public FortAccount(
		String accessCode, String merchantIdentifier, String shaRequestPhrase,
		String shaResponsePhrase) {

		this.accessCode = accessCode;
		this.merchantIdentifier = merchantIdentifier;
		this.shaRequestPhrase = shaRequestPhrase;
		this.shaResponsePhrase = shaResponsePhrase;

		this.hashingFunction = SHA_256.hashingFunction();
	}

	public FortAccount(
		String accessCode, String merchantIdentifier, String shaRequestPhrase,
		String shaResponsePhrase, HashingFunction hashingFunction) {

		this.accessCode = accessCode;
		this.merchantIdentifier = merchantIdentifier;
		this.shaRequestPhrase = shaRequestPhrase;
		this.shaResponsePhrase = shaResponsePhrase;
		this.hashingFunction = hashingFunction;
	}

	public String getAccessCode() {
		return accessCode;
	}

	public HashingFunction getHashingFunction() {
		return hashingFunction;
	}

	public String getMerchantIdentifier() {
		return merchantIdentifier;
	}

	public String getShaRequestPhrase() {
		return shaRequestPhrase;
	}

	public String getShaResponsePhrase() {
		return shaResponsePhrase;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	/**
	 * Used to override hashing logic
	 * Your Implementation must be thread safe
	 */
	public void setHashingFunction(HashingFunction hashingFunction) {
		this.hashingFunction = hashingFunction;
	}

	public void setMerchantIdentifier(String merchantIdentifier) {
		this.merchantIdentifier = merchantIdentifier;
	}

	public void setShaRequestPhrase(String shaRequestPhrase) {
		this.shaRequestPhrase = shaRequestPhrase;
	}

	public void setShaResponsePhrase(String shaResponsePhrase) {
		this.shaResponsePhrase = shaResponsePhrase;
	}

	private String accessCode;
	private HashingFunction hashingFunction;
	private String merchantIdentifier;
	private String shaRequestPhrase;
	private String shaResponsePhrase;

}