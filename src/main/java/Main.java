import az.code.util.PostgreDB;

public class Main {
    public static void main(String[] args) {
        PostgreDB db = new PostgreDB(PostgreDB.UnitName.IMDB);

//        manager.getTransaction().begin();
//        TypedQuery<Movie> query = manager.createQuery("SELECT movie FROM Movie movie", Movie.class);
//        query.getResultList().forEach(movie -> System.out.println(movie.getName()));
//        manager.merge(Movie.builder().name("Test").duration(123).releaseDate(LocalDate.now()).build());
//        manager.getTransaction().commit();

        db.getManager().close();
    }
}
