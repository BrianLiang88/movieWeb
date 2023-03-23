package javaWeb.Brian.movieServlet;

import javaWeb.Brian.util.DBUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "MovieServlet", value = "/MovieServlet")
public class MovieServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置响应内容类型
        response.setContentType("text/html;charset=UTF-8");

        // 从数据库获取电影信息
        String sql = "SELECT * FROM movies";
        try {
            Connection conn = DBUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // 将电影信息显示到网页上
            while (rs.next()) {
                response.getWriter().println("<h1>" + rs.getString("title") + "</h1>");
                response.getWriter().println("<p>" + rs.getString("description") + "</p>");
            }

            // 关闭数据库连接
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
