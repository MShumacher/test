<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="row">
	<form:form class="col s6" method="POST" action="${pageContext.request.contextPath}"	modelAttribute="formModel">
		<c:if test="${not empty message}">
				<div class="col s12 center">
					<div class="message">${message}</div>
				</div>
		</c:if>
			<div class="input-field col s12">
				<form:input path="firstName" type="text" />
				<form:errors path="firstName" cssClass="red-text" />
				<label for="firstName">First name</label>
			</div>
			<div class="input-field col s12">
				<form:input path="lastName" type="text" />
				<form:errors path="lastName" cssClass="red-text" />
				<label for="lastName">Last name</label>
			</div>
			<div class="col s6">
				<button class="btn waves-effect waves-light right" type="submit"
					value="Submit">
					Send
					<i class="material-icons right">send</i>
				</button>
			</div>
			
	</form:form>
</div>