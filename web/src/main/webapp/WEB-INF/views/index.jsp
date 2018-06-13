<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Simple mail</title>
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
<script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0-beta/css/materialize.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0-beta/js/materialize.min.js"></script>

</head>

<body>
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
</body>
</html>