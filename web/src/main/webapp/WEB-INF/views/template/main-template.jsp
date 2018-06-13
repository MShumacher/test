<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><tiles:insertAttribute name="title" /></title>
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
<script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0-beta/css/materialize.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0-beta/js/materialize.min.js"></script>




</head>

<body>
    <tiles:insertAttribute name="header" />
    <main>
    <div class="row">
       <div class="col s12">
            <div class="container">
                 <tiles:insertAttribute name="body" />
            </div> 
       </div>
    </div>
    </main>
    <tiles:insertAttribute name="footer" />
</body>
</html>