<%@ page import="static calendar.custom.module.portlet.CalendarCustomModulePortlet.getProductNames" %>
<%@ page import="com.liferay.portal.kernel.exception.PortalException" %>
<%@ page import="java.util.List" %>
<%@ page import="static calendar.custom.module.portlet.CalendarCustomModulePortlet.getObjectEntryValues" %>
<%@ page import="static com.liferay.batch.engine.BatchEngineTaskContentType.JSON" %>
<%@ include file="/init.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.google.gson.Gson" %>

<portlet:actionURL var="createOrderActionURL" name="createOrder">
</portlet:actionURL>
<%
    List<Object> resultSet = getProductNames();
    request.setAttribute("resultSet", resultSet);
    String entryListJson = null;
    try {
        List<Map<String, String>> entryList = getObjectEntryValues();
        entryListJson = new Gson().toJson(entryList);
        request.setAttribute("entryListJson", entryListJson);
    } catch (PortalException e) {
        System.out.println("error getting object entries");
    }
%>
<div class="container col-md-4">
	<h2>Order Creation form</h2>
	<aui:container>
        <aui:form action="<%=createOrderActionURL%>" method="POST">
            <aui:select label="Product" name="firstName" id="product" multiple="true">
                <c:forEach var="result" items="${resultSet}">
                    <aui:option value="${result}">${result}</aui:option>
				</c:forEach>
			</aui:select>
            <aui:input name="date" type="date">

			</aui:input>
			<div id="calendar"></div>
			<aui:button-row>
                <aui:button type="submit" />
			</aui:button-row>
		</aui:form>
	</aui:container>
</div>


<script src="<%=request.getContextPath()%>/lib/index.global.js"></script>
<script>

	document.addEventListener('DOMContentLoaded', function() {
		let calendarEl = document.getElementById('calendar');
		let entryList = <%=entryListJson%>;

		const events = [];

		for (let i = 0; i < entryList.length; i++) {
			const entryMap = entryList[i];
			const title = entryMap['testfield']; // Assuming 'testfield' is an array in your data
			const start = entryMap['eventDate'];
			console.log("title" + title + " date" + start)

			events.push({
				title: title,
				start: start
			});
		}


		const calendar = new FullCalendar.Calendar(calendarEl, {
			initialDate: '2024-01-12',
			editable: true,
			selectable: true,
			businessHours: true,
			dayMaxEvents: true, // allow "more" link when too many events
			events: events
		});

		calendar.render();
	});

</script>
<style>

	body {
		margin: 40px 10px;
		padding: 0;
		font-family: Arial, Helvetica Neue, Helvetica, sans-serif;
		font-size: 14px;
	}

	#calendar {
		max-width: 1100px;
		margin: 0 auto;
	}

</style>

