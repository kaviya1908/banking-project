package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BalanceServlet
 */
@WebServlet("/balanceServlet")
public class BalanceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accountNo = request.getParameter("accountNo");

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/vasundhara", "root", "varsha@2002");

            String sql = "SELECT initial_balance FROM customer WHERE account_no = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, accountNo);
            ResultSet rs = statement.executeQuery();

            double balance = 0.0;
            if (rs.next()) {
                balance = rs.getDouble("initial_balance");
            }

            // Set balance as a request attribute
            request.setAttribute("balance", balance);

            // Forward the request to balance.jsp
            RequestDispatcher dispatcher = request.getRequestDispatcher("/balance.jsp");
            dispatcher.forward(request, response);

            con.close(); // Close connection (best practice)

        } catch (Exception e) {
            e.printStackTrace();
            // Handle the error gracefully, e.g., redirect to an error page
            response.sendRedirect("error.jsp");
        }
    }
}
