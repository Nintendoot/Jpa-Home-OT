package by.nintendo.Dao;

import by.nintendo.entity.MyUser;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
@Transactional
public class UserDao {
    private final EntityManagerFactory entityManagerFactory;

    public UserDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void save(MyUser user) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();

    }

    @Transactional(readOnly = true)
    public MyUser findById(long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        return em.find(MyUser.class, id);
    }

    public boolean findByLoginBool(String login) {
        List<MyUser> all = findAll();
        for (MyUser us : all) {
            if (us.getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

    public void deleteUser(MyUser user) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("delete from MyUser where login = : login").setParameter("login", user.getLogin()).executeUpdate();
        em.getTransaction().commit();
    }

    @Transactional(readOnly = true)
    public List<MyUser> findAll() {
        EntityManager em = entityManagerFactory.createEntityManager();
        return em.createQuery("from MyUser ", MyUser.class).getResultList();

    }

    @Transactional(readOnly = true)
    public MyUser findByLogin(String login) {
        EntityManager em = entityManagerFactory.createEntityManager();
        return em.createQuery("from MyUser where login = :login", MyUser.class)
                .setParameter("login", login)
                .getSingleResult();
    }
}
