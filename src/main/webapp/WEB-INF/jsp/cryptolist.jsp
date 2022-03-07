<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Getting Started: Serving Web Content</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
<div>
<select name="cryptoItemSelection">
  <c:forEach items="${cryptoItemsMap}" var="cryptoItem">
      <option value="${cryptoItem.key}">${cryptoItem.value}</option>
  </c:forEach>
</select>
</div>
</body>
</html>