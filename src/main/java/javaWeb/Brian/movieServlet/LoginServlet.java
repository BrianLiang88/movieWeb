package javaWeb.Brian.movieServlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 获取用户提交的用户名和密码
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // TODO: 身份验证和会话管理

        // 设置响应内容类型和字符编码
        response.setContentType("text/html;charset=UTF-8");

        // 输出响应内容
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Login Result</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Login Result</h1>");
        out.println("<p>Username: " + username + "</p>");
        out.println("<p>Password: " + password + "</p>");
        out.println("</body>");
        out.println("</html>");
    }

}
