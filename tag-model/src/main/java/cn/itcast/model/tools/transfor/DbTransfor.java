package cn.itcast.model.tools.transfor;

import java.sql.*;
import java.util.*;


public class DbTransfor {

    private static String driver = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:mysql:///tags_dat?useUnicode=true&characterEncoding=utf8&user=root&password=123456";

    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Random random = new Random();
        int nextInt = random.nextInt(952);
        String yhq = "YHQ20190720V900N";
        for (int i = 1; i <= 68; i++) {
            String no = "" + i;
            if (no.length() == 1) {
                no = "000" + no;
            }
            if (no.length() == 2) {
                no = "00" + no;
            }
            if (no.length() == 3) {
                no = "0" + no;
            }
            System.out.println("UPDATE tbl_orders SET couponCode=" + yhq + no + ",couponCodeValue=" + 900 + ",orderAmount=(orderAmount-900) WHERE id= ;");
        }
//		List<Integer> ids = getIds();
//		List<Map<String, Integer>> beans = getOrders();
//		updateOrder(ids, beans);
    }

    public static List<Integer> getIds() {
        List<Integer> ids = new ArrayList<Integer>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = DriverManager.getConnection(url);
            ps = connection.prepareStatement("SELECT id FROM tbl_users");
            rs = ps.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ids;
    }

    public static List<Map<String, Integer>> getOrders() {
        List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = DriverManager.getConnection(url);
            ps = connection.prepareStatement("SELECT id,memberId FROM tbl_orders WHERE memberId=0");
            rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Integer> bean = new HashMap<String, Integer>();
                bean.put("id", rs.getInt(1));
                bean.put("memberId", rs.getInt(2));
                list.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static void updateOrder(List<Integer> ids, List<Map<String, Integer>> beans) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Random random = new Random();
        try {
            connection = DriverManager.getConnection(url);
            ps = connection.prepareStatement("UPDATE tbl_orders SET memberId=? WHERE id=?");
            for (Map<String, Integer> map : beans) {
                ps.setInt(1, ids.get(random.nextInt(952)));
                ps.setInt(2, map.get("id"));
                ps.addBatch();
            }
            int[] executeBatch = ps.executeBatch();
            System.out.println(executeBatch.length);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
