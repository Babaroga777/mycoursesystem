package dataAccess;

import domain.Student;
import util.Assert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlStudentRepository implements MyStudentRepository {

    private Connection con;

    public MySqlStudentRepository() throws SQLException, ClassNotFoundException {
        this.con = MySqlDatabaseConnection.getConnection("jdbc:mysql://localhost:3306/kurssystem", "root", "");
    }

    @Override
    public Optional<Student> insert(Student entity) {
        Assert.notNull(entity);
        try {
            String sql = "INSERT INTO `student` (`vorname`, `nachname`, `geburtsdatum`) VALUES (?,?,?)";
            PreparedStatement prepareStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prepareStatement.setString(1, entity.getVorname());
            prepareStatement.setString(2, entity.getNachname());
            prepareStatement.setDate(3, entity.getGeburtsdatum());
            int affectedRows = prepareStatement.executeUpdate();
            if (affectedRows == 0) {
                return Optional.empty();
            }
            ResultSet generatedKeys = prepareStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return this.getById(generatedKeys.getLong(1));
            } else {
                return Optional.empty();
            }
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public Optional<Student> getById(Long id) {
        return Optional.empty();
    }


    @Override
    public List<Student> getAll() {
        String sql = "SELECT * FROM `student`";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Student> studentList = new ArrayList<>();
            while (resultSet.next()) {
                studentList.add(new Student(
                        resultSet.getLong("id"),
                        resultSet.getString("vorname"),
                        resultSet.getString("nachname"),
                        resultSet.getDate("geburtsdatum"))
                );
            }
            return studentList;
        } catch (SQLException e) {
            throw new DatabaseException("Database error occurred!");
        }
    }

    @Override
    public Optional<Student> update(Student entity) {
        return Optional.empty();
    }


    @Override
    public void deleteById(Long id) {
        Assert.notNull(id);
        String sql = "Delete FROM `student` WHERE `id` = ?";
        try {
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            }catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }


    @Override
    public List<Student> findAllStudentByFirstnameOrLastname(String searchText) {
        return null;
    }

    @Override
    public List<Student> findAllStudentByFirstname(String vorname) {
        return null;
    }

    @Override
    public List<Student> findAllStudentByLastname(String nachname) {
        try {
            String sql = "SELECT * FROM `student` WHERE LOWER(`nachname`) LIKE LOWER(?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, "%" + nachname + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Student> studentList = new ArrayList<>();
            while (resultSet.next()) {
                studentList.add(new Student(
                        resultSet.getLong("id"),
                        resultSet.getString("vorname"),
                        resultSet.getString("nachname"),
                        resultSet.getDate("geburtsdatum")));
            }
            return studentList;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public List<Student> findAllStudentByGeburtsdatum(Date geburtsdatum) {
        return null;
    }
}
