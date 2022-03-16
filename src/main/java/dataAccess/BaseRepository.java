package dataAccess;

import java.util.List;
import java.util.Optional;

//CRUD Interface mit standardisierten DB-Methoden
//parametrisiert mit generischen Typ T oder I, (als Rückgabe optional mit Inhalt oder auch ohne,
//in unserem Fall ist T der Course Typ), funktioniert für jede beliebige Art von Entitäten,
//und kann somit auch als Basis für weitere Repositories dienen.
public interface BaseRepository<T, I> {

    //Entitäten einfügen
    Optional<T> insert(T entity);

    //generischer Typ I, liefert den Schlüssel long Id von der BaseEntity
    //holt Entitäten aus der DB per Id
    Optional<T> getById(I id);

    //alle Entitäten von einem bestimmten Typ holen
    List<T> getAll();

    //Entität updaten
    Optional<T> update(T entity);

    //Entität löschen
    void deleteById(I id);
}
