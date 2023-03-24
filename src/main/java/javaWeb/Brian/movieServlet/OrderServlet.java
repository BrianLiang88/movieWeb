package javaWeb.Brian.movieServlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javaWeb.Brian.util.DBUtil;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String movieName = request.getParameter("movieName");
        String orderTime = getCurrentTime();

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO orders(username, movieName, orderTime) VALUES(?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, movieName);
            pstmt.setString(3, orderTime);
            pstmt.executeUpdate();
            response.sendRedirect(request.getContextPath() + "/movie");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    private String getCurrentTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return df.format(now);
    }

}
