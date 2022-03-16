package dataAccess;

import domain.Student;

import java.sql.Date;
import java.util.List;

public interface MyStudentRepository extends BaseRepository<Student, Long> {

    List<Student> findAllStudentByFirstnameOrLastname(String searchText);

    List<Student> findAllStudentByFirstname(String vorname);

    List<Student> findAllStudentByLastname(String nachname);

    List<Student> findAllStudentByGeburtsdatum(Date geburtsdatum);
}
