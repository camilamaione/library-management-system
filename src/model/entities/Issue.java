package model.entities;

import java.io.Serializable;
import java.util.Date;

public class Issue implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer bookId;
	private Integer studentId;
	private Date issueDate;
	
	public Issue() { }

	public Issue(Integer bookId, Integer studentId, Date issueDate) {
		this.bookId = bookId;
		this.studentId = studentId;
		this.issueDate = issueDate;
	}
	
	public Issue(Integer id, Integer bookId, Integer studentId, Date issueDate) {
		this.id = id;
		this.bookId = bookId;
		this.studentId = studentId;
		this.issueDate = issueDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Issue other = (Issue) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "IssuedBook [id=" + id + ", bookId=" + bookId + ", studentId=" + studentId + ", issueDate=" + issueDate
				+ "]";
	}
}
