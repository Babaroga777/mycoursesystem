package domain;

import java.sql.Date;

public class Student extends BaseEntity {
    private String vorname;
    private String nachname;
    private Date geburtsdatum;

    public Student(Long id, String vorname, String nachname, Date geburtsdatum) throws InvalidValueException {
        super(id);
        this.setVorname(vorname);
        this.setNachname(nachname);
        this.setGeburtsdatum(geburtsdatum);
    }

    public Student(String vorname, String nachname, Date geburtsdatum) throws InvalidValueException {
        super(null);
        this.setVorname(vorname);
        this.setNachname(nachname);
        this.setGeburtsdatum(geburtsdatum);
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) throws InvalidValueException {
        if (vorname != null && vorname.length() > 2) {
            this.vorname = vorname;
        } else {
            throw new InvalidValueException("Vorname muss mindestens 3 Zeichen lang sein!");
        }
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) throws InvalidValueException {
        if (nachname != null && nachname.length() > 2) {
            this.nachname = nachname;
        } else {
            throw new InvalidValueException("Nachname muss mindestens 3 Zeichen lang sein!");
        }
    }

    public Date getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(Date geburtsdatum) throws InvalidValueException {
        long now = System.currentTimeMillis();
        Date sqlDateNow = new Date(now);
        if (geburtsdatum != null) {
            if (geburtsdatum.before(sqlDateNow)) {
                this.geburtsdatum = geburtsdatum;
            } else {
                throw new InvalidValueException("Geburtsdatum muss in der Vergangenheit sein!");
            }
        } else {
            throw new InvalidValueException("Geburtsdatum darf nicht null/leer sein!");
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + super.getId() + '\'' +
                ", vorname='" + vorname + '\'' +
                ", nachname='" + nachname + '\'' +
                ", geburtsdatum=" + geburtsdatum +
                '}';
    }
}
