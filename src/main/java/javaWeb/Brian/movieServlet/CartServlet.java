package javaWeb.Brian.movieServlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javaWeb.Brian.model.Movie;

import javaWeb.Brian.util.DBUtil;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取用户ID
        int userId = Integer.parseInt(request.getParameter("userId"));

        // 查询用户的购物车信息
        List<Movie> cart = getCart(userId);

        // 设置响应内容类型
        response.setContentType("text/html;charset=UTF-8");

        // 在网页上显示购物车信息
        String html = "<html><body><h2>购物车信息：</h2><table><tr><th>电影名称</th><th>价格</th></tr>";
        for (Movie movie : cart) {
            html += "<tr><td>" + movie.getName() + "</td><td>" + movie.getPrice() + "</td></tr>";
        }
        html += "</table></body></html>";

        // 将购物车信息写入响应中
        response.getWriter().println(html);
    }

    /**
     * 从数据库中获取用户的购物车信息
     */
    private List<Movie> getCart(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Movie> cart = new ArrayList<>();

        try {
            // 获取数据库连接
            conn = DBUtil.getConnection();

            // 构造 SQL 语句
            String sql = "SELECT m.id, m.name, m.price FROM cart c, movie m WHERE c.user_id = ? AND c.movie_id = m.id";

            // 创建 PreparedStatement 对象
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);

            // 执行查询并处理结果集
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Movie movie = new Movie();
                movie.setId(rs.getInt("id"));
                movie.setName(rs.getString("name"));
                //movie.setPrice(rs.getDouble("price"));
                cart.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭数据库连接
            DBUtil.close(conn, pstmt);
        }

        return cart;
    }

}
