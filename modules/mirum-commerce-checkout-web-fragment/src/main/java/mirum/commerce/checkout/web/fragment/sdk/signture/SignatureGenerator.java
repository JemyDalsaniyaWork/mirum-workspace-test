package mirum.commerce.checkout.web.fragment.sdk.signture;

import java.nio.charset.Charset;

import mirum.commerce.checkout.web.fragment.sdk.hash.HashingFunction;
import mirum.commerce.checkout.web.fragment.sdk.parameter.FortParameter;

/**
 * The class is responsible to generate fort signature
 */
public final class SignatureGenerator {

	public SignatureGenerator(
		HashingFunction hashingFunction, Charset charset) {

		this.hashingFunction = hashingFunction;
		this.charset = charset;
	}

	/**
	 * @param phrase
	 * @param parameters
	 * @return hashed string include all passed parameters and phrase
	 */
	public String generate(String phrase, FortParameter parameters) {
		return hashingFunction.hash(
			prepareStringToHash(phrase, parameters), charset
		).toUpperCase();
	}

	private String concatenatedString(String phrase, FortParameter parameters) {
		StringBuilder sb = new StringBuilder(phrase);

		parameters.iterator(
			(k, v) -> {
				sb.append(k.name() + "=" + v);
			});
		sb.append(phrase);

		return sb.toString();
	}

	private String prepareStringToHash(
		String phrase, FortParameter parameters) {

		String concatenatedString = concatenatedString(phrase, parameters);

		return concatenatedString;
	}

	private final Charset charset;
	private final HashingFunction hashingFunction;

}