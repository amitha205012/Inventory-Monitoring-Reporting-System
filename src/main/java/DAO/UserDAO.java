package DAO;

import model.User;
import java.util.List;

public interface UserDAO {
    void addUser(User user);
    User getUserByEmail(String email);
    List<User> getAllUsers();
    void updateUser(User user);
}
