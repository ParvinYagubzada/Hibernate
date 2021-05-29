package az.code.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public interface DataBase {
    EntityManagerFactory getFactory();

    EntityManager getManager();
}
