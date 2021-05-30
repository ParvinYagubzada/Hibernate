package az.code.util;

public final class Printer {
    public static final String start = "Please select one of selections:\n";
    public static final String end = "Your selection:\t";
    public static final String menu = Color.CYAN.asString + """
            If you want to go back write "-1" in text fields.""" + Color.PURPLE.asString + """
            
            You need to exit form main menu if you want to Save unsaved data.""" + Color.BLUE.asString + """
                        
            Operations on:
            \t1. Movies.
            \t2. Genres.
            \t3. Persons.
            \t4. Professions.""" + Color.YELLOW.asString + """
                        
            \t0. Exit.
            """ + Color.RESET.asString;
    public static final String createOrSearch = Color.BLUE.asString + """
            \t1. Create new Entity.
            \t2. Select entities from db.
            \t3. Search for Update or Delete.""" + Color.YELLOW.asString + """
                        
            \t0. Back.
            """ + Color.RESET.asString;
    public static final String updateOrDelete = Color.BLUE.asString + """
            \t1. Update.
            \t2. Delete.""" + Color.YELLOW.asString + """
                        
            \t0. Back.
            """ + Color.RESET.asString;
    public static final String order =Color.BLUE.asString + """
            \t\t ORDER TYPE:
            \t1. Ascending.
            \t2. Descending""" + Color.YELLOW.asString + """
                        
            \t0. Back.
            """ + Color.RESET.asString;
    public static final String updateMovie =Color.BLUE.asString + """
            \t\t UPDATE MOVIE BY:
            \t1. Name.
            \t2. Duration
            \t3. Release date.
            \t4. Genres.""" + Color.YELLOW.asString + """
                        
            \t0. Back.
            """ + Color.RESET.asString;
    public static final String orderMovie =Color.BLUE.asString + """
            \t\t ORDER MOVIE BY:
            \t1. Name.
            \t2. Duration
            \t3. Release date.
            \t4. Id""" + Color.YELLOW.asString + """
                        
            \t0. Back.
            """ + Color.RESET.asString;
    public static final String updatePerson = Color.BLUE.asString + """
            \t\t UPDATE PERSON BY:
            \t1. Name.
            \t2. Surname.
            \t3. Patronymic.
            \t4. Birthdate.
            \t5. Profession.
            \t6. Address.
            \t7. Phone Number.
            \t7. Email.""" + Color.YELLOW.asString + """
                        
            \t0. Back.
            """ + Color.RESET.asString;
    public static final String returningBack = Color.CYAN.asString + "Returning back..." + Color.RESET.asString;
    public static final String returningMainMenu = Color.CYAN.asString + "Returning main menu..." + Color.RESET.asString;
    public static final String exiting = Color.CYAN.asString + "Exiting..." + Color.RESET.asString;

    public static <T> String colorString(Color color, T word) {
        return color.asString + word + Color.RESET.asString;
    }

    public static void print(String string) {
        System.out.print(string);
    }

    public static void println() {
        System.out.println();
    }

    public static <T> void println(T value) {
        System.out.println(value);
    }

    public static void printMenu(String menu) {
        System.out.print(start + menu);
    }

    public static void printError(String error) {
        System.out.println(Color.RED.asString + error + Color.RESET.asString);
    }

    public static void printBack() {
        println(returningBack);
    }
}