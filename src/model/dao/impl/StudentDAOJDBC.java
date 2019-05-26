/**
 * This class implements a student data access object using the JDBC API. 
 */
package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.dao.StudentDAO;
import model.database.Database;
import model.database.DatabaseException;
import model.database.DatabaseIntegrityException;
import model.entities.Student;

public class StudentDAOJDBC implements StudentDAO {
	
	private Connection conn = null;
	
	public StudentDAOJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insertStudent(Student student) {
		PreparedStatement st = null;		
		try {
			st = conn.prepareStatement("INSERT INTO student (Name, Email, Contact) "
					+ "VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			st.setString(1, student.getName());
			st.setString(2, student.getEmail());
			st.setLong(3, student.getContact());
			int rowsAffected = st.executeUpdate();
			if (rowsAffected < 1)
				throw new DatabaseException("Unexpected error. No rows affected by insertion.");
			else {
				ResultSet rs = st.getGeneratedKeys();
				rs.next();
				student.setId(rs.getInt(1));
				Database.closeResultSet(rs);
			}				
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			Database.closeStatement(st);
		}		
	}

	@Override
	public void updateStudent(Student student) {
		PreparedStatement st = null;		
		try {
			st = conn.prepareStatement("UPDATE student SET Name = ?, Email = ?, Contact = ? "
					+ "WHERE ID = ?");
			st.setString(1, student.getName());
			st.setString(2, student.getEmail());
			st.setLong(3, student.getContact());			
			st.setInt(4, student.getId());
			int rowsAffected = st.executeUpdate();
			if (rowsAffected < 1)
				throw new DatabaseException("Unexpected error. No ID match found in the database for the student.");						
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			Database.closeStatement(st);
		}
		
	}

	@Override
	public void deleteStudent(Integer studentId) {
		PreparedStatement st = null;		
		try {
			st = conn.prepareStatement("DELETE FROM student WHERE ID = ?");			
			st.setInt(1, studentId);
			int rowsAffected = st.executeUpdate();
			if (rowsAffected < 1)
				throw new DatabaseException("Unexpected error. No ID match found in the database for the student.");
			resetIdColumn();
		} catch (SQLException e) {
			throw new DatabaseIntegrityException(e.getMessage());
		} finally {
			Database.closeStatement(st);
		}
		
	}

	@Override
	public Student findById(Integer studentId) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM student WHERE ID = ?");			
			st.setInt(1, studentId);
			rs = st.executeQuery();
			Student student = new Student();
			if (rs.next()) {
				student.setId(rs.getInt("ID"));
				student.setName(rs.getString("Name"));
				student.setEmail(rs.getString("Email"));				
				student.setContact(rs.getLong("Contact"));
			}
			return student;
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			Database.closeStatement(st);
			Database.closeResultSet(rs);
		}	
	}
	
	@Override
	public Student findByEmail(String studentEmail) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM student WHERE Email = ?");			
			st.setString(1, studentEmail);
			rs = st.executeQuery();
			Student student = new Student();
			if (rs.next()) {
				student.setId(rs.getInt("ID"));
				student.setName(rs.getString("Name"));
				student.setEmail(rs.getString("Email"));				
				student.setContact(rs.getLong("Contact"));
			}
			return student;
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			Database.closeStatement(st);
			Database.closeResultSet(rs);
		}	
	}

	@Override
	public List<Student> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM student");			
			rs = st.executeQuery();
			List<Student> studentsList = new ArrayList<>();
			while (rs.next()) {
				Student student = new Student();
				student.setId(rs.getInt("ID"));
				student.setName(rs.getString("Name"));
				student.setEmail(rs.getString("Email"));				
				student.setContact(rs.getLong("Contact"));
				studentsList.add(student);
			}
			return studentsList;
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
			st = conn.prepareStatement("ALTER TABLE student AUTO_INCREMENT = 1");
			st.executeUpdate();			
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			Database.closeStatement(st);
		}
	}
}
