package dataAccess;

import domain.Course;
import domain.CourseType;

import java.sql.Date;
import java.util.List;


//Dieses Interface ist das eigentliche DAO.
//Es erbt alle Methoden von BaseRepository,
//in dem Fall parametrisiert mit Course als Entity und Long als Schlüsseltyp.
public interface MyCourseRepository extends BaseRepository<Course, Long> {

    List<Course> findAllCourseByName(String name);

    List<Course> findAllCourseByDescription(String description);

    List<Course> findAllCourseByNameOrDescription(String searchText);

    List<Course> findAllCourseByStartDate(Date startDate);

    List<Course> findAllCourseByCourseType(CourseType courseType);

    List<Course> findAllRunningCourses();

}
