package by.nintendo.service;

import by.nintendo.Dao.UserDao;
import by.nintendo.entity.MyUser;
import by.nintendo.entity.Role;
import by.nintendo.exception.NotAllDataEnteredException;
import by.nintendo.exception.UserAlreadyExistsException;
import by.nintendo.exception.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder, UserDao userDao) {
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
    }

    public void save(MyUser user) {
            if (userDao.findByLoginBool(user.getLogin())) {
                throw new UserAlreadyExistsException("User already exists");
            } else {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setRole(Role.USER);
                userDao.save(user);
            }
    }

    public MyUser findById(long id) {
        MyUser byId = userDao.findById(id);
        if(byId!=null){
            return byId;
        }else {
            throw new UserNotFoundException("User not found");
        }
    }

    public void deleteUser(MyUser user) {
        if (userDao.findByLoginBool(user.getLogin())) {
            userDao.deleteUser(user);
        } else {
            throw new UserNotFoundException("There is no such user.");
        }
    }

    public List<MyUser> findAll() {
        return userDao.findAll();
    }

    public MyUser findByLogin(String login) {
        if (!login.isEmpty()) {
            if (userDao.findByLoginBool(login)) {
                return userDao.findByLogin(login);
            } else {
                throw new UserNotFoundException("User not found");
            }
        } else {
            throw new NotAllDataEnteredException("Not data entered");
        }
    }
}
