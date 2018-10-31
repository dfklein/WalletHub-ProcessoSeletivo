package com.ef.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import com.ef.model.Block;
import com.ef.model.Request;

/**
 * I decided to use pure JDBC since the project is too small and has only two simple insert statements
 */
public class ParserDao {
	
	private static final String URL = "jdbc:mysql://localhost/parser";
	private static final String USER = "root";
	private static final String PASSWORD = "";
	
	private static final String SQL_INSERT_LOGS = "INSERT INTO PS_REQUEST (DT_DATE, DS_IP, TP_REQUEST, CD_STATUS, DS_USER_AGENT) VALUES (?, ?, ?, ?, ?)";
	private static final String SQL_BLOCKED_IPS = "INSERT INTO PS_BLOCK (DS_COMMENT, DS_IP) VALUES (?, ?)";
    
	public ParserDao() throws ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw e;
		}
		
	}
	
	public List<Request> persistLogs(List<Request> logs) throws Exception {
		try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				Statement stmt = conn.createStatement();
				PreparedStatement ps = conn.prepareStatement(SQL_INSERT_LOGS);) {
			conn.setAutoCommit(false);
			
			for(Request log : logs) {
				ps.setTimestamp(1, new java.sql.Timestamp(log.getDate().getTime()));
				ps.setString(2, log.getIp());
				ps.setString(3, log.getRequest());
				ps.setInt(4, log.getStatus());
				ps.setString(5, log.getUserAgent());
				ps.execute();
			}
			
			conn.commit();
			return logs;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public List<Block> persistBlockedIps(List<Block> blockedIps) throws Exception {
		Block block = new Block();
		try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				Statement stmt = conn.createStatement();
				PreparedStatement ps = conn.prepareStatement(SQL_BLOCKED_IPS);) {
			conn.setAutoCommit(false);
			
			for(Block blockedIp : blockedIps) {
				block = blockedIp;
				ps.setString(1, blockedIp.getComment());
				ps.setString(2, blockedIp.getIp());
				ps.execute();
			}
			
			conn.commit();
			return blockedIps;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
}
