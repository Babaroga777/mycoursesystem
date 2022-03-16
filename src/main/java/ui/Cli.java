package ui;

import dataAccess.DatabaseException;
import dataAccess.MyCourseRepository;
import dataAccess.MyStudentRepository;
import domain.Course;
import domain.CourseType;
import domain.InvalidValueException;
import domain.Student;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Cli {

    Scanner scan;
    MyCourseRepository repo;
    MyStudentRepository studentRepo;

    public Cli(MyCourseRepository repo, MyStudentRepository studentRepo) {
        this.scan = new Scanner(System.in);
        this.repo = repo;
        this.studentRepo = studentRepo;
    }

    public void start() {
        String input = "-";
        while (!input.equals("x")) {
            showMenue();
            input = scan.nextLine();
            switch (input) {
                case "1":
                    addCourse();
                    break;
                case "2":
                    showAllCourses();
                    break;
                case "3":
                    showCourseDetails();
                    break;
                case "4":
                    updateCourseDetails();
                    break;
                case "5":
                    deleteCourse();
                    break;
                case "6":
                    courseSearch();
                    break;
                case "7":
                    runningCourses();
                    break;
                case "8":
                    addStudent();
                    break;
                case "9":
                    showAllStudent();
                    break;
                case "0":
                    deleteStudent();
                    break;
                case "10":
                    studentSearchLastname();
                    break;
                case "x":
                    System.out.println("Auf Wiedersehen");
                    break;
                default:
                    inputError();
                    break;
            }
        }
        scan.close();
    }

    private void studentSearchLastname() {
        System.out.println("Geben Sie den Nachnamen an!");
        System.out.print("Eingabe: ");
        String searchText = scan.nextLine();
        List<Student> studentList;
        try {
            studentList = studentRepo.findAllStudentByLastname(searchText);
            for (Student student : studentList) {
                System.out.println(student);
            }
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbank fehler bei der Studenten-Suche: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler bei der Studenten-Suche " + e.getMessage());
        }
    }

    private void deleteStudent() {
        System.out.println("Welchen Student möchten Sie löschen? Bitte ID eingeben.");
        Long studentIdToDelete = Long.parseLong(scan.nextLine());
        try {
            studentRepo.deleteById(studentIdToDelete);
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim löschen: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler beim löschen: " + e.getMessage());
        }
    }

    private void showAllStudent() {
        List<Student> students = null;
        try {
            students = studentRepo.getAll();
            if (students.size() > 0) {
                for (Student student : students) {
                    System.out.println(student);
                }
            } else {
                System.out.println("Studenten-Liste leer!");
            }
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei Anzeige aller Studenten: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler bei Anzeige aller Studenten:  " + e.getMessage());
        }
    }

    private void addStudent() {
        String vorname, nachname;
        Date geburtsdatum;
        try {
            System.out.println("Bitte alle Daten vom Studenten eingeben: ");
            System.out.print("Vorname: ");
            vorname = scan.nextLine();
            if (vorname.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein");
            System.out.print("Nachname: ");
            nachname = scan.nextLine();
            if (nachname.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");
            System.out.print("Geburtsdatum (YYYY-MM-DD): ");
            geburtsdatum = Date.valueOf(scan.nextLine());
            Optional<Student> optionalStudent = studentRepo.insert(new Student(vorname, nachname, geburtsdatum));
            if (optionalStudent.isPresent()) {
                System.out.println("Student angelegt: " + optionalStudent.get());
            } else {
                System.out.println("Student kann nicht angelegt werden!");
            }

        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        } catch (InvalidValueException invalidValueException) {
            System.out.println("Studentendaten nicht korrekt angegeben: " + invalidValueException.getMessage());
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim einügen: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler beim einfügen: " + e.getMessage());
        }
    }

    private void runningCourses() {
        System.out.println("Aktuell laufende Kurse: ");
        List<Course> list;
        try {
            list = repo.findAllRunningCourses();
            for (Course course : list) {
                System.out.println(course);
            }
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei Kurs-Anzeige für laufende Kurse: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler bei Kurs-Anzeige für laufende Kurse: " + exception.getMessage());
        }
    }

    private void courseSearch() {
        System.out.println("Geben Sie einen Suchbegriff an!");
        String searchString = scan.nextLine();
        List<Course> courseList;
        try {
            courseList = repo.findAllCourseByNameOrDescription(searchString);
            for (Course course : courseList) {
                System.out.println(course);
            }
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei der Kurssuche: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler bei der Kurssuche " + e.getMessage());
        }
    }

    private void deleteCourse() {
        System.out.println("Welchen Kurs möchten Sie löschen? Bitte ID eingeben: ");
        Long courseIdToDelete = Long.parseLong(scan.nextLine());

        try {
            repo.deleteById(courseIdToDelete);
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim löschen: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler beim löschen: " + e.getMessage());
        }
    }

    private void updateCourseDetails() {
        System.out.println("Für welche Kurs-ID möchten Sie die Kursdetails ändern?");
        Long courseId = Long.parseLong(scan.nextLine());

        try {
            Optional<Course> courseOptional = repo.getById(courseId);
            if (courseOptional.isEmpty()) {
                System.out.println("Kurs mit der gegebenen ID " + courseId + " nicht in der Datenbank!");
            } else {
                Course course = courseOptional.get();
                System.out.println("Änderung für folgenden Kurs: ");
                System.out.println(course);

                String name, description, hours, dateFrom, dateTo, courseType;

                System.out.println("Bitte neue Kursdaten angaben (Enter, falls keine Änderungen gewünscht ist): ");
                System.out.print("Name: ");
                name = scan.nextLine();
                System.out.print("Beschreibung: ");
                description = scan.nextLine();
                System.out.print("Stundenanzahl: ");
                hours = scan.nextLine();
                System.out.print("Startdatum (YYYY-MM-DD): ");
                dateFrom = scan.nextLine();
                System.out.print("Enddatum (YYYY-MM-DD): ");
                dateTo = scan.nextLine();
                System.out.print("KursTyp (ZA/BF/FF/OE): ");
                courseType = scan.nextLine();

                Optional<Course> optionalCourseUpdated = repo.update(
                        new Course(
                                course.getId(),

                                //bei Leereingabe werden die bestehenden Daten behalten (Abfrage mit course.getName),
                                // sonst wird die Benutzereingabe verwendet (name)
                                // ? und : ternäre Operatoren,  sind soviel wie if und else
                                name.equals("") ? course.getName() : name,
                                description.equals("") ? course.getDescription() : description,
                                hours.equals("") ? course.getHours() : Integer.parseInt(hours),
                                dateFrom.equals("") ? course.getBeginDate() : Date.valueOf(dateFrom),
                                dateTo.equals("") ? course.getEndDate() : Date.valueOf(dateTo),
                                courseType.equals("") ? course.getCourseType() : CourseType.valueOf(courseType)
                        )
                );
                optionalCourseUpdated.ifPresentOrElse(
                        (c) -> System.out.println("Kurs aktualisiert: " + c),
                        () -> System.out.println("Kurs konnte nicht aktualisiert werden!"));
            }
        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        } catch (InvalidValueException invalidValueException) {
            System.out.println("Kursdaten nicht korrekt angegeben: " + invalidValueException.getMessage());
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Kurupdate: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler beim Kurupdate: " + e.getMessage());
        }
    }

    private void addCourse() {

        String name, description;
        int hours;
        Date dateFrom, dateTo;
        CourseType courseType;

        try {
            System.out.println("Bitte alle Kursdaten angeben: ");
            System.out.println("Name: ");
            name = scan.nextLine();

            //Userinput Validierung
            if (name.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein");
            System.out.println("Beschreibung: ");
            description = scan.nextLine();
            if (description.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");
            System.out.println("Stundenanzahl: ");
            hours = Integer.parseInt(scan.nextLine());
            System.out.println("Startdatum (YYYY-MM-DD): ");
            dateFrom = Date.valueOf(scan.nextLine());
            System.out.println("Enddatum (YYYY-MM-DD): ");
            dateTo = Date.valueOf(scan.nextLine());
            System.out.println("KursTyp (ZA/BF/FF/OE): ");
            courseType = CourseType.valueOf(scan.nextLine());
            Optional<Course> optionalCourse = repo.insert(
                    new Course(name, description, hours, dateFrom, dateTo, courseType));
            if (optionalCourse.isPresent()) {
                System.out.println("Kurs angelegt: " + optionalCourse.get());
            } else {
                System.out.println("Kurs kann nicht angelegt werden!");
            }

        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        } catch (InvalidValueException invalidValueException) {
            System.out.println("Kursdaten nicht korrekt angegeben: " + invalidValueException.getMessage());
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Einfügen: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler beim Einfügen: " + e.getMessage());
        }
    }


    private void showCourseDetails() {
        System.out.println("Für welchen Kurs möchten Sie die Kurs Details anzeigen?");
        System.out.println("Eingabe: ");
        Long courseId = Long.parseLong(scan.nextLine());
        try {
            Optional<Course> courseOptional = repo.getById(courseId);
            if (courseOptional.isPresent()) {
                System.out.println(courseOptional.get());
            } else {
                System.out.println("Kurs mit der ID " + courseId + " nicht gefunden!");
            }

        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei Kurs-Detailsanzeige: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Ubnbekannter Fehler bei Kurs-Detailanzeige: " + e.getMessage());
        }
    }

    private void showAllCourses() {
        List<Course> list = null;

        try {
            list = repo.getAll();
            if (list.size() > 0) {
                for (Course course : list) {
                    System.out.println(course);
                }
            } else {
                System.out.println("Kursliste leer!");
            }
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei Anzeige aller Kurse: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler bei Anzeigen aller Kurse: " + exception.getMessage());
        }
    }

    private void showMenue() {
        System.out.println("---------------------------------------KURSMANAGMENT----------------------------------------");
        System.out.println("(1) Kurs eingeben \t\t (2) Alle Kurse anzeigen \t\t\t (3) Kursdetails anzeigen");
        System.out.println("(4) Kursdetails ändern \t (5) Kurs löschen \t\t\t\t\t (6) Kurssuche");
        System.out.println("(7) Laufende Kurse \t\t (8) Student anlegen \t\t\t\t (9) Alle Studierenden anzeigen");
        System.out.println("(0) Studenten löschen \t (10) Student per Nachnamen suchen \t (x) ENDE");
    }

    private void inputError() {
        System.out.println("Bitte nur die Zahlen der Menüauswahl eingeben!");
    }
}
