package mirum.generate.invoice.rest.module.application.DTO;

import com.liferay.expando.kernel.model.ExpandoBridge;

public class IdDTO {

    private long commerceOrderId;

    private String languageId;

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public long getCommerceOrderId() {
        return commerceOrderId;
    }

    public void setCommerceOrderId(long commerceOrderId) {
        this.commerceOrderId = commerceOrderId;
    }
}
