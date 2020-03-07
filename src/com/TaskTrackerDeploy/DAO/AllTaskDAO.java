package com.TaskTrackerDeploy.DAO;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.TaskTrackerDeploy.model.AllTaskModel;

public class AllTaskDAO extends DAO {

	public static Connection db = null;

	public ArrayList<AllTaskModel> Tasklist(String user) {

		ArrayList<AllTaskModel> Tlist = new ArrayList<AllTaskModel>();
		try {

			Connection db = DAO.Intialize_DB();
			String Query = "SELECT TO_CHAR(date,'Mon dd yyyy') as date, TASK, STATUS, TASK_ID FROM task_main WHERE uname='"+user+"' ORDER BY date DESC;";
			Statement st = db.createStatement();
			ResultSet rs = st.executeQuery(Query);

			while (rs.next()) {
				AllTaskModel task = new AllTaskModel();
				task.setDate(rs.getString("DATE"));
				task.setTask(rs.getString("TASK"));
				task.setStatus(rs.getString("STATUS"));
				task.setTask_ID(rs.getInt("TASK_ID"));

				Tlist.add(task);
			}
		} catch (Exception e) {
			System.out.println("Exception in DB");
			e.printStackTrace();
		}
		return Tlist;

	}

	// Update status completed tasks and delete it

	public void UpdateStatus(List Statusupdate) {
		for (int i = 0; i < Statusupdate.size(); i++) {
			try {

				Connection db = DAO.Intialize_DB();
				String Query = "DELETE FROM task_main WHERE task_id='" + Statusupdate.get(i) + "';";
				Statement st = db.createStatement();
				st.executeUpdate(Query);
			} catch (Exception e) {
				System.out.println("Exception While deleting");
			}
		}

	}
	
	// Reset All
	public void ResetAll() {
		
			try {

				Connection db = DAO.Intialize_DB();
				Statement statement = db.createStatement();
				String updateQuery = "DELETE FROM task_main";
				statement.executeUpdate(updateQuery);
				statement.executeUpdate("ALTER SEQUENCE task_main_task_id_seq RESTART WITH 1");
			
			} catch (Exception e) {
				System.out.println("Exception While deleting");
			}
	
	}
	
	//Add Task
	
	public void AddTask(String Task, Date date, String user) {
		//SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");  
		//String newDate = formatter.format(date);
		try {

			Connection db = DAO.Intialize_DB();
			Statement statement = db.createStatement();
			String updateQuery = "INSERT INTO task_main (date,task,status,uname) values ('"+date+"','"+ Task +"','due','"+user+"');";
			statement.executeUpdate(updateQuery);
		
		} catch (Exception e) {
			System.out.println("Exception While Adding");
		}
		
	}
	
	// Edit existing task
	public void EditTask(String Task, Date date, String task_id) {
		//SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");  
		//String newdate = formatter.format(date);
		try {

			Connection db = DAO.Intialize_DB();
			Statement statement = db.createStatement();
			String updateQuery = "UPDATE task_main SET date='" + date + "',task='" + Task + "' WHERE task_id='"+ task_id +"';";
			statement.executeUpdate(updateQuery);
		
		} catch (Exception e) {
			System.out.println("Exception While Adding");
		}
	}
	
	// Authenticate user
	
public Boolean AuthenticateUser(String uname, String pass) {
	
	Connection db = DAO.Intialize_DB();
	try {
	Statement statement = db.createStatement();
	String execuString = "Select  uname from task_main_login_data where uname='"+uname+"' and md5pwd=md5('"+pass+"');";
	ResultSet authenticate = statement.executeQuery(execuString);
	if(authenticate.next()) {
		return true;
	}
	}
	catch (Exception e) {
		System.out.println("Authentication error");
	}
	
	return false;
}
}
