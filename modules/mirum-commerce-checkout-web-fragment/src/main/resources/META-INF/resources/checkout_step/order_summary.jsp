<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="/init.jsp" %>

<%@ page import="com.liferay.commerce.model.CommerceOrderItem" %><%@
page import="com.liferay.portal.kernel.util.LocaleUtil" %>

<%@ page import="java.util.Currency" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.math.RoundingMode" %>
<%@ page import="com.liferay.commerce.product.service.CPDefinitionLocalServiceUtil" %>
<%@ page import="com.liferay.expando.kernel.model.ExpandoBridge" %>


<%
CommerceContext commerceContext = (CommerceContext)request.getAttribute(CommerceWebKeys.COMMERCE_CONTEXT);

int AWSPaymentResult = (int)request.getAttribute("paymentStatus");

CommerceAccount commerceAccount = commerceContext.getCommerceAccount();

OrderSummaryCheckoutStepDisplayContext orderSummaryCheckoutStepDisplayContext = (OrderSummaryCheckoutStepDisplayContext)request.getAttribute(CommerceCheckoutWebKeys.COMMERCE_CHECKOUT_STEP_DISPLAY_CONTEXT);

CommerceOrder commerceOrder = orderSummaryCheckoutStepDisplayContext.getCommerceOrder();

CommerceOrderPrice commerceOrderPrice = orderSummaryCheckoutStepDisplayContext.getCommerceOrderPrice();

CommerceDiscountValue shippingDiscountValue = commerceOrderPrice.getShippingDiscountValue();
CommerceMoney shippingValueCommerceMoney = commerceOrderPrice.getShippingValue();
CommerceMoney subtotalCommerceMoney = commerceOrderPrice.getSubtotal();
CommerceDiscountValue subtotalCommerceDiscountValue = commerceOrderPrice.getSubtotalDiscountValue();
CommerceMoney taxValueCommerceMoney = commerceOrderPrice.getTaxValue();
CommerceDiscountValue totalCommerceDiscountValue = commerceOrderPrice.getTotalDiscountValue();
CommerceMoney totalOrderCommerceMoney = commerceOrderPrice.getTotal();

String priceDisplayType = orderSummaryCheckoutStepDisplayContext.getCommercePriceDisplayType();

if (priceDisplayType.equals(CommercePricingConstants.TAX_INCLUDED_IN_PRICE)) {
	shippingDiscountValue = commerceOrderPrice.getShippingDiscountValueWithTaxAmount();
	shippingValueCommerceMoney = commerceOrderPrice.getShippingValueWithTaxAmount();
	subtotalCommerceMoney = commerceOrderPrice.getSubtotalWithTaxAmount();
	subtotalCommerceDiscountValue = commerceOrderPrice.getSubtotalDiscountValueWithTaxAmount();
	totalCommerceDiscountValue = commerceOrderPrice.getTotalDiscountValueWithTaxAmount();
	totalOrderCommerceMoney = commerceOrderPrice.getTotalWithTaxAmount();
}
%>

<style>
	/*.table-list tbody th, .table-list tbody .data-background{
		background-color: rgba(235, 237, 241, 1)!important;
	}*/
	.table-list tbody th, .table-list tbody .data-background{
		background-color: #EBEDF1!important;
		color: #304F78;
	}
	.font-weight-labels {
		font-weight: bold;
	}
	.table-list tbody th, .table-list tbody .total-row-background {
		background-color: #F2FEFF;
		border: none !important;
	}
	.table-list {
		border-collapse: collapse;
	}
	.table td,
	.table th {
		position: relative;
		padding: 12px 30px !important;
	}
	.table tbody tr{
		height: auto;
	}
	.table tbody tr:not(:last-child) td {
		border-bottom: 1px solid #DFE6ED !important;
	}
	.table td::before,
	.table th::before {
		content: "";
		position: absolute;
		top: 50%;
		right: 0;
		left: 0;
		height: 20px;
		width: 1px;
		background-color: #DFE6ED;
		transform: translateY(-50%);
	}
	.table tr:last-child td::before {
		display: none;
	}
	.table td:first-child::before,
	.table th:first-child::before
	{
		display: none;
	}
</style>

