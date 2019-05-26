/**
 * This interface describes a data access object for retrieving and manipulating data from the database
 * regarding objects of Student type.
 * @author Camila Maione
 */
package model.dao;

import java.util.List;

import model.entities.Student;

public interface StudentDAO {
	
	/**
	 * Insert the data of a new student in the database.
	 * @param student The new student, whose data must be persisted.
	 */
	void insertStudent(Student student);
	
	/**
	 * Updates the data of an existing student in the database.
	 * @param student The student whose data must be persisted.
	 */
	void updateStudent(Student student);
	
	/**
	 * Delete the data of a student from the database.
	 * @param studentId The ID of the student whose data is to be removed.
	 */
	void deleteStudent(Integer studentId);
	
	/**
	 * Retrieves the data of a student whose ID is given and converts them into
	 * an object.
	 * @param studentId The ID of the student whose data is to be retrieved.
	 * @return The student retrieved according to the given ID.
	 */
	Student findById(Integer studentId);
	
	/**
	 * Retrieves the data of a student whose email is given and converts them into
	 * an object.
	 * @param studentEmail The email of the student whose data is to be retrieved.
	 * @return The student retrieved according to the given email.
	 */
	Student findByEmail(String studentEmail);
	
	/**
	 * Retrieves the data of all students stored in the database and converts them into
	 * objects.
	 * @return A list of all students stored in the database.
	 */
	List<Student> findAll();

}
