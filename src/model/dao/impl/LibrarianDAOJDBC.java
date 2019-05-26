/**
 * This class implements a librarian data access object using the JDBC API. 
 */
package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.dao.LibrarianDAO;
import model.database.Database;
import model.database.DatabaseException;
import model.database.DatabaseIntegrityException;
import model.entities.Librarian;

public class LibrarianDAOJDBC implements LibrarianDAO {
	
	private Connection conn = null;
	
	public LibrarianDAOJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insertLibrarian(Librarian librarian) {
		PreparedStatement st = null;		
		try {
			st = conn.prepareStatement("INSERT INTO librarian (Name, Password, Email, Address, City, Contact, isAdmin) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			st.setString(1, librarian.getName());
			st.setString(2, librarian.getPassword());
			st.setString(3, librarian.getEmail());
			st.setString(4, librarian.getAddress());
			st.setString(5, librarian.getCity());
			st.setLong(6, librarian.getContact());
			st.setBoolean(7, librarian.isAdmin());
			int rowsAffected = st.executeUpdate();
			if (rowsAffected < 1)
				throw new DatabaseException("Unexpected error. No rows affected by insertion.");
			else {
				ResultSet rs = st.getGeneratedKeys();
				rs.next();
				librarian.setId(rs.getInt(1));
				Database.closeResultSet(rs);
			}				
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			Database.closeStatement(st);
		}	
		
	}

	@Override
	public void updateLibrarian(Librarian librarian) {
		PreparedStatement st = null;		
		try {
			st = conn.prepareStatement("UPDATE librarian SET Name = ?, Password = ?, Email = ?, Address = ?, City = ?, "
					+ "Contact = ?, isAdmin = ? WHERE ID = ?");
			st.setString(1, librarian.getName());
			st.setString(2,  librarian.getPassword());
			st.setString(3, librarian.getEmail());
			st.setString(4, librarian.getAddress());
			st.setString(5, librarian.getCity());
			st.setLong(6, librarian.getContact());
			st.setBoolean(7, librarian.isAdmin());
			st.setInt(8, librarian.getId());			
			int rowsAffected = st.executeUpdate();
			if (rowsAffected < 1)
				throw new DatabaseException("Unexpected error. No ID match found in the database for the librarian.");						
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			Database.closeStatement(st);
		}	
		
	}

	@Override
	public void deleteLibrarian(Integer librarianId) {
		PreparedStatement st = null;		
		try {
			st = conn.prepareStatement("DELETE FROM librarian WHERE ID = ?");			
			st.setInt(1, librarianId);
			int rowsAffected = st.executeUpdate();
			if (rowsAffected < 1)
				throw new DatabaseException("Unexpected error. No ID match found in the database for the librarian.");
			resetIdColumn();
		} catch (SQLException e) {
			throw new DatabaseIntegrityException(e.getMessage());
		} finally {
			Database.closeStatement(st);
		}		
	}

	@Override
	public Librarian findById(Integer librarianId) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM librarian WHERE ID = ?");			
			st.setInt(1, librarianId);
			rs = st.executeQuery();
			Librarian lib = new Librarian();
			if (rs.next()) {
				lib.setId(rs.getInt("ID"));
				lib.setName(rs.getString("Name"));
				lib.setPassword(rs.getString("Password"));
				lib.setEmail(rs.getString("Email"));
				lib.setAddress(rs.getString("Address"));
				lib.setCity(rs.getString("City"));
				lib.setContact(rs.getLong("Contact"));
				lib.setAdmin(rs.getBoolean("isAdmin"));
			}
			return lib;
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			Database.closeStatement(st);
			Database.closeResultSet(rs);
		}
	}
	
	@Override
	public Librarian findByEmail(String librarianEmail) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM librarian WHERE Email = ?");			
			st.setString(1, librarianEmail);
			rs = st.executeQuery();
			Librarian lib = new Librarian();
			if (rs.next()) {
				lib.setId(rs.getInt("ID"));
				lib.setName(rs.getString("Name"));
				lib.setEmail(rs.getString("Email"));
				lib.setPassword(rs.getString("Password"));
				lib.setAddress(rs.getString("Address"));
				lib.setCity(rs.getString("City"));
				lib.setContact(rs.getLong("Contact"));
				lib.setAdmin(rs.getBoolean("isAdmin"));
			}
			return lib;
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			Database.closeStatement(st);
			Database.closeResultSet(rs);
		}
	}

	@Override
	public List<Librarian> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM librarian");			
			rs = st.executeQuery();
			List<Librarian> libList = new ArrayList<>();
			while (rs.next()) {
				Librarian lib = new Librarian();
				lib.setId(rs.getInt("ID"));
				lib.setName(rs.getString("Name"));
				lib.setPassword(rs.getString("Password"));
				lib.setEmail(rs.getString("Email"));
				lib.setAddress(rs.getString("Address"));
				lib.setCity(rs.getString("City"));
				lib.setContact(rs.getLong("Contact"));
				lib.setAdmin(rs.getBoolean("isAdmin"));
				libList.add(lib);
			}
			return libList;
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
			st = conn.prepareStatement("ALTER TABLE librarian AUTO_INCREMENT = 1");
			st.executeUpdate();			
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			Database.closeStatement(st);
		}
	}
}
