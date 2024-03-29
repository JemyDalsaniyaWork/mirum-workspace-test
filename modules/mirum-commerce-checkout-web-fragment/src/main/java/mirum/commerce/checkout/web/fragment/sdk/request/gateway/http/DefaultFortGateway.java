package mirum.commerce.checkout.web.fragment.sdk.request.gateway.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import mirum.commerce.checkout.web.fragment.sdk.exception.FortException;
import mirum.commerce.checkout.web.fragment.sdk.request.gateway.FortGateway;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;

/**
 * Implementation for @{@link FortGateway } interface using Apache HttpClient
 * *** In case you don't like to have Apache HttpClient in your classpath then set any implementation for calling fort using {@link FortRequestConfiguration}
 */
public final class DefaultFortGateway implements FortGateway {

	public DefaultFortGateway(String url, String contentType) {
		this.url = url;
		this.contentType = contentType;
	}

	public String send(String content) {
		try {
			HttpUriRequest request = prepareRequest(content);

			return sendRequest(request);
		}
		catch (IOException ex) {
			throw new FortException("Error while calling fort : " + url, ex);
		}
	}

	private HttpPost prepareRequest(String content)
		throws UnsupportedEncodingException {

		HttpPost request = new HttpPost(url);
		StringEntity params = new StringEntity(content);
		request.addHeader(CONTENT_TYPE, contentType);
		request.setEntity(params);

		return request;
	}

	private String processResponse(HttpResponse response) throws IOException {
		StringBuilder sb = new StringBuilder();

		try (InputStreamReader inputStreamReader = new InputStreamReader(
				response.getEntity(
				).getContent());
			BufferedReader reader = new BufferedReader(
				inputStreamReader, Short.MAX_VALUE)) {

			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			return sb.toString();
		}
	}

	private String sendRequest(HttpUriRequest request) throws IOException {
		try (CloseableHttpClient httpClient = HttpClientBuilder.create(
			).setRedirectStrategy(
				new LaxRedirectStrategy()
			).build()) {

			return processResponse(httpClient.execute(request));
		}
	}

	private static final String CONTENT_TYPE = "content-type";

	private final String contentType;
	private final String url;

}