<div class="commerce-order-summary">
	<liferay-ui:error exception="<%= CommerceDiscountLimitationTimesException.class %>" message="the-inserted-coupon-code-has-reached-its-usage-limit" />
	<liferay-ui:error exception="<%= CommerceOrderBillingAddressException.class %>" message="please-select-a-valid-billing-address" />
	<liferay-ui:error exception="<%= CommerceOrderGuestCheckoutException.class %>" message="you-must-sign-in-to-complete-this-order" />
	<liferay-ui:error exception="<%= CommerceOrderPaymentMethodException.class %>" message="please-select-a-valid-payment-method" />
	<liferay-ui:error exception="<%= CommerceOrderShippingAddressException.class %>" message="please-select-a-valid-shipping-address" />
	<liferay-ui:error exception="<%= CommerceOrderShippingMethodException.class %>" message="please-select-a-valid-shipping-method" />
	<liferay-ui:error exception="<%= NoSuchDiscountException.class %>" message="the-inserted-coupon-is-no-longer-valid" />

	<div class="align-items-center row">
		<div class="col-md-12 commerce-checkout-summary">
			<div class="commerce-checkout-summary-body" id="<portlet:namespace />entriesContainer">
				<div class="table-responsive">
					<table class="show-quick-actions-on-hover table table-autofit table-heading-nowrap table-list" data-searchcontainerid="_com_liferay_commerce_checkout_web_internal_portlet_CommerceCheckoutPortlet_commerceOrderItems">
						<thead>
							<tr>
								<th class="autofit-col-expand lfr-product-column font-weight-labels" id="_com_liferay_commerce_checkout_web_internal_portlet_CommerceCheckoutPortlet_commerceOrderItems_col-product">
<%--									<strong>Product</strong>--%>
									<strong><liferay-ui:message key="service-name"/></strong>
								</th>
								<th class="lfr-quantity-column font-weight-labels" id="_com_liferay_commerce_checkout_web_internal_portlet_CommerceCheckoutPortlet_commerceOrderItems_col-quantity">
<%--									<strong>Quantity</strong>--%>
									<strong><liferay-ui:message key="quantity"/></strong>
								</th>
								<th class="lfr-price-column font-weight-labels" id="_com_liferay_commerce_checkout_web_internal_portlet_CommerceCheckoutPortlet_commerceOrderItems_col-price">
<%--									<strong>Price</strong>--%>
									<strong><liferay-ui:message key="price"/></strong>
								</th>
<%--								<th class="lfr-discount-column" id="_com_liferay_commerce_checkout_web_internal_portlet_CommerceCheckoutPortlet_commerceOrderItems_col-discount">
									<strong>Discount</strong>
									<strong><liferay-ui:message key="discount"/></strong>
								</th>--%>
								<th class="lfr-quantity-column font-weight-labels">
<%--									<strong>VAT</strong>--%>
									<strong><liferay-ui:message key="vat"/></strong>
								</th>
								<th class="lfr-total-column font-weight-labels" id="_com_liferay_commerce_checkout_web_internal_portlet_CommerceCheckoutPortlet_commerceOrderItems_col-total" style="color: rgba(55, 186, 198, 1);">
