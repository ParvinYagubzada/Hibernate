package az.code.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class PostgreDB {
    private final EntityManagerFactory factory;
    private final EntityManager manager;

    public PostgreDB(UnitName name) {
        this.factory = Persistence.createEntityManagerFactory(name.asString);
        this.manager = factory.createEntityManager();
    }

    public EntityManagerFactory getFactory() {
        return factory;
    }

    public EntityManager getManager() {
        return manager;
    }

    public enum UnitName {
        IMDB("Imdb");

        String asString;

        UnitName(String asString) {
            this.asString = asString;
        }
    }
}
