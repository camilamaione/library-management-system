package model.entities;

import java.io.Serializable;

public class Book implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String title;
	private String author;
	private String publisher;
	private Integer quantity;
	private Integer issued;
	private Integer edition;
	private Integer year;
	private Integer numPages;
	private String description;
	private String imageUrl;
	
	public Book() { }

	public Book(String title, String author, String publisher, Integer quantity, Integer issued, Integer edition,
			Integer year, Integer numPages, String description, String imageUrl) {
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.quantity = quantity;
		this.issued = issued;
		this.edition = edition;
		this.year = year;
		this.numPages = numPages;
		this.description = description;
		this.imageUrl = imageUrl;
	}

	public Book(Integer id, String title, String author, String publisher, Integer quantity, Integer issued,
			Integer edition, Integer year, Integer numPages, String description, String imageUrl) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.quantity = quantity;
		this.issued = issued;
		this.edition = edition;
		this.year = year;
		this.numPages = numPages;
		this.description = description;
		this.imageUrl = imageUrl;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getIssued() {
		return issued;
	}

	public void setIssued(Integer issued) {
		this.issued = issued;
	}

	public Integer getEdition() {
		return edition;
	}

	public void setEdition(Integer edition) {
		this.edition = edition;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getNumPages() {
		return numPages;
	}

	public void setNumPages(Integer numPages) {
		this.numPages = numPages;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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
		Book other = (Book) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + ", author=" + author + ", publisher=" + publisher + ", quantity="
				+ quantity + ", issued=" + issued + ", edition=" + edition + ", year=" + year + ", numPages=" + numPages
				+ ", description=" + description + ", imageUrl=" + imageUrl + "]";
	}	
}