<%--									<strong>Total</strong>--%>
									<strong><liferay-ui:message key="total"/></strong>
								</th>
							</tr>
						</thead>

						<tbody>

							<%
								int primaryProductQuantity = 0;
								int secondaryServiceQuantity = 0;
								boolean hasPoint = false;
								String finalQuantity = "";
								boolean isMultiProduct = false;

							for (CommerceOrderItem commerceOrderItem : commerceOrder.getCommerceOrderItems()) {
								hasPoint = false;
								isMultiProduct = commerceOrder.getCommerceOrderItems().size() > 1 ? true : false;
								CPDefinition cpDefinition = commerceOrderItem.getCPDefinition();
								CommerceProductPrice commerceProductPrice = orderSummaryCheckoutStepDisplayContext.getCommerceProductPrice(commerceOrderItem);
								CPInstance cpInstance = commerceOrderItem.fetchCPInstance();

								ExpandoBridge expandoBridgeQuantity = CPDefinitionLocalServiceUtil.getCPDefinition(cpDefinition.getCPDefinitionId()).getExpandoBridge();
								if (expandoBridgeQuantity.hasAttribute("hasPoint") && expandoBridgeQuantity.getAttribute("hasPoint").equals(false)){
									primaryProductQuantity = commerceOrderItem.getQuantity();
								} else if (expandoBridgeQuantity.hasAttribute("hasPoint") && expandoBridgeQuantity.getAttribute("hasPoint").equals(true)) {
									secondaryServiceQuantity = commerceOrderItem.getQuantity();
									hasPoint = true;
								}
								if (primaryProductQuantity != 0) {
									finalQuantity = secondaryServiceQuantity / primaryProductQuantity + "x" + primaryProductQuantity;
								} else {
									finalQuantity = "N/A";
								}
							%>

								<tr class=" " data-qa-id="row">
									<td class="autofit-col-expand lfr-product-column data-background" colspan="1">
										<div class="description-section">
											<div>
												<%= HtmlUtil.escape(cpDefinition.getName(themeDisplay.getLanguageId())) %>
											</div>

											<%
											StringJoiner stringJoiner = new StringJoiner(StringPool.COMMA);

											for (KeyValuePair keyValuePair : orderSummaryCheckoutStepDisplayContext.getKeyValuePairs(commerceOrderItem.getCPDefinitionId(), commerceOrderItem.getJson(), locale)) {
												stringJoiner.add(keyValuePair.getValue());
											}
											%>

											<div class="list-group-subtitle"><%= HtmlUtil.escape(stringJoiner.toString()) %></div>

											<%
											Map<Long, List<CommerceOrderValidatorResult>> commerceOrderValidatorResultsMap = orderSummaryCheckoutStepDisplayContext.getCommerceOrderValidatorResultsMap();
											%>

											<c:if test="<%= !commerceOrderValidatorResultsMap.isEmpty() %>">

												<%
												List<CommerceOrderValidatorResult> commerceOrderValidatorResults = commerceOrderValidatorResultsMap.get(commerceOrderItem.getCommerceOrderItemId());

												for (CommerceOrderValidatorResult commerceOrderValidatorResult : commerceOrderValidatorResults) {
												%>

													<div class="alert-danger commerce-alert-danger">
														<liferay-ui:message key="<%= HtmlUtil.escape(commerceOrderValidatorResult.getLocalizedMessage()) %>" />
													</div>

												<%
												}
												%>

											</c:if>
										</div>
									</td>
									<td class="lfr-quantity-column data-background" colspan="1">
										<div class="quantity-section">
											<c:choose>
												<c:when test="<%=hasPoint%>">
													<span class="commerce-quantity"><%= finalQuantity %></span>
												</c:when>
												<c:otherwise>
													<span class="commerce-quantity"><%= commerceOrderItem.getQuantity() %></span>
												</c:otherwise>
											</c:choose>
										</div>
									</td>
									<td class="lfr-price-column data-background" colspan="1">
										<c:if test="<%= commerceProductPrice != null %>">

											<%
											CommerceMoney unitPriceCommerceMoney = commerceProductPrice.getUnitPrice();
											CommerceMoney unitPromoPriceCommerceMoney = commerceProductPrice.getUnitPromoPrice();

											if (priceDisplayType.equals(CommercePricingConstants.TAX_INCLUDED_IN_PRICE)) {
												unitPriceCommerceMoney = commerceProductPrice.getUnitPriceWithTaxAmount();
												unitPromoPriceCommerceMoney = commerceProductPrice.getUnitPromoPriceWithTaxAmount();
											}
											%>

											<div class="value-section">
												<span style="align-items: center; display: block;">
													<c:choose>
														<c:when test="<%= !unitPromoPriceCommerceMoney.isEmpty() && CommerceBigDecimalUtil.gt(unitPromoPriceCommerceMoney.getPrice(), BigDecimal.ZERO) %>">
<%--															<span class="price-value price-value-promo">--%>
															<span>
																<%= HtmlUtil.escape(unitPromoPriceCommerceMoney.format(Locale.ENGLISH)) %> SAR
															</span>
														</c:when>
														<c:otherwise>
															<span>
																<%= HtmlUtil.escape(unitPriceCommerceMoney.format(Locale.ENGLISH)) %> SAR
															</span>
														</c:otherwise>
													</c:choose>
												</span>

												<c:if test="<%= (cpInstance != null) && Validator.isNotNull(cpInstance.getCPSubscriptionInfo()) %>">
													<span class="commerce-subscription-info">
														<commerce-ui:product-subscription-info
															CPInstanceId="<%= commerceOrderItem.getCPInstanceId() %>"
															showDuration="<%= false %>"
														/>
													</span>
												</c:if>
											</div>
										</c:if>
									</td>
								<%--	<td class="lfr-discount-column data-background" colspan="1">
										<c:if test="<%= commerceProductPrice != null %>">

											<%
											CommerceDiscountValue discountValue = commerceProductPrice.getDiscountValue();

											if (priceDisplayType.equals(CommercePricingConstants.TAX_INCLUDED_IN_PRICE)) {
												discountValue = commerceProductPrice.getDiscountValueWithTaxAmount();
											}

											CommerceMoney discountAmountCommerceMoney = null;

											if (discountValue != null) {
												discountAmountCommerceMoney = discountValue.getDiscountAmount();
											}
											%>

											<div class="value-section">
												<span class="commerce-value">
													<%= (discountAmountCommerceMoney == null) ? "0%" : HtmlUtil.escape(discountAmountCommerceMoney.format(Locale.ENGLISH)) %>
												</span>
											</div>
										</c:if>
									</td>--%>
									<td class="lfr-tax-column data-background" colspan="1">
									    <c:choose>
                                            <c:when test="<%=hasPoint && primaryProductQuantity != 0%>">
                                                    <%= ((commerceOrderItem.getUnitPriceWithTaxAmount().subtract(commerceOrderItem.getUnitPrice())).setScale(2, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf((secondaryServiceQuantity / primaryProductQuantity) * primaryProductQuantity)) %>
                                            </c:when>
                                            <c:otherwise>
                                                <%= ((commerceOrderItem.getUnitPriceWithTaxAmount().subtract(commerceOrderItem.getUnitPrice())).setScale(2, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(commerceOrderItem.getQuantity())) %>
                                            </c:otherwise>
                                        </c:choose>
									    SAR
									</td>
									<td class="lfr-total-column data-background" colspan="1" style="color: rgba(55, 186, 198, 1);">
										<c:if test="<%= commerceProductPrice != null %>">

											<%
											CommerceMoney finalPriceCommerceMoney = commerceProductPrice.getFinalPrice();

											if (priceDisplayType.equals(CommercePricingConstants.TAX_INCLUDED_IN_PRICE)) {
												finalPriceCommerceMoney = commerceProductPrice.getFinalPriceWithTaxAmount();
											}

											Currency currency = Currency.getInstance(LocaleUtil.getDefault());
											%>

											<div class="value-section">
												<span class="commerce-value">
<%--													<%= totalOrderCommerceMoney.getPrice().setScale(2, RoundingMode.HALF_UP)%>--%>
<%--													<%= HtmlUtil.escape(totalOrderCommerceMoney.format(Locale.ENGLISH)) %> SAR--%>
													<%= HtmlUtil.escape(commerceOrderItem.getFinalPriceWithTaxAmountMoney().format(Locale.ENGLISH)) %> SAR
												</span>
											</div>
										</c:if>
									</td>
								</tr>

							<%
							}
							%>
							<tr class="<%= isMultiProduct ? "" : "hide" %>" data-qa-id="row">
								<td class="total-row-background font-weight-labels" colspan="1"><liferay-ui:message key="order-total-value"/></td>
								<td class="total-row-background" colspan="1"></td>
								<td class="total-row-background" colspan="1"></td>
								<td class="total-row-background" colspan="1"></td>
								<td class="total-row-background" colspan="1"
								<%--									style="color: rgba(55, 186, 198, 1); border: none !important;">--%>
									style="color: #37BAC6 !important;">
									<div class="value-section">
										<span class="commerce-value font-weight-labels">
										<%= totalOrderCommerceMoney.getPrice().setScale(2, RoundingMode.HALF_UP)%> SAR
										</span>
									</div>
								</td>
							</tr>

						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<%
	if (AWSPaymentResult == 4) {
	%>

		<h5>
			<liferay-ui:message key="payment-has-failed-please-retry" />
		</h5>

	<%
	}
	%>

</div>

