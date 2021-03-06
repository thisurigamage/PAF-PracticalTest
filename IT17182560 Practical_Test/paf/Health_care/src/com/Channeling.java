package com;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Channeling {
	
	private Connection connect()
	{
	 Connection con = null;

	 try
	 {
	 Class.forName("com.mysql.jdbc.Driver");
	 con= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/healthcare","root","");
	 //For testing
	 System.out.print("Successfully connected");
	 }
	 catch(Exception e)
	 {
	 e.printStackTrace();
	 }

	 return con; 
	}

	
	public String readChannel() {
		
		String output = "";
		 
		 try
		 {
		     Connection con = connect();
		     if (con == null)
		    {
		         return "Error while connecting to the database for reading.";
		     }
		  // Prepare the html table to be displayed  
		     
		     output = "<table border='1'><tr><th>Patient Name</th><th>Doctor Name</th><th>Hospital Name</th>"     
		     + "<th>Specialization</th><th>Date</th><th>Update</th><th>Remove</th></tr>"; 
		     
		     String query = "select * from channeling";  
		     Statement stmt = con.createStatement();   
		     ResultSet rs = stmt.executeQuery(query); 
		    
		     // iterate through the rows in the result set   
		     while (rs.next())   
		     {    
		    	 String channelingCode = Integer.toString(rs.getInt("channelingCode"));    
		    	 String patientName = rs.getString("patientName");   
		    	 String doctorName = rs.getString("doctorName");
		    	 String hospitalName = rs.getString("hospitalName");
		    	 String specialization = rs.getString("specialization");
		    	 String date = rs.getString("date"); 
		 
		    	// Add into the html table    
		    	 output += "<tr><td><input id='hidItemIDUpdate' name='hidItemIDUpdate' type='hidden' value='" + channelingCode + "'>" + patientName + "</td>";    
		    	 output += "<td>" + doctorName + "</td>";    
		    	 output += "<td>" + hospitalName + "</td>";    
		    	 output += "<td>" + specialization + "</td>"; 
		    	 output += "<td>" + date + "</td>"; 
		    	 
		    	   // buttons   
		    	 output += "<td><input name='btnUpdate' type='button' value='Update' class='btnUpdate btn btn-secondary'></td>"
		    	 		+ "<td><input name='btnRemove' type='button' value='Remove'class='btnRemove btn btn-danger' data-itemid='" + channelingCode + "'>" + "</td></tr>"; 
		    	 } 
		    	 
		    	  con.close(); 
		    	 
		    	  // Complete the html table   
		    	  output += "</table>"; 
		    	  } 
		        catch (Exception e)  
		
		 {
		    		  output = "Error while reading the items.";  
		    		  System.err.println(e.getMessage());  
		    		  } 
		    	 
		    	 return output;
	}	

	public String insertChannel(String patientName,String doctorName,String hospitalName,String specialization,String date)
	   {
	        String output = "";
	     try
	       {
	           Connection con = connect();
	           if (con==null)
	            {
	               return "Error while connecting to the database.";
	               }
	           
	 // create a prepared statement
	           
	 String query = " insert into channeling(`channelingCode`,`patientName`,`doctorName`,`hospitalName`,`specialization`,`date`) "
	 		+ "values (?,?,?,?,?,?)";
	
	 PreparedStatement preparedStmt = con.prepareStatement(query);
	 // binding values
	 preparedStmt.setInt(1, 0);
	 preparedStmt.setString(2,patientName);
	 preparedStmt.setString(3,doctorName);
	 preparedStmt.setString(4,hospitalName);
	 preparedStmt.setString(5,specialization); 
	 preparedStmt.setString(6,date); 
	
	//execute the statement
	 preparedStmt.execute();
	 con.close();
	 
	 String newChanneling= readChannel();    
	 output = "{\"status\":\"success\", \"data\": \"" + newChanneling + "\"}";  
	 }  
	     catch (Exception e)  
	     {   
		   output = "{\"status\":\"error\", \"data\":\"Error while inserting the item.\"}";    
		   System.err.println(e.getMessage());  
		   } 
	     
	     
	     return output;
    }

	

	public String updateChannel(String channelingCode, String patientName, String doctorName, String hospitalName, String specialization, String date) {
		
		String output ="";
		
		try {
			Connection con= connect();
			
			if(con==null) {
				return "Error while connecting to the database";
			}
			
			//create a prepared statement
			String query = "UPDATE channeling SET patientName=?,doctorName=?,hospitalName=?,specialization=?,date=? WHERE channelingCode=?";
			
			PreparedStatement preparedStmt = con.prepareStatement(query);
			
			//binding values
			preparedStmt.setString(1, patientName);
			preparedStmt.setString(2, doctorName);
			preparedStmt.setString(3, hospitalName);
			preparedStmt.setString(4, specialization);
			preparedStmt.setString(5, date);
			preparedStmt.setInt(6,Integer.parseInt(channelingCode));
			
			//execute the statement
			preparedStmt.execute();
			con.close();
			
			String newChanneling = readChannel();
			 output= "{\"status\":\"success\", \"data\":\""+ newChanneling +"\"}";
			
		}
		catch(Exception e) {
			output = "{\"status\":\"error\", \"data\":\"Error while updating.\"}";
			System.err.println(e.getMessage());
		}
		return output;
	}
	
	
	
	public String deleteChannel(String channelingCode)
	{
	 String output = "";
	try
	 {
	   Connection con = connect();
	 if (con == null)
	 {
	 return "Error while connecting to the database for deleting.";
	 }
	 // create a prepared statement
	 String query = "delete from channeling where channelingCode=?";
	 PreparedStatement preparedStmt = con.prepareStatement(query);
	 
	 // binding values
	 preparedStmt.setInt(1,Integer.parseInt(channelingCode));

	 // execute the statement
	 preparedStmt.execute();
	 con.close();
	 
	 String newChanneling =readChannel();
	 output= "{\"status\":\"success\", \"data\":\""+ newChanneling +"\"}";
	 
	 }
	catch (Exception e)
	 {
		output = "{\"status\":\"error\", \"data\":\"Error while deleting.\"}";
	 System.err.println(e.getMessage());
	 }
	return output;
}
	
}
	
