<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="bbs.BbsDAO" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="bbs" class="bbs.Bbs" scope="page" />
<jsp:setProperty name="bbs" property="bbsTitle" />
<jsp:setProperty name="bbs" property="bbsContent" />
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSP 게시판 웹 사이트</title>
</head>
<body>
<%--게시글 작성은 로그인이 되어 있어야만 작성 가능 --%>
	<%
		String userID = null;
		if(session.getAttribute("userID") != null){
			userID =  (String) session.getAttribute("userID");
		}
		if(userID == null) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("로그인 하세요.");
			script.println("location.href='login.jsp'");
			script.println("</script>");
		}else{
			if(bbs.getBbsTitle() == null || bbs.getBbsContent() == null){
						PrintWriter script = response.getWriter();
						script.println("<script>");
						script.println("alert('입력이 안 된 사항이 있습니다.')");
						script.println("history.back()");
						script.println("</script>");
					}else{
						BbsDAO bbsDAO = new BbsDAO();
						int result = bbsDAO.writer(bbs.getBbsTitle(), userID, bbs.getBbsContent());
						if(result == -1){
							PrintWriter script = response.getWriter();
							script.println("<script>");
							script.println("alert('게시글 작성에 실패했습니다.')");
							script.println("history.back()");
							script.println("</script>");
						}
						else{
							PrintWriter script = response.getWriter();
							script.println("<script>");
							script.println("location.href='bbs.jsp'");
							script.println("</script>");
						}
					}
		}
	%>
</body>
</html>