package DAO;

import model.User;
import org.junit.jupiter.api.*;
import testutils.TestDbUtils;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOImplIT {

    private UserDAO dao;
    private DataSource ds;

    @BeforeEach
    void setup() throws Exception {
        ds = TestDbUtils.createH2DataSource();
        TestDbUtils.runSqlScript(ds, "/Test.sql");
        dao = new UserDAOImpl(ds);
    }

    @Test
    void testCRUD() throws Exception {
        User u = new User(0,"admin","1234","admin@test.com","ADMIN");
        int id = dao.addUser(u);
        assertTrue(id > 0);

        User fetched = dao.getUserById(id);
        assertEquals("admin", fetched.getUsername());

        fetched.setUsername("superadmin");
        dao.updateUser(fetched);
        User updated = dao.getUserById(id);
        assertEquals("superadmin", updated.getUsername());

        List<User> list = dao.getAllUsers();
        assertTrue(list.size() > 0);

        dao.deleteUser(id);
        assertNull(dao.getUserById(id));
    }
}
