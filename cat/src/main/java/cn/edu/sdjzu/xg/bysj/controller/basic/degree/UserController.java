package cn.edu.sdjzu.xg.bysj.controller.basic.degree;

import cn.edu.sdjzu.xg.bysj.domain.User;
import cn.edu.sdjzu.xg.bysj.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/user.ctl")
public class UserController extends HttpServlet {
    /**
     *  PUT, http://localhost:8080/department.ctl, 修改系
     * 修改一个系对象：将来自前端请求的JSON对象，更新数据库表中相同id的记录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        String password = request.getParameter("password");
        JSONObject message = new JSONObject();
        try {
            UserService.getInstance().changePassword(id,password);
            message.put("message","修改成功");
        } catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        }catch(Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        }//响应message到前端
        response.getWriter().println(message);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //读取参数id
        String id_str = request.getParameter("id");
        String username_str = request.getParameter("username");

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
            if (id_str!=null){
                int id = Integer.parseInt(id_str);
                responseUser(id, response);
            }else{
                responseUserByUsername(response, username_str);
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        }
    }

    //响应一个学位对象
    private void responseUser(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找学院
        User user = UserService.getInstance().find(id);
        String user_json = JSON.toJSONString(user);

        //响应message到前端
        response.getWriter().println(user_json);
    }

    //要求服务器响应paraType类的并对应相应Id的所有系
    private void responseUserByUsername(HttpServletResponse response,String username) throws SQLException, IOException {
        //根据用户名查找账号
        User user = UserService.getInstance().findByUsername(username);
        String user_json = JSON.toJSONString(user);
        //响应
        response.getWriter().println(user_json);
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //添加try-catch块解决异常
        try {
            UserService.getInstance().delete(id);
            message.put("message", "删除成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        }catch(Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
        }

        //响应message到前端
        response.getWriter().println(message);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        try {
            User userToLogin=UserService.getInstance().login(username,password);
            if (userToLogin!=null){
                HttpSession session = request.getSession();
                session.setAttribute("currentUser",userToLogin);
                message.put("message", "登录成功");
            }else {
                message.put("message", "密码账户不正确");
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        }
        //响应message到前端
        response.getWriter().println(message);
    }

}
