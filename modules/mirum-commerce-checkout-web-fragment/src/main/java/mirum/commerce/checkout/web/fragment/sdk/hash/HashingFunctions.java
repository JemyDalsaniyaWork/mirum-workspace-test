package mirum.commerce.checkout.web.fragment.sdk.hash;

import static com.google.common.hash.Hashing.sha256;
import static com.google.common.hash.Hashing.sha512;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Implementation for @{@link HashingFunction} interface using google implementation
 * *** In case you don't like to have guava in your classpath then set any implementation for hashing using {@link FortRequestConfiguration}
 */
public enum HashingFunctions {

	SHA_256(
		(string, charset) -> sha256(
		).hashString(
			string, charset
		).toString()),
	SHA_512(
		(string, charset) -> sha512(
		).hashString(
			string, charset
		).toString());

	public HashingFunction hashingFunction() {
		return hashingFunction;
	}

	HashingFunctions(HashingFunction hashingFunction) {
		this.hashingFunction = hashingFunction;
	}

	public String hashString(String value) {
		return hashingFunction.hash(value, UTF_8);
	}

	private final HashingFunction hashingFunction;

}