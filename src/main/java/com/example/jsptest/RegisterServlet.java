package com.example.jsptest;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.sql.*;

import java.io.IOException;
import java.util.Properties;

@WebServlet("/RegisterServlet")

public class RegisterServlet extends HttpServlet {
    /*serialVersionUID는 Serializable 인터페이스를 구현하는 클래스에서 직렬화(Serialization)와 역직렬화(Deserialization)에 사용되는 고유한 ID입니다. HttpServlet 클래스는 Serializable 인터페이스를 상속받고 있으므로, serialVersionUID가 필요합니다.
    역할:
    객체를 직렬화할 때, 이 ID는 클래스의 버전 정보를 식별하는 데 사용됩니다.
    만약 클래스가 변경된 후 직렬화된 객체를 다시 불러오면, serialVersionUID가 다르면 InvalidClassException이 발생할 수 있습니다.
            따라서, 클래스 버전을 명확히 지정하고, 변경 사항에 의해 발생할 수 있는 호환성 문제를 방지하기 위해 명시적으로 선언하는 것이 좋습니다.*/
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 응답 인코딩 설정
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        //폼에서 받은 데이터
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String nickname = request.getParameter("nickname");
        String gender = request.getParameter("gender");

        //프로퍼티 파일에서 DB 설정을 로드
        Properties props = new Properties();
        try (InputStream input = getServletContext().getResourceAsStream("/WEB-INF/config.properties")) {
            props.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //프로퍼티 파일에서 설정 읽기
        String jdbcUrl = props.getProperty("jdbc.url");
        String dbUser = props.getProperty("jdbc.user");
        String dbPassword = props.getProperty("jdbc.password");

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String message =""; //성공&실패 메세지 저장용 변수

        try {
            //JDBC 드라이버 로드
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //데이터베이스 연결
            conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);

            //중복 검사 쿼리
//          //1. 아이디 중복 검사
            String checkUsernameSql = "select count(*) from users where username = ?";
            pstmt = conn.prepareStatement(checkUsernameSql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                //중복된 아이디나 이메일, 닉네임이 있는 경우
                message = "이미 존재하는 아이디입니다.";
            }

            //2. 이메일 중복 검사 (아이디 중복이 없는 경우만 검사)
            if (message.isEmpty()) {
                String checkEmailSql = "select count(*) from users where email = ?";
                pstmt = conn.prepareStatement(checkEmailSql);
                pstmt.setString(1, email);
                rs = pstmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    message = "이미 존재하는 이메일입니다.";
                }
            }

            //3. 닉네임 중복 검사 (아이디와 이메일 중복이 없는 경우만 검사)
            if (message.isEmpty()) {
                String checkNicknameSql = "select count(*) from users where nickname = ?";
                pstmt = conn.prepareStatement(checkNicknameSql);
                pstmt.setString(1, nickname);
                rs = pstmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    message = "이미 존재하는 닉네임입니다.";
                }
            }

            //4. 중복된 정보가 없을 때 회원가입 처리
            if (message.isEmpty()) {
                //SQL 삽입문 준비
                String sql = "insert into users (username, password, email, name, nickname, gender) values (?, ?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.setString(3, email);
                pstmt.setString(4, name);
                pstmt.setString(5, nickname);
                pstmt.setString(6, gender);

                //SQL 실행
                int result = pstmt.executeUpdate();
                if (result > 0) {
                    message = ("회원가입 성공!");
                } else {
                    message = ("회원가입 실패!");
                }
            }

            //after_register.jsp로 메시지 전달
            request.setAttribute("message", message);
            RequestDispatcher dispatcher = request.getRequestDispatcher("after_register.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "오류가 발생했습니다: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("after_register.jsp");
            dispatcher.forward(request, response);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}