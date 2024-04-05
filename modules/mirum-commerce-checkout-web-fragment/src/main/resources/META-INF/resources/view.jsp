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
<%@ page import="mirum.commerce.checkout.web.fragment.util.AmazonPaymentUtil" %>
<%@ page import="com.liferay.commerce.model.CommerceOrder" %>
<%@ page import="com.liferay.commerce.service.CommerceOrderLocalServiceUtil" %>
<%@ page import="com.liferay.portal.kernel.exception.PortalException" %>
<%@ page import="com.liferay.expando.kernel.model.ExpandoBridge" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.liferay.portal.kernel.util.LocaleUtil" %>

<style>
.multi-step-item.active .multi-step-icon,
body .multi-step-item.active .multi-step-divider,
body .multi-step-item.success-page.complete .multi-step-divider {
	background-color: #17658B;
}

.multi-step-item .multi-step-icon:hover,
body .multi-step-item.active .multi-step-divider:hover,
body .multi-step-item.success-page.complete .multi-step-divider:hover{
	background-color: #2799d1;
	color: #fff;
}

.multi-step-item.complete .multi-step-divider {
	background-color: #2799d1;
	color: #fff;
}

.multi-step-icon {
	background-color: #17658B !important;
	color: #fff;
}

a:hover{
	text-decoration: none;
}






.table-bottom-sec {
    display:flex;
    align-items:center;
    justify-content:center;
    gap:40px;
    border-top:1px solid rgb(120, 145, 171,0.5);
    border-bottom:1px solid rgb(120, 145, 171,0.5);
    width:100%;
    padding:25px 15px;
    margin:36px 0px 60px;
    flex-wrap: wrap;
}
.table-bottom-sec span {
    font-size:14px;
    color:#304F78;
    text-align:center;
}
.button-holder.btn-bottom .btn {
    background:var(--gradientBlueHorizontal);
    padding:12px 24px;
    color:#fff;
    font-size:16px;
    color:#fff;
    border-radius:30px;
}
.order-table-main
{
	background-color: #F5F7FA;
	padding: 100px 200px;
	margin: 0 50px;

}

.rent-transfer {
	background-color: #F5F7FA;
	padding: 80px 40px;
	text-align: center;
	max-width: 400px;
	margin: 0 auto;
}

.rent-transfer-heading {
	font-size: 18px;
	font-weight: 700;
	line-height: 27px;
	color: #0097A5;
}

.rent-transfer-content {
	font-size: 14px;
	font-weight: 400;
	line-height: 25px;
	color: #17658B;
	margin-bottom: 0px;
	text-align: center;
}

@media(max-width: 1366px){
	.table-bottom-sec div{
		width: 200px;
		text-align: center;
	}
}

@media (max-width: 1150px){
	.order-table-main{
		padding: 100px 30px;
	}
}


@media (max-width: 767px){
	.portlet-dropzone{
		padding-left: 0px;
		padding-right: 0px;
	}
}


</style>

