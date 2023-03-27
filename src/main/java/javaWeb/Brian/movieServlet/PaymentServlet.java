package javaWeb.Brian.movieServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javaWeb.Brian.model.Movie;
import javaWeb.Brian.util.DBUtil;
import javaWeb.Brian.util.PaymentUtil;

public class PaymentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // 获取用户购买电影的信息
        String movieId = request.getParameter("movieId");
        String userId = request.getParameter("userId");
        String movieName = "";
        int price = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM movie WHERE movie_id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, movieId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                movieName = rs.getString("movie_name");
                price = rs.getInt("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //DBUtil.close(conn, pstmt, rs);
            DBUtil.close(conn, pstmt);
        }

        // 调用支付接口完成支付
        String orderId = UUID.randomUUID().toString().replace("-", "");
        String body = "购买电影：" + movieName;
        String subject = "电影购买";
        String total_fee = String.valueOf(price);
        String result = PaymentUtil.pay(orderId, body, subject, total_fee);
        if ("success".equals(result)) {
            // 支付成功，将订单信息插入数据库
            try {
                conn = DBUtil.getConnection();
                String sql = "INSERT INTO orders(order_id, user_id, movie_id, movie_name, price) VALUES(?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, orderId);
                pstmt.setString(2, userId);
                pstmt.setString(3, movieId);
                pstmt.setString(4, movieName);
                pstmt.setInt(5, price);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                //DBUtil.close(conn, pstmt, rs);
                DBUtil.close(conn, pstmt);
            }
            out.print("<script>alert('支付成功！');location.href='movie.jsp';</script>");
        } else {
            // 支付失败，返回支付页面重新支付
            out.print("<script>alert('支付失败，请重新支付！');history.back();</script>");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
