package com.udacity.jwdnd.course1.cloudstorage.UserControllerTest;
import com.udacity.jwdnd.course1.cloudstorage.controller.UserController; // Update with the correct import
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        // Create a list of mock users
        List<User> mockUsers = new ArrayList<>();
        mockUsers.add(new User(1, "Username1", "Password1", "FirstName1", "LastName1", "Salt1"));
        mockUsers.add(new User(2, "Username2", "Password2", "FirstName2", "LastName2", "Salt2"));


        // Define the behavior of your userService mock
        when(userService.getAllUsers()).thenReturn(mockUsers);

        // Call your controller method
        String viewName = userController.getAllUsers(model);

        // Verify that the userService.getAllUsers() method was called
        verify(userService, times(1)).getAllUsers();

        // Verify that the "users" attribute was added to the model
        verify(model, times(1)).addAttribute(eq("users"), anyList());

        // Check that the view name returned by the controller is "home"
        assertEquals("home", viewName);
    }
}
