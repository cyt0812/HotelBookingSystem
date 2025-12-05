// CheckHotelData.java
import java.sql.*;

public class CheckHotelData {
    public static void main(String[] args) {
        System.out.println("=== 检查数据库中的酒店数据 ===");
        
        try {
            // 加载驱动
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            
            // 连接数据库
            String url = "jdbc:derby:memory:testdb;create=true";
            Connection conn = DriverManager.getConnection(url, "app", "app");
            
            // 1. 查看 hotels 表结构
            System.out.println("\n1. hotels 表结构:");
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet columns = meta.getColumns(null, null, "HOTELS", null);
            while (columns.next()) {
                String colName = columns.getString("COLUMN_NAME");
                String colType = columns.getString("TYPE_NAME");
                System.out.println("  " + colName + " : " + colType);
            }
            
            // 2. 查看 hotels 表数据
            System.out.println("\n2. hotels 表数据:");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM hotels");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                System.out.println("  ID: " + id + ", Name: " + name);
            }
            
            // 3. 查看 rooms 表结构
            System.out.println("\n3. rooms 表结构:");
            columns = meta.getColumns(null, null, "ROOMS", null);
            while (columns.next()) {
                String colName = columns.getString("COLUMN_NAME");
                String colType = columns.getString("TYPE_NAME");
                System.out.println("  " + colName + " : " + colType);
            }
            
            // 4. 查看 rooms 表数据
            System.out.println("\n4. rooms 表数据:");
            rs = stmt.executeQuery("SELECT * FROM rooms");
            while (rs.next()) {
                int id = rs.getInt("id");
                int hotelId = rs.getInt("hotel_id");
                String roomNumber = rs.getString("room_number");
                System.out.println("  Room ID: " + id + ", Hotel ID: " + hotelId + ", Room: " + roomNumber);
            }
            
            conn.close();
            
        } catch (Exception e) {
            System.err.println("错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}