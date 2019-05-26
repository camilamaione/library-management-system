/**
 * This class implements an issue data access object using the JDBC API. 
 */
package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.dao.IssueDAO;
import model.database.Database;
import model.database.DatabaseException;
import model.database.DatabaseIntegrityException;
import model.entities.Issue;

public class IssueDAOJDBC implements IssueDAO {
	
	private Connection conn = null;
	
	public IssueDAOJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insertIssue(Issue issue) {
		PreparedStatement st = null;		
		try {
			st = conn.prepareStatement("INSERT INTO issuedBooks (IssuedDate, StudentId, BookId) "
					+ "VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			st.setDate(1, new java.sql.Date(issue.getIssueDate().getTime()));
			st.setInt(2, issue.getStudentId());
			st.setInt(3, issue.getBookId());
			int rowsAffected = st.executeUpdate();
			if (rowsAffected < 1)
				throw new DatabaseException("Unexpected error. No rows affected by insertion.");
			else {
				ResultSet rs = st.getGeneratedKeys();
				rs.next();
				issue.setId(rs.getInt(1));
				Database.closeResultSet(rs);
			}				
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			Database.closeStatement(st);
		}		
	}

	@Override
	public void updateIssue(Issue issue) {
		PreparedStatement st = null;		
		try {
			st = conn.prepareStatement("UPDATE issuedBooks SET IssuedDate = ?, StudentId = ?, BookId = ? "
					+ "WHERE ID = ?");
			st.setDate(1, new java.sql.Date(issue.getIssueDate().getTime()));
			st.setInt(2, issue.getStudentId());
			st.setInt(3, issue.getBookId());
			st.setInt(4, issue.getId());
			int rowsAffected = st.executeUpdate();
			if (rowsAffected < 1)
				throw new DatabaseException("Unexpected error. No ID match found in the database for the issue.");						
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			Database.closeStatement(st);
		}			
	}

	@Override
	public void deleteIssue(Integer issueId) {
		PreparedStatement st = null;		
		try {
			st = conn.prepareStatement("DELETE FROM issuedBooks WHERE ID = ?");			
			st.setInt(1, issueId);
			int rowsAffected = st.executeUpdate();
			if (rowsAffected < 1)
				throw new DatabaseException("Unexpected error. No ID match found in the database for the issue.");
			resetIdColumn();
		} catch (SQLException e) {
			throw new DatabaseIntegrityException(e.getMessage());
		} finally {
			Database.closeStatement(st);
		}		
	}

	@Override
	public Issue findById(Integer issueId) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM issuedBooks WHERE ID = ?");			
			st.setInt(1, issueId);
			rs = st.executeQuery();
			Issue issue = new Issue();
			if (rs.next()) {
				issue.setId(rs.getInt("ID"));
				issue.setBookId(rs.getInt("BookId"));
				issue.setStudentId(rs.getInt("StudentId"));
				issue.setIssueDate(rs.getDate("IssuedDate"));
			}
			return issue;
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			Database.closeStatement(st);
			Database.closeResultSet(rs);
		}	
	}
	
	@Override
	public List<Issue> findByStudentId(Integer studentId) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM issuedBooks WHERE StudentId = ?");
			st.setInt(1, studentId);
			rs = st.executeQuery();
			List<Issue> issueList = new ArrayList<>();
			while (rs.next()) {
				Issue issue = new Issue();
				issue.setId(rs.getInt("ID"));
				issue.setBookId(rs.getInt("BookId"));
				issue.setStudentId(rs.getInt("StudentId"));
				issue.setIssueDate(rs.getDate("IssuedDate"));
				issueList.add(issue);
			}
			return issueList;
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			Database.closeStatement(st);
			Database.closeResultSet(rs);
		}
	}
	
	@Override
	public List<Issue> findByBookId(Integer bookId) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM issuedBooks WHERE BookId = ?");
			st.setInt(1, bookId);
			rs = st.executeQuery();
			List<Issue> issueList = new ArrayList<>();
			while (rs.next()) {
				Issue issue = new Issue();
				issue.setId(rs.getInt("ID"));
				issue.setBookId(rs.getInt("BookId"));
				issue.setStudentId(rs.getInt("StudentId"));
				issue.setIssueDate(rs.getDate("IssuedDate"));
				issueList.add(issue);
			}
			return issueList;
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			Database.closeStatement(st);
			Database.closeResultSet(rs);
		}
	}

	@Override
	public List<Issue> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM issuedBooks");			
			rs = st.executeQuery();
			List<Issue> issueList = new ArrayList<>();
			while (rs.next()) {
				Issue issue = new Issue();
				issue.setId(rs.getInt("ID"));
				issue.setBookId(rs.getInt("BookId"));
				issue.setStudentId(rs.getInt("StudentId"));
				issue.setIssueDate(rs.getDate("IssuedDate"));
				issueList.add(issue);
			}
			return issueList;
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			Database.closeStatement(st);
			Database.closeResultSet(rs);
		}
	}
	
	private void resetIdColumn() {
		PreparedStatement st = null;		
		try {
			st = conn.prepareStatement("ALTER TABLE issuedBooks AUTO_INCREMENT = 1");
			st.executeUpdate();			
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			Database.closeStatement(st);
		}
	}
}
