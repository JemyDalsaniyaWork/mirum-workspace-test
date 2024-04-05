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

<%@ include file="/display/init.jsp" %>
<%
String redirect = ParamUtil.getString(request, "redirect");

DDMFormInstance formInstance = ddmFormDisplayContext.getFormInstance();

DDMFormInstanceRecord formInstanceRecord = ddmFormDisplayContext.getFormInstanceRecord();

DDMFormInstanceRecordVersion formInstanceRecordVersion = null;

if (formInstanceRecord != null) {
	formInstanceRecordVersion = formInstanceRecord.getLatestFormInstanceRecordVersion();
}

portletDisplay.setURLBack(redirect);
portletDisplay.setShowBackIcon(true);

String title = ParamUtil.getString(request, "title");

renderResponse.setTitle(GetterUtil.get(title, LanguageUtil.get(request, "view-form")));
%>

<clay:container-fluid>
	<c:if test="<%= formInstanceRecordVersion != null %>">
		<aui:model-context bean="<%= formInstanceRecordVersion %>" model="<%= DDMFormInstanceRecordVersion.class %>" />

		<div class="panel text-center">
			<c:choose>
                <c:when test ="<%= formInstanceRecordVersion.getStatus() == 100 %>">
                    <span class="taglib-workflow-status">
                        <span class="mr-2 workflow-version">
                            <span class="workflow-label">Version:</span>
                            <span class="workflow-value"> ${ formInstanceRecordVersion.getVersion() }</span>
                        </span>
                        <span class="workflow-status">
                            <span class="label status workflow-value" style="background-color: #e7e7e6; border-color: #9499cd; color: #bb892d;">
                                PAYMENT PAID
                            </span>
                        </span>
                    </span>
                </c:when>
                <c:when test = "<%= formInstanceRecordVersion.getStatus() == 101 %>">
                    <span class="taglib-workflow-status">
                        <span class="mr-2 workflow-version">
                            <span class="workflow-label">Version:</span>
                            <span class="workflow-value"> ${ formInstanceRecordVersion.getVersion() }</span>
                        </span>
                        <span class="workflow-status">
                            <span class="label status workflow-value" style="background-color: #fdfdfd; border-color: #ffffff; color: #781212;">
                                PAYMENT FAILED
                            </span>
                        </span>
                    </span>
                </c:when>
                <c:otherwise>
                    <aui:workflow-status markupView="lexicon" model="<%= DDMFormInstanceRecord.class %>" showHelpMessage="<%= false %>" showIcon="<%= false %>" showLabel="<%= false %>" status="<%= formInstanceRecordVersion.getStatus() %>" version="<%= formInstanceRecordVersion.getVersion() %>" />
                </c:otherwise>
            </c:choose>
		</div>
	</c:if>
</clay:container-fluid>

<clay:container-fluid
	cssClass="ddm-form-builder-app editing-form-entry"
>
	<portlet:actionURL name="/dynamic_data_mapping_form/add_form_instance_record" var="editFormInstanceRecordActionURL" />

	<aui:form action="<%= editFormInstanceRecordActionURL %>" data-DDMFormInstanceId="<%= ddmFormDisplayContext.getFormInstanceId() %>" data-senna-off="true" method="post" name="fm">
		<aui:input name="redirect" type="hidden" value="<%= redirect %>" />
		<aui:input name="formInstanceRecordId" type="hidden" value="<%= ddmFormDisplayContext.getFormInstanceRecordId() %>" />
		<aui:input name="formInstanceId" type="hidden" value="<%= ddmFormDisplayContext.getFormInstanceId() %>" />
		<aui:input name="defaultLanguageId" type="hidden" value='<%= ParamUtil.getString(request, "defaultLanguageId") %>' />

		<liferay-portlet:resourceURL copyCurrentRenderParameters="<%= false %>" id="/dynamic_data_mapping_form/validate_csrf_token" var="validateCSRFTokenURL" />

		<div id=<%= ddmFormDisplayContext.getContainerId() %>>

			<%
			String languageId = ddmFormDisplayContext.getDefaultLanguageId();

			Locale displayLocale = LocaleUtil.fromLanguageId(languageId);
			%>

			<react:component
				module="admin/js/FormView"
				props='<%=
					HashMapBuilder.<String, Object>put(
						"description", formInstance.getDescription(displayLocale)
					).put(
						"title", formInstance.getName(displayLocale)
					).put(
						"validateCSRFTokenURL", validateCSRFTokenURL.toString()
					).putAll(
						ddmFormDisplayContext.getDDMFormContext()
					).build()
				%>'
			/>
		</div>
	</aui:form>
</clay:container-fluid>

