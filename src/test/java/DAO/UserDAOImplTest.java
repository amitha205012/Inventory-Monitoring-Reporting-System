package DAO;

import model.User;
import org.junit.jupiter.api.*;
import org.mockito.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserDAOImplTest {

    @Mock
    UserDAOImpl dao;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUser() throws Exception {
        User u = new User(0, "admin", "1234", "admin@test.com", "ADMIN");
        when(dao.addUser(u)).thenReturn(101);

        int id = dao.addUser(u);
        assertEquals(101, id);
        verify(dao, times(1)).addUser(u);
    }
}
