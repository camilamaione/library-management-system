/**
 * This class implements a book data access object using the JDBC API. 
 */
package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.dao.BookDAO;
import model.database.Database;
import model.database.DatabaseException;
import model.database.DatabaseIntegrityException;
import model.entities.Book;

public class BookDAOJDBC implements BookDAO {
	
	private Connection conn;
	
	public BookDAOJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insertBook(Book book) {
		PreparedStatement st = null;		
		try {
			st = conn.prepareStatement("INSERT INTO books (Title, Author, Publisher, Quantity, Issued, Edition, NumberOfPages, Description, Year, ImageURL) "
					+ "VALUES (?, ?, ?, ?, 0, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			st.setString(1, book.getTitle());
			st.setString(2, book.getAuthor());
			st.setString(3, book.getPublisher());
			st.setInt(4, book.getQuantity());
			if (book.getEdition() == null)
				st.setNull(5, java.sql.Types.INTEGER);
			else 
				st.setInt(5, book.getEdition());
			if (book.getNumPages() == null)
				st.setNull(6, java.sql.Types.INTEGER);
			else
				st.setInt(6, book.getNumPages());			
			st.setString(7, book.getDescription());
			if (book.getYear() == null) 
				st.setNull(8, java.sql.Types.INTEGER);
			else
				st.setInt(8, book.getYear());
			st.setString(9, book.getImageUrl());
			int rowsAffected = st.executeUpdate();
			if (rowsAffected < 1)
				throw new DatabaseException("Unexpected error. No rows affected by insertion.");
			else {
				ResultSet rs = st.getGeneratedKeys();
				rs.next();
				book.setId(rs.getInt(1));
				Database.closeResultSet(rs);
			}				
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			Database.closeStatement(st);
		}		
	}

	@Override
	public void updateBook(Book book) {
		PreparedStatement st = null;		
		try {
			st = conn.prepareStatement("UPDATE books SET Title = ?, Author = ?, Publisher = ?, Quantity = ?, "
					+ "Issued = ?, Edition = ?, NumberOfPages = ?, Description = ?, Year = ?, ImageURL = ? "
					+ "WHERE ID = ?");
			st.setString(1, book.getTitle());
			st.setString(2, book.getAuthor());
			st.setString(3, book.getPublisher());
			st.setInt(4, book.getQuantity());
			st.setInt(5, book.getIssued());
			
			if (book.getEdition() == null)
				st.setNull(6, java.sql.Types.INTEGER);
			else 
				st.setInt(6, book.getEdition());			
			if (book.getNumPages() == null)
				st.setNull(7, java.sql.Types.INTEGER);
			else
				st.setInt(7, book.getNumPages());		
			st.setString(8, book.getDescription());
			if (book.getYear() == null) 
				st.setNull(9, java.sql.Types.INTEGER);
			else
				st.setInt(9, book.getYear());
			st.setString(10, book.getImageUrl());
			st.setInt(11, book.getId());
			int rowsAffected = st.executeUpdate();
			if (rowsAffected < 1)
				throw new DatabaseException("Unexpected error. No ID match found in the database for the book.");						
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			Database.closeStatement(st);
		}		
	}

	@Override
	public void deleteBook(Integer bookId) {
		PreparedStatement st = null;		
		try {
			st = conn.prepareStatement("DELETE FROM books WHERE ID = ?");			
			st.setInt(1, bookId);
			int rowsAffected = st.executeUpdate();
			if (rowsAffected < 1)
				throw new DatabaseException("Unexpected error. No ID match found in the database for the book.");
			resetIdColumn();
		} catch (SQLException e) {
			throw new DatabaseIntegrityException(e.getMessage());
		} finally {
			Database.closeStatement(st);
		}		
	}

	@Override
	public Book findById(Integer bookId) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM books WHERE ID = ?");			
			st.setInt(1, bookId);
			rs = st.executeQuery();
			Book book = new Book();
			if (rs.next()) {
				book.setId(rs.getInt("ID"));
				book.setTitle(rs.getString("Title"));
				book.setAuthor(rs.getString("Author"));
				book.setPublisher(rs.getString("Publisher"));
				book.setQuantity(rs.getInt("Quantity"));
				book.setIssued(rs.getInt("Issued"));
				book.setEdition(rs.getInt("Edition"));
				book.setNumPages(rs.getInt("NumberOfPages"));
				book.setDescription(rs.getString("Description"));
				book.setYear(rs.getInt("Year"));
				book.setImageUrl(rs.getString("ImageURL"));
			}
			return book;
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			Database.closeStatement(st);
			Database.closeResultSet(rs);
		}	
	}

	@Override
	public List<Book> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM books");			
			rs = st.executeQuery();
			List<Book> bookList = new ArrayList<>();
			while (rs.next()) {
				Book book = new Book();
				book.setId(rs.getInt("ID"));
				book.setTitle(rs.getString("Title"));
				book.setAuthor(rs.getString("Author"));
				book.setPublisher(rs.getString("Publisher"));
				book.setQuantity(rs.getInt("Quantity"));
				book.setIssued(rs.getInt("Issued"));
				book.setEdition(rs.getInt("Edition"));
				book.setNumPages(rs.getInt("NumberOfPages"));
				book.setDescription(rs.getString("Description"));
				book.setYear(rs.getInt("Year"));
				book.setImageUrl(rs.getString("ImageURL"));
				bookList.add(book);
			}
			return bookList;
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
			st = conn.prepareStatement("ALTER TABLE books AUTO_INCREMENT = 1");
			st.executeUpdate();			
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		} finally {
			Database.closeStatement(st);
		}
	}
}
