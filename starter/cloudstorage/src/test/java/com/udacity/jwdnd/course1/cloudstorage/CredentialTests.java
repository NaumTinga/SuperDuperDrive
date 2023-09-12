package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CredentialTests extends CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	private void login() {
		HomePage homePage = signUpAndLogin();
	}

	private void logout(HomePage homePage) {
		homePage.logout();
	}

	/**
	 * Test that creates a credential, and verifies it is displayed.
	 */

	@Test
	public void testCreateAndDisplayCredential() {
		HomePage homePage = signUpAndLogin();
		String url = "https://naumtinga.com";
		String username = "Goku";
		String password = "kayokenx100";
		createCredential(url, username, password, homePage);
		homePage.navToCredentialsTab();
		homePage = new HomePage(driver);
		Credential credential = homePage.getFirstCredential();
		Assertions.assertEquals(url, credential.getUrl());
		Assertions.assertEquals(username, credential.getUsername());
		deleteCredential(homePage);
		logout(homePage);
	}


	/**
	 * Test that edits an existing credential and verifies that the changes are displayed.
	 */
	@Test
	public void testModifyCredential() {
		login();
		String url = "https://naumtinga.com";
		String username = "Goku";
		String password = "kayokenx100";
		HomePage homePage = new HomePage(driver);
		createCredential(url, username, password, homePage);
		homePage.navToCredentialsTab();
		homePage = new HomePage(driver);
		homePage.editCredential();
		String modifiedUrl = "https://modified-naumtinga.com";
		homePage.setCredentialUrl(modifiedUrl);
		homePage.saveCredentialChanges();
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();
		homePage.navToCredentialsTab();
		Credential credential = homePage.getFirstCredential();
		Assertions.assertEquals(modifiedUrl, credential.getUrl());
		deleteCredential(homePage);
		logout(homePage);
	}

	private void createCredential(String url, String username, String password, HomePage homePage) {
		homePage.navToCredentialsTab();
		homePage.addNewCredential();
		homePage.setCredentialUrl(url);
		homePage.setCredentialUsername(username);
		homePage.setCredentialPassword(password);
		homePage.saveCredentialChanges();
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();
		homePage.navToCredentialsTab();
	}

	private void deleteCredential(HomePage homePage) {
		homePage.deleteCredential();
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();
	}
}