<%
CheckoutDisplayContext checkoutDisplayContext = (CheckoutDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

	String[] isOfflinePaymentArray = null;
	String isOfflinePayment = null;
	AmazonPaymentUtil paymentUtil = new AmazonPaymentUtil(currentURL, themeDisplay, checkoutDisplayContext.getCommerceOrderUuid(), request);
	String orderIdString = null;
	String offlinePaymentDetails = null;
	Locale localeId = themeDisplay.getLocale();
	String localeString = localeId.toString();
	try {
		orderIdString = paymentUtil.getOrderId();
		String[] parts = orderIdString.split(":");
		CommerceOrder commerceOrder = null;
		if (parts.length == 2) {
			String numericOrderId = parts[1].trim();
			long commerceOrderId = Long.parseLong(numericOrderId);
			commerceOrder = CommerceOrderLocalServiceUtil.getCommerceOrder(commerceOrderId);
			if (commerceOrder != null) {
				ExpandoBridge expandoBridge = commerceOrder.getExpandoBridge();
				if (expandoBridge.hasAttribute("isOfflinePayment") && expandoBridge.hasAttribute("offlinePaymentDetails")) {
					isOfflinePaymentArray = (String[]) expandoBridge.getAttribute("isOfflinePayment");
					if (isOfflinePaymentArray != null && isOfflinePaymentArray.length > 0) {
						isOfflinePayment = isOfflinePaymentArray[0];
					}
					Map<Locale, String> localeMap = (Map<Locale, String>) expandoBridge.getAttribute("offlinePaymentDetails");
					String englishValue = localeMap.get(LocaleUtil.fromLanguageId("en_US"));
					String arabicValue = localeMap.get(LocaleUtil.fromLanguageId("ar_SA"));
					if(localeString.equals("en_US")){
						System.out.println("inside if:");
						offlinePaymentDetails = englishValue;
					} else if (localeString.equals("ar_SA")) {
						System.out.println("inside if:");
						offlinePaymentDetails = arabicValue;
					}
				}
			}
		}
	} catch (PortalException e) {
		System.out.println("Invalid orderId format: " + orderIdString);
	}
%>

<%--<div style="background-color: #F5F7FA; padding: 100px 200px; margin: 0 50px;">--%>
<div class="order-table-main" >
	<c:choose>
		<c:when test="<%= !checkoutDisplayContext.hasCommerceChannel() %>">
			<div class="alert alert-info mx-auto">
				<liferay-ui:message key="this-site-does-not-have-a-channel" />
			</div>
		</c:when>
		<c:otherwise>
			<div class="row">
				<div class="commerce-checkout container-fluid container-fluid-max-xl">
					<c:choose>
						<c:when test="<%= checkoutDisplayContext.isEmptyCommerceOrder() %>">
							<div class="alert alert-info mx-auto">
								<liferay-ui:message key="the-cart-is-empty" />
								<liferay-ui:message key="please-add-products-to-proceed-with-the-checkout" />
							</div>
						</c:when>
						<c:otherwise>
							<ul class="commerce-multi-step-nav multi-step-indicator-label-top multi-step-nav multi-step-nav-collapse-sm">

								<%
								boolean complete = true;
								String currentCheckoutStepName = checkoutDisplayContext.getCurrentCheckoutStepName();
								int step = 1;

								List<CommerceCheckoutStep> commerceCheckoutSteps = checkoutDisplayContext.getCommerceCheckoutSteps();

								if (commerceCheckoutSteps.size() >= 4) {
									commerceCheckoutSteps = commerceCheckoutSteps.subList(Math.max(commerceCheckoutSteps.size() - 4, 0), commerceCheckoutSteps.size());
								}

								Iterator<CommerceCheckoutStep> commerceCheckoutStepIterator = commerceCheckoutSteps.iterator();

								while (commerceCheckoutStepIterator.hasNext()) {
									CommerceCheckoutStep commerceCheckoutStep = commerceCheckoutStepIterator.next();

									String name = commerceCheckoutStep.getName();

									if (!currentCheckoutStepName.equals(name) && !commerceCheckoutStep.isVisible(request, response)) {
										continue;
									}

									String cssClass = "multi-step-item";

									if (commerceCheckoutStepIterator.hasNext()) {
										cssClass += " multi-step-item-expand";
									}

									if (currentCheckoutStepName.equals(name)) {
										cssClass += " active";
										complete = false;
									}

									if (complete) {
										cssClass += " complete";
									}

									String stepName = commerceCheckoutStep.getLabel(locale);

									if (stepName.equalsIgnoreCase("billing-address")) {
										stepName = "form-submitted";
									}
								%>

									<li class="<%= cssClass %>">
										<div class="multi-step-divider"></div>
										<div class="multi-step-indicator">
											<div class="multi-step-indicator-label">
												<liferay-ui:message key="<%= HtmlUtil.escape(stepName) %>" />
											</div>

											<span class="multi-step-icon" data-multi-step-icon="<%= step %>"></span>
										</div>
									</li>

								<%
									step++;
								}
								%>

							</ul>

							<portlet:actionURL name="/commerce_checkout/save_step" var="saveStepURL" />

							<!-- CUSTOMIZE -->

							<%
							//AmazonPaymentUtil paymentUtil = new AmazonPaymentUtil(currentURL, themeDisplay, checkoutDisplayContext.getCommerceOrderUuid(), request);

							if (currentCheckoutStepName.equalsIgnoreCase("order-confirmation")) {
							%>

								<portlet:actionURL name="/commerce_checkout/save_step" var="saveStepURL" />

								<aui:form action="<%= saveStepURL %>" data-senna-off="<%= checkoutDisplayContext.isSennaDisabled() %>" method="post" name="fm" onSubmit='<%= "event.preventDefault(); " + liferayPortletResponse.getNamespace() + "saveCheckoutStep();" %>'>
									<aui:input name="checkoutStepName" type="hidden" value="<%= currentCheckoutStepName %>" />
									<aui:input name="commerceOrderUuid" type="hidden" value="<%= checkoutDisplayContext.getCommerceOrderUuid() %>" />
									<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />

									<%
									checkoutDisplayContext.renderCurrentCheckoutStep(pageContext);
									%>

									<c:if test="<%= checkoutDisplayContext.showControls() %>">
										<aui:button-row>
											<aui:button cssClass="pull-right" name="continue" primary="<%= true %>" type="submit" value="continue" />
										</aui:button-row>
									</c:if>
								</aui:form>

								<aui:script>
									function <portlet:namespace />saveCheckoutStep() {
										submitForm(document.<portlet:namespace />fm);
									}
								</aui:script>

							<%
							}
							else {
							%>

								<form action="<%= paymentUtil.getPaymentUrl() %>" id="<portlet:namespace />fm" method="post" name="<portlet:namespace />fm">
									<input name="signature" type="hidden" value="<%= paymentUtil.getSignature() %>" />
									<input name="access_code" type="hidden" value="<%= paymentUtil.getAccessCode() %>" />
									<input name="amount" type="hidden" value="<%= String.valueOf(paymentUtil.getAmount().intValue()) %>" />
									<input name="command" type="hidden" value="PURCHASE" />
									<input name="currency" type="hidden" value="SAR" />
									<input name="customer_email" type="hidden" value="<%= paymentUtil.getCustomerEmail() %>" />
									<input name="customer_ip" type="hidden" value="<%= paymentUtil.getCustomerIP() %>" />
									<input name="eci" type="hidden" value="ECOMMERCE" />
									<input name="language" type="hidden" value="en" />
									<input name="merchant_extra" type="hidden" value="<%= paymentUtil.getFormId() %>" />
									<input name="merchant_identifier" type="hidden" value="<%= paymentUtil.getMerchantIdentifier() %>" />
									<input name="merchant_reference" type="hidden" value="<%= paymentUtil.getMerchantReference() %>" />
									<input name="return_url" type="hidden" value="<%= paymentUtil.getReturnURL() %>" />
									<input name="order_description" type="hidden" value="<%= paymentUtil.getOrderId() %>" />


									<%
									checkoutDisplayContext.renderCurrentCheckoutStep(pageContext);
									%>

									<c:if test="<%= checkoutDisplayContext.showControls() && isOfflinePayment!=null %>">
										<c:if test='<%=isOfflinePayment.equals("true")%>'>
											<div class="rent-transfer">
												<p><%=offlinePaymentDetails%></p>
											</div>
										</c:if>
										<c:if test='<%=isOfflinePayment.equals("false")%>'>
											<div class="table-bottom-sec">
												<div>
													<span> <liferay-ui:message key="mada-bank-card"/></span>
												</div>
												<div>
													<img src="<%=request.getContextPath()%>/images/mada-logo.png"
														 style="width: 65px; height: 20px;">
												</div>
												<div>
													<img src="<%=request.getContextPath()%>/images/mastercard.png"
														 style="width: 65px; height: 45px;" class="mastercard-logo">
												</div>
												<div>
													<img src="<%=request.getContextPath()%>/images/visa.png"
														 style="width: 65px; height: 20px;" class="visa-logo">
												</div>
											</div>
											<div>
												<div class="button-holder btn-bottom" style="text-align: center;">
													<button class="btn btn-main btn-primary btn-regular btn-rounded pull-right"
															id="" name="" style="display: inline !important;"
															type="submit">
														<span class="lfr-btn-label"><liferay-ui:message
																key="pay-now"/></span>
													</button>
												</div>
											</div>
										</c:if>
									</c:if>
								</form>

							<%
							}
							%>

						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</c:otherwise>
	</c:choose>
</div>

<script>
	document.addEventListener("DOMContentLoaded", function() {
		var rentTransferDiv = document.querySelector(".rent-transfer p");
		var content = rentTransferDiv.innerText;
		var lines = content.split(",");
		rentTransferDiv.innerText = "";
		lines.forEach(function(line, index) {
			var lineElement = document.createElement("div");
			if(index === 0) {
				lineElement.classList.add("rent-transfer-heading");
			}
			if (index > 0) {
				lineElement.classList.add("rent-transfer-content");
			}
			lineElement.innerText = line.trim();
			rentTransferDiv.appendChild(lineElement);
		});
	});
</script>
