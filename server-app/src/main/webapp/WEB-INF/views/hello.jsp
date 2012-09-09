<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>

<tag:pageWrapper sectionName="Main">
    <p>Hello there! Greeting data is as follows:</p>
    <table>
        <tr>
            <td>Origin</td>
            <td>${hello.origin}</td>
        </tr>
        <tr>
            <td>Greeting</td>
            <td>${hello.greeting}</td>
        </tr>
        <tr>
            <td>Created</td>
            <td>${hello.created}</td>
        </tr>
    </table>
</tag:pageWrapper>