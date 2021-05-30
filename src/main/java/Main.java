import az.code.db.DataBase;
import az.code.db.PostgreDB;
import az.code.model.*;
import az.code.util.Color;
import az.code.util.Printer;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import static az.code.util.Printer.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DataBase db = new PostgreDB(PostgreDB.UnitName.IMDB);
    private static final EntityManager manager = db.getManager();
    private static final Pattern namePattern = Pattern.compile("\\p{Alpha}+");
    private static final String updateStatement = "Do you want to continue editing?";
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static void main(String[] args) {
        startApp();
    }

    //Prints main menu. Asks for user selection. Application runs until user wants to exit. (Option 0)
    public static void startApp() {
        int userInput = -1;
        while (userInput != 0) {
            printMenu(menu);
            userInput = getSelection(4);
            switch (userInput) {
                case 0 -> {
                    println(exiting);
                    manager.close();
                    db.getFactory().close();
                }
                case 1 -> operationMovie();
                case 2 -> operationGenre();
                case 3 -> operationPerson();
                case 4 -> operationProfession();
            }
        }
    }

    //Starts CRUD operations on Movie entities.
    //IF any selection throw Exception (BackToMenu) this try-catch block catches it and returns main menu.
    public static void operationMovie() {
        try {
            printMenu(createOrSearch);
            switch (getSelection(2)) {
                case 0, -1:
                    break;
                case 1:
                    Movie movie = getNewMovie();
                    manager.getTransaction().begin();
                    manager.merge(movie);
                    manager.getTransaction().commit();
                    println("Movie " + colorString(Color.PURPLE, movie.getName()) + " added to DB!");
                    break;
                case 2:
                    int limit = getInteger("number of entities you want to see");
                    boolean input = !askFinished("Do you want to order entities?");
                    if (input) {
                        printMovies(limit, true);
                    } else {
                        printMovies(limit);
                    }
                    break;
                case 3:
                    StringBuilder search = new StringBuilder(getName("name of movie you want to search"));
                    TypedQuery<Movie> query = manager.createQuery(
                            "SELECT movie FROM Movie movie WHERE lower(movie.name) LIKE lower(:search)", Movie.class);
                    query.setParameter("search", search.append("%").toString());
                    List<Movie> movies = query.getResultList();
                    if (movies.size() > 0) {
                        println();
                        movies.forEach(System.out::println);
                        println();
                        Integer id = getInteger("movie id you want to do operations on");
                        printMenu(updateOrDelete);
                        switch (getSelection(2)) {
                            case 0, -1:
                                break;
                            case 1:
                                Movie updated = manager.find(Movie.class, id);
                                if (updated != null) {
                                    manager.getTransaction().begin();
                                    if (updateMovie(updated)) {
                                        println("Movie " + colorString(Color.PURPLE, updated.getName()) + " updated on DB!");
                                        manager.getTransaction().commit();
                                    } else {
                                        printError("Update failed!");
                                        manager.getTransaction().rollback();
                                    }
                                } else {
                                    printError("This movie doesn't exists.");
                                    println(returningBack);
                                }
                                break;
                            case 2:
                                manager.getTransaction().begin();
                                Query delete = manager.createQuery("DELETE FROM Movie movie WHERE movie.id = :id");
                                delete.setParameter("id", id);
                                int affectedRows = delete.executeUpdate();
                                manager.getTransaction().commit();
                                if (affectedRows != 0) {
                                    printError("Delete succeeded!");
                                } else {
                                    printError("This movie doesn't exists.");
                                    printError("Delete failed!");
                                }
                                break;
                        }
                    } else {
                        printError("There is no movie name which starts with \"" + search.substring(0, search.length() - 1) + "\"");
                    }
            }
        } catch (BackToMenu ignored) {}
        println(returningMainMenu);
    }

    //Starts CRUD operations on Genre entities.
    //IF any selection throw Exception (BackToMenu) this try-catch block catches it and returns main menu.
    public static void operationGenre() {
        try {
            printMenu(createOrSearch);
            switch (getSelection(2)) {
                case 0, -1:
                    break;
                case 1:
                    Genre genre = getNewGenre();
                    manager.getTransaction().begin();
                    manager.merge(genre);
                    manager.getTransaction().commit();
                    println("Genre " + colorString(Color.PURPLE, genre.getName()) + " added to DB!");
                    break;
                case 2:
                    break;
                case 3:
                    StringBuilder search = new StringBuilder(getName("name of genre you want to search"));
                    TypedQuery<Genre> query = manager.createQuery(
                            "SELECT genre FROM Genre genre WHERE lower(genre.name) LIKE lower(:search)", Genre.class);
                    query.setParameter("search", search.append("%").toString());
                    List<Genre> genres = query.getResultList();
                    if (genres.size() > 0) {
                        println();
                        genres.forEach(System.out::println);
                        println();
                        Integer id = getInteger("genre id you want to do operations on");
                        printMenu(updateOrDelete);
                        switch (getSelection(2)) {
                            case 0, -1:
                                break;
                            case 1:
                                Genre updated = manager.find(Genre.class, id);
                                if (updated != null) {
                                    manager.getTransaction().begin();
                                    if (updateGenre(updated)) {
                                        println("Genre " + colorString(Color.PURPLE, updated.getName()) + " updated on DB!");
                                        manager.getTransaction().commit();
                                    } else {
                                        printError("Update failed!");
                                        manager.getTransaction().rollback();
                                    }
                                } else {
                                    printError("This genre doesn't exists.");
                                    println(returningBack);
                                }
                                break;
                            case 2:
                                manager.getTransaction().begin();
                                Query delete = manager.createQuery("DELETE FROM Genre genre WHERE genre.id = :id");
                                delete.setParameter("id", id);
                                int affectedRows = delete.executeUpdate();
                                manager.getTransaction().commit();
                                if (affectedRows != 0) {
                                    printError("Delete succeeded!");
                                } else {
                                    printError("This genre doesn't exists.");
                                    printError("Delete failed!");
                                }
                                break;
                        }
                    } else {
                        printError("There is no genre name which starts with \"" + search.substring(0, search.length() - 1) + "\"");
                    }
            }
        } catch (BackToMenu ignored) {
        }
        println(returningMainMenu);
    }

    //Starts CRUD operations on Profession entities.
    //IF any selection throw Exception (BackToMenu) this try-catch block catches it and returns main menu.
    public static void operationProfession() {
        try {
            printMenu(createOrSearch);
            switch (getSelection(2)) {
                case 0, -1:
                    break;
                case 1:
                    Profession profession = getNewProfession();
                    manager.getTransaction().begin();
                    manager.merge(profession);
                    manager.getTransaction().commit();
                    println("Profession " + colorString(Color.PURPLE, profession.getName()) + " added to DB!");
                    break;
                case 2:
                    break;
                case 3:
                    StringBuilder search = new StringBuilder(getName("name of profession you want to search"));
                    TypedQuery<Profession> query = manager.createQuery(
                            "SELECT profession FROM Profession profession WHERE lower(profession.name) LIKE lower(:search)", Profession.class);
                    query.setParameter("search", search.append("%").toString());
                    List<Profession> professions = query.getResultList();
                    if (professions.size() > 0) {
                        println();
                        professions.forEach(System.out::println);
                        println();
                        Integer id = getInteger("profession id you want to do operations on");
                        printMenu(updateOrDelete);
                        switch (getSelection(2)) {
                            case 0, -1:
                                break;
                            case 1:
                                Profession updated = manager.find(Profession.class, id);
                                if (updated != null) {
                                    manager.getTransaction().begin();
                                    if (updateProfession(updated)) {
                                        println("Profession " + colorString(Color.PURPLE, updated.getName()) + " updated on DB!");
                                        manager.getTransaction().commit();
                                    } else {
                                        printError("Update failed!");
                                        manager.getTransaction().rollback();
                                    }
                                } else {
                                    printError("This profession doesn't exists.");
                                    println(returningBack);
                                }
                                break;
                            case 2:
                                manager.getTransaction().begin();
                                Query delete = manager.createQuery("DELETE FROM Profession profession WHERE profession.id = :id");
                                delete.setParameter("id", id);
                                int affectedRows = delete.executeUpdate();
                                manager.getTransaction().commit();
                                if (affectedRows != 0) {
                                    printError("Delete succeeded!");
                                } else {
                                    printError("This profession doesn't exists.");
                                    printError("Delete failed!");
                                }
                                break;
                        }
                    } else {
                        printError("There is no profession name which starts with \"" + search.substring(0, search.length() - 1) + "\"");
                    }
            }
        } catch (BackToMenu ignored) {
        }
        println(returningMainMenu);
    }

    //Starts CRUD operations on Person entities.
    //IF any selection throw Exception (BackToMenu) this try-catch block catches it and returns main menu.
    public static void operationPerson() {
        try {
            printMenu(createOrSearch);
            switch (getSelection(2)) {
                case 0, -1:
                    break;
                case 1:
                    Person person = getNewPerson();
                    manager.getTransaction().begin();
                    manager.merge(person);
                    manager.getTransaction().commit();
                    println("Person " + colorString(Color.PURPLE, person.getName()) + " added to DB!");
                    break;
                case 2:
                    break;
                case 3:
                    StringBuilder search = new StringBuilder(getName("name of person you want to search"));
                    TypedQuery<Person> query = manager.createQuery(
                            "SELECT person FROM Person person WHERE lower(person.name) LIKE lower(:search)", Person.class);
                    query.setParameter("search", search.append("%").toString());
                    List<Person> persons = query.getResultList();
                    if (persons.size() > 0) {
                        println();
                        persons.forEach(System.out::println);
                        println();
                        Integer id = getInteger("person id you want to do operations on");
                        printMenu(updateOrDelete);
                        switch (getSelection(2)) {
                            case 0, -1:
                                break;
                            case 1:
                                Person updated = manager.find(Person.class, id);
                                if (updated != null) {
                                    manager.getTransaction().begin();
                                    if (updatePerson(updated)) {
                                        println("Person " + colorString(Color.PURPLE, updated.getName()) + " updated on DB!");
                                        manager.getTransaction().commit();
                                    } else {
                                        printError("Update failed!");
                                        manager.getTransaction().rollback();
                                    }
                                } else {
                                    printError("This person doesn't exists.");
                                    println(returningBack);
                                }
                                break;
                            case 2:
                                manager.getTransaction().begin();
                                Query delete = manager.createQuery("DELETE FROM Person person WHERE person.id = :id");
                                delete.setParameter("id", id);
                                int affectedRows = delete.executeUpdate();
                                manager.getTransaction().commit();
                                if (affectedRows != 0) {
                                    printError("Delete succeeded!");
                                } else {
                                    printError("This person doesn't exists.");
                                    printError("Delete failed!");
                                }
                                break;
                        }
                    } else {
                        printError("There is no person name which starts with \"" + search.substring(0, search.length() - 1) + "\"");
                    }
            }
        } catch (BackToMenu ignored) {
        }
        println(returningMainMenu);
    }

    /**
     * Gets selections for switches.
     * Continues until user inserts "-1" or valid selection.
     *
     * @param limit limits selection from 0 (inclusive) to limit (inclusive).
     **/
    private static void printMovies(int limit, boolean order) {
        String sql = "SELECT movie FROM Movie movie";
        if (order) {
            printMenu(orderMovie);
            sql += " ORDER BY movie.";
            switch (getSelection(4)) {
                case -1, 0 -> throw new BackToMenu();
                case 1 -> sql += "name";
                case 2 -> sql += "duration";
                case 3 -> sql += "releaseDate";
                case 4 -> sql += "id";
            }
            printMenu(Printer.order);
            switch (getSelection(2)) {
                case -1, 0 -> throw new BackToMenu();
                case 1 -> sql += " ASC";
                case 2 -> sql += " DESC";
            }
        }
        TypedQuery<Movie> query = manager.createQuery(sql, Movie.class);
        query.setMaxResults(limit);
        List<Movie> movies = query.getResultList();
        movies.forEach(System.out::println);
    }

    private static void printMovies(int limit) {
        printMovies(limit, false);
    }

    /**
     * Gets selections for switches.
     * Continues until user inserts "-1" or valid selection.
     *
     * @param limit limits selection from 0 (inclusive) to limit (inclusive).
     * @return integer selection which will be used in switches.
     **/
    private static int getSelection(int limit) {
        int selection = Integer.MIN_VALUE;
        do {
            try {
                if (selection != Integer.MIN_VALUE)
                    printError("Please select only number 0 to " + limit);
                print(end);
                selection = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException exception) {
                scanner.nextLine();
                selection = -2;
            }
        } while (selection < -1 || selection > limit);
        if (selection == -1)
            printBack();
        return selection;
    }

    /**
     * Gets name of entities fields for updating or creating new entity.
     * Continues until user inserts "-1" or valid name.
     *
     * @param specification name of field that you want user to insert.
     * @return string, valid name.
     * @throws BackToMenu if user inserts "-1" as input.
     **/
    private static String getName(String specification) {
        while (true) {
            print("Please enter " + specification + ": ");
            String str = scanner.nextLine();
            if (str.equals("-1"))
                throw new BackToMenu();
            if (namePattern.matcher(str).matches())
                return str;
            printError("Your input was incorrect! Please enter again.");
        }
    }

    /**
     * Gets integer value of fields for updating or creating new entity.
     * Continues until user inserts "-1" or valid integer.
     *
     * @param specification name of field that you want user to insert.
     * @return string, valid integer.
     * @throws BackToMenu if user inserts "-1" as input.
     **/
    private static Integer getInteger(String specification) {
        while (true) {
            print("Please enter " + specification + ": ");
            String str = scanner.nextLine();
            if (str.equals("-1"))
                throw new BackToMenu();
            try {
                int value = Integer.parseInt(str);
                if (value > 0)
                    return value;
                else
                    printError("Negative duration!");
            } catch (ClassCastException ignored) {
            }
            printError("Your input was incorrect! Please enter again.");
        }
    }

    /**
     * Gets new date for updating or creating entity.
     * Continues until user inserts "-1" or valid name.
     *
     * @param specification name of field that you want user to insert.
     * @return LocalDate, valid date.
     * @throws BackToMenu if user inserts "-1" as input.
     **/
    private static LocalDate getDate(String specification) {
        while (true) {
            print("Please enter " + specification + " with this format XX.XX.XXXX: ");
            String str = scanner.nextLine();
            if (str.equals("-1"))
                throw new BackToMenu();
            try {
                return LocalDate.parse(str, dateFormatter);
            } catch (ClassCastException ignored) {
            }
            printError("Your input was incorrect.");
        }
    }

    /**
     * Gets all genres and gives option to user for selecting multiple genres.
     * Continues until user inserts "-1" or valid name.
     *
     * @return List of genres that user selected.
     * @throws BackToMenu if user inserts "-1" as input.
     **/
    private static List<Genre> getGenres() {
        List<Genre> selectedGenres = new LinkedList<>();
        TypedQuery<Genre> query = manager.createQuery("SELECT genre FROM Genre genre", Genre.class);
        List<Genre> allGenres = query.getResultList();
        int count = 1;
        println("All Genres in Data base");
        for (Genre genre : allGenres) {
            println(count + ". " + genre.getName());
            count++;
        }
        print("Enter genre numbers by comma without any whitespace characters: ");
        String input = scanner.nextLine();
        Pattern pattern = Pattern.compile("^\\p{Digit}+(,\\p{Digit}+)*$");
        if (pattern.matcher(input).matches()) {
            for (String number : input.split(",")) {
                try {
                    selectedGenres.add(allGenres.get(Integer.parseInt(number) - 1));
                } catch (IndexOutOfBoundsException ignored) {
                    printError("This genre does not exists.");
                    throw new BackToMenu();
                }
            }
        }
        return selectedGenres;
    }

    /**
     * Gets profession for updating or creating new person.
     * Continues until user inserts "-1" or valid name.
     *
     * @return Profession, profession of Person selected by user.
     * @throws BackToMenu if user inserts "-1" as input.
     **/
    private static Profession getProfession() {
        TypedQuery<Profession> query = manager.createQuery("SELECT prof FROM Profession prof", Profession.class);
        List<Profession> allProfessions = query.getResultList();
        int count = 1;
        println("All Genres in Data base");
        for (Profession prof : allProfessions) {
            println(count + ". " + prof.getName());
            count++;
        }
        int selection = getInteger("profession number");
        return allProfessions.get(selection - 1);
    }

    //Asks new PersonDetail data for creating new PersonDetail object.
    private static PersonDetail getDetails() {
        String address = getName("address of person");
        String phoneNumber = getName("phone number of person");
        String email = getName("email of person");
        return PersonDetail.builder()
                .address(address)
                .phoneNumber(phoneNumber)
                .email(email).build();
    }

    //Asks new Movie data for creating new Movie object.
    private static Movie getNewMovie() {
        String name = getName("name of movie");
        Integer duration = getInteger("duration of movie");
        LocalDate date = getDate("release date of movie");
        List<Genre> genres = getGenres();
        return Movie.builder()
                .name(name)
                .duration(duration)
                .releaseDate(date)
                .genres(genres).build();
    }

    //Asks new Movie data for creating new Movie object.
    private static Person getNewPerson() {
        String name = getName("name of person");
        String surname = getName("surname of person");
        String patronymic = getName("patronymic of person");
        LocalDate birthDate = getDate("birthday of person");
        Profession profession = getProfession();
        PersonDetail detail = getDetails();
        System.out.println(detail.getAddress());
        return Person.builder()
                .name(name)
                .surname(surname)
                .patronymic(patronymic)
                .birthDate(birthDate)
                .profession(profession)
                .detail(detail).build();
    }

    //Asks new Genre data for creating new Genre object.
    private static Genre getNewGenre() {
        String name = getName("name of genre");
        return Genre.builder()
                .name(name).build();
    }

    //Asks new Profession data for creating new Profession object.
    private static Profession getNewProfession() {
        String name = getName("name of profession");
        return Profession.builder()
                .name(name).build();
    }

    /**
     * Updates Movie object.
     * Continues until user selects exit (0) option.
     * Or user selects -1 for returning main menu.
     * Or until user inserts "no" after updating 1 field of Movie object.
     *
     * @param movie selected object wanted to be updated.
     * @return boolean true if any field of Movie object has been updated, false if update process fails.
     **/
    private static boolean updateMovie(Movie movie) {
        boolean finished = true;
        boolean updated = false;
        do {
            printMenu(updateMovie);
            int selection = getSelection(4);
            switch (selection) {
                case 0, -1:
                    return updated;
                case 1:
                    movie.setName(getName("name"));
                    finished = askFinished(updateStatement);
                    updated = true;
                    break;
                case 2:
                    movie.setDuration(getInteger("duration"));
                    finished = askFinished(updateStatement);
                    updated = true;
                    break;
                case 3:
                    movie.setReleaseDate(getDate("release date"));
                    finished = askFinished(updateStatement);
                    updated = true;
                    break;
                case 4:
                    movie.setGenres(getGenres());
                    finished = askFinished(updateStatement);
                    updated = true;
                    break;
            }
        } while (!finished);
        return true;
    }

    /**
     * Updates Genre object.
     * Continues until user selects exit (0) option.
     * Or user selects -1 for returning main menu.
     * Or until user inserts "no" after updating 1 field of Genre object.
     *
     * @param genre selected object wanted to be updated.
     * @return boolean true if any field of Genre object has been updated, false if update process fails.
     **/
    private static boolean updateGenre(Genre genre) {
        genre.setName(getName("name"));
        return true;
    }

    /**
     * Updates Profession object.
     * Continues until user selects exit (0) option.
     * Or user selects -1 for returning main menu.
     * Or until user inserts "no" after updating 1 field of Profession object.
     *
     * @param profession selected object wanted to be updated.
     * @return boolean true if any field of Profession object has been updated, false if update process fails.
     **/
    private static boolean updateProfession(Profession profession) {
        profession.setName(getName("name"));
        return true;
    }

    /**
     * Updates Person object.
     * Continues until user selects exit (0) option.
     * Or user selects -1 for returning main menu.
     * Or until user inserts "no" after updating 1 field of Person object.
     *
     * @param person selected object wanted to be updated.
     * @return boolean true if any field of Person object has been updated, false if update process fails.
     **/
    private static boolean updatePerson(Person person) {
        boolean finished = true;
        boolean updated = false;
        do {
            printMenu(updatePerson);
            int selection = getSelection(4);
            switch (selection) {
                case 0, -1:
                    return updated;
                case 1:
                    person.setName(getName("name"));
                    finished = askFinished(updateStatement);
                    updated = true;
                    break;
                case 2:
                    person.setSurname(getName("surname"));
                    finished = askFinished(updateStatement);
                    updated = true;
                    break;
                case 3:
                    person.setPatronymic(getName("patronymic"));
                    finished = askFinished(updateStatement);
                    updated = true;
                    break;
                case 4:
                    person.setBirthDate(getDate("birthdate"));
                    finished = askFinished(updateStatement);
                    updated = true;
                    break;
                case 5:
                    person.setProfession(getProfession());
                    finished = askFinished(updateStatement);
                    updated = true;
                    break;
                case 6:
                    person.getDetail().setAddress(getName("address"));//TODO: Create new method for input check
                    finished = askFinished(updateStatement);
                    updated = true;
                    break;
                case 7:
                    person.getDetail().setPhoneNumber(getName("phone number"));//TODO: Create new method for input check
                    finished = askFinished(updateStatement);
                    updated = true;
                    break;
                case 8:
                    person.getDetail().setEmail(getName("email"));//TODO: Create new method for input check
                    finished = askFinished(updateStatement);
                    updated = true;
                    break;
            }
        } while (!finished);
        return true;
    }

    /**
     * Asks user to insert "yes" or "no" inputs.
     * This indicates that if user wants to continue editing or wants to stop and return main menu.
     * Continues until user inserts "-1" or valid input.
     *
     * @param specification finishing statement.
     * @return boolean true if any user inserts valid "yes", false if inserts valid "no".
     * @throws BackToMenu if user inserts "-1" as input.
     **/
    private static boolean askFinished(String specification) {
        Pattern pattern = Pattern.compile("(?i)([yn]|(?i)(yes|no))");
        do {
            print(specification + " Yes/No: ");
            String ans = scanner.next();
            if (pattern.matcher(ans).matches()) {
                return !ans.equalsIgnoreCase("yes");
            } else if (ans.equals("-1")) {
                throw new BackToMenu();
            }
            printError("Enter \"Yes\" or \"No\" to continue.");
        } while (true);
    }

    //Exception for escaping input fields to main menu.
    static class BackToMenu extends RuntimeException {
        public BackToMenu() {
            println(returningBack);
        }
    }
}
