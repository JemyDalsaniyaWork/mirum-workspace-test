package mirum.commerce.checkout.web.fragment.sdk.parameter;

import java.util.Objects;

public class FortKey {

	public FortKey(String ketName) {
		this.ketName = ketName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)

			return true;

		if ((o == null) || (getClass() != o.getClass()))

			return false;
		FortKey fortKey = (FortKey)o;

		return Objects.equals(ketName, fortKey.ketName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ketName);
	}

	public String name() {
		return ketName;
	}

	@Override
	public String toString() {
		return "FortKey{" + "ketName='" + ketName + '\'' + '}';
	}

	private final String ketName;

}