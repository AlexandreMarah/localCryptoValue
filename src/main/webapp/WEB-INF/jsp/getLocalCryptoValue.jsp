<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Getting Started: Serving Web Content</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
<c:if test="${cryptoLocalValue != null}">
        <div> Crypto name :  ${cryptoLocalValue.cryptoName}</div>
        <div> Crypto value :  ${cryptoLocalValue.cryptoValue}</div>
</c:if>
<a href="getCryptoList"> Get another local value</a>
</body>