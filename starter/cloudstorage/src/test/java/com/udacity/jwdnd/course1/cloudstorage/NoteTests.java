package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NoteTests extends CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	private UserMapper userMapper;
	public NoteMapper noteMapper;

	@BeforeAll
	public static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		HomePage homePage = new HomePage(driver);
		NoteService noteService = new NoteService(userMapper, noteMapper);
		login();
	}

	@AfterEach
	public void afterEach(HomePage homePage) {
		homePage.logout();
		if (this.driver != null) {
			driver.quit();
		}
	}

	@AfterAll
	public static void afterAll() {
		// Clean up resources
	}

	@Test
	public void testDelete() {
		String noteTitle = "My First Note";
		String noteDescription = "This is my first note.";
		HomePage homePage = signUpAndLogin();
		createNote(noteTitle, noteDescription, homePage);
		homePage.navToNotesTab();
		homePage = new HomePage(driver);
		Assertions.assertFalse(homePage.noNotes(driver));
		deleteNote(homePage);
		Assertions.assertTrue(homePage.noNotes(driver));
	}

	@Test
	public void testCreateAndDisplay() {
		String noteTitle = "My first Note";
		String noteDescription = "This is my first note.";
		HomePage homePage = signUpAndLogin();
		createNote(noteTitle, noteDescription, homePage);
		homePage.navToNotesTab();
		homePage = new HomePage(driver);
		Note note = homePage.getFirstNote();
		Assertions.assertEquals(noteTitle, note.getNoteTitle());
		Assertions.assertEquals(noteDescription, note.getNoteDescription());
		deleteNote(homePage);
		homePage.logout();
	}

	@Test
	public void testModify() {
		String noteTitle = "My First Note";
		String noteDescription = "This is my first note.";
		HomePage homePage = signUpAndLogin();
		createNote(noteTitle, noteDescription, homePage);
		homePage.navToNotesTab();
		homePage = new HomePage(driver);
		homePage.editNote();
		String modifiedNoteTitle = "My First Modified Note";
		homePage.modifyNoteTitle(modifiedNoteTitle);
		String modifiedNoteDescription = "This is my first modified note.";
		homePage.modifyNoteDescription(modifiedNoteDescription);
		homePage.saveNoteChanges();
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();
		homePage.navToNotesTab();
		Note note = homePage.getFirstNote();
		Assertions.assertEquals(modifiedNoteTitle, note.getNoteTitle());
		Assertions.assertEquals(modifiedNoteDescription, note.getNoteDescription());
	}

	private void createNote(String noteTitle, String noteDescription, HomePage homePage) {
		homePage.navToNotesTab();
		homePage.addNewNote();
		homePage.setNoteTitle(noteTitle);
		homePage.setNoteDescription(noteDescription);
		homePage.saveNoteChanges();
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();
		homePage.navToNotesTab();
	}

	private void deleteNote(HomePage homePage) {
		homePage.deleteNote();
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();
	}

	private void login() {
		HomePage homePage = signUpAndLogin();
	}

	private void logout(HomePage homePage) {
		homePage.logout();
	}
}
