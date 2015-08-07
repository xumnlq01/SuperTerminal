package terminal;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.*;

public class DBConnection {
	private Connection conn;
	private Statement stmt;
	private String url,commId,macAddr;
	
	
	public DBConnection(String url,String commId){
		this.url="jdbc:mysql://"+url+"/super_terminal";
		getMacAddr();
		this.commId=commId;
	}
	private void getMacAddr(){
		macAddr="";
		try {
			byte[] mac=NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
			for(int i=0;i<mac.length;i++){
				int tmp=mac[i]&0xff;
				String str=Integer.toHexString(tmp);
				if(str.length()==1)
					macAddr+=("0"+str);
				else {
					macAddr+=str;
				}
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	private Connection getConnection(){
		Connection con=null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection(this.url, "root", "root");
		}catch(Exception e){
			e.printStackTrace();
		}
		return con;
	}
	public  void insert(String command){
		if(command.length()==0)
			return;
		try{
			conn=getConnection();
			String sql="insert terminal_log (host,commId,time,command)values(\""
			+this.macAddr+"\",\""+this.commId+"\",now(),\""+command+"\")";
			
			stmt=conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
			conn.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		
	}
	/*public static void main(String args[]){
		DBConnection con=new DBConnection("localhost:3306", "comm1");
		con.insert("dis cur");
		con.insert("vlan 1");
	}*/

}
