package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.File;
import java.time.Duration;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private WebDriverWait webDriverWait;

    @BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	private void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	private void getSignupPage() {
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	private void getHomePage() {
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Home", driver.getTitle());
	}

	@Test
	@Order(1)
	public void testUnauthorizedUserAccess (){
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@Order(2)
	public void signUpThenLoginThenVerifyHomePageAccessTest (){

		//Sign Up and check message from result.
		doMockSignUp("user","one","user","123");

		//Login and check message from result.
		doLogIn("user","123");

		//Logout
		doLogout();

		//Check if home page is accessible after logout.
		testUnauthorizedUserAccess();
	}

	@Test
	@Order(3)
	public void createNoteThenVerifiesIfDisplayedTest (){

		// Login if needed.
		boolean isLoggedIn = driver.getCurrentUrl().equals("http://localhost:" + this.port + "/home");
		if (!isLoggedIn) {
			doLogIn("user", "123");
		}

		//Create note.
        String NOTE_TITLE = "Title";
        String NOTE_DESCRIPTION = "Description";

        createNote (NOTE_TITLE, NOTE_DESCRIPTION);

		getHomePage();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement tabButtonNotes = driver.findElement(By.id("nav-notes-tab"));
		tabButtonNotes.click();

		//Verifies if note displays.
		WebElement notesTable = driver.findElement(By.id("notesTable"));
		List<WebElement> notesList = notesTable.findElements(By.tagName("th"));
		boolean created = false;
        for (WebElement element : notesList) {
            if (element.getAttribute("innerHTML").equals(NOTE_TITLE)) {
                created = true;
                break;
            }
        }
		Assertions.assertTrue(created);

	}

	@Test
	@Order(4)
	public void editNoteThenVerifiesChangesDisplayedTest (){

		String NOTE_TITLE_EDITED = "EditedTitle";
		String NOTE_DESCRIPTION_EDITED = "EditedDescription";

		// Login if needed.
		boolean isLoggedIn = driver.getCurrentUrl().equals("http://localhost:" + this.port + "/home");
		if (!isLoggedIn) {
			doLogIn("user", "123");
		}

		//Create note.
		//createNote(NOTE_TITLE, NOTE_DESCRIPTION);
		//Edit note method.
		editNote(NOTE_TITLE_EDITED, NOTE_DESCRIPTION_EDITED);

		getHomePage();

		//Go to notes tab.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement tabButtonNotes = driver.findElement(By.id("nav-notes-tab"));
		tabButtonNotes.click();

		//Verifies if note displays.
		WebElement notesTable = driver.findElement(By.id("notesTable"));
		List<WebElement> notesList = notesTable.findElements(By.tagName("th"));
		boolean created = false;
		for (WebElement element : notesList) {
			if (element.getAttribute("innerHTML").equals("EditedTitle")) {
				created = true;
				break;
			}
		}
		Assertions.assertTrue(created);

	}

	@Test
	@Order(5)
	public void deleteNoteThenVerifiesIfNoLongerDisplayedTest () {

		//Login if needed.
		boolean isLoggedIn = driver.getCurrentUrl().equals("http://localhost:" + this.port + "/home");
		if(!isLoggedIn){
			doLogIn("user","123");
		}

		//Create note.
		//createNote(NOTE_TITLE, NOTE_DESCRIPTION);

		//Delete note method.
		deleteNote();

		//Go to notes tab.
		getHomePage();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement tabButtonNotes = driver.findElement(By.id("nav-notes-tab"));
		tabButtonNotes.click();

		//Check if note is deleted
		WebElement notesTable = driver.findElement(By.id("notes-table-body"));
		List<WebElement> notesList = notesTable.findElements(By.tagName("tr"));

		Assertions.assertEquals(0, notesList.size());

	}

	@Test
	@Order(6)
	public void createCredentialsThenVerifiesIfPasswordEncryptedThenVerifiesIfDisplayedTest(){

		//Login if needed.
		boolean isLoggedIn = driver.getCurrentUrl().equals("http://localhost:" + this.port + "/home");
		if(!isLoggedIn){
			doLogIn("user","123");
		}

		//Create credentials.
		createCredentials("https://www.google.com", "user","123");

		//Go to credentials tab.
		getHomePage();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement credTabButton = driver.findElement(By.id("nav-credentials-tab"));
		credTabButton.click();

		// Check if the credentials appears.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-table-body")));
		WebElement credentialsTable = driver.findElement(By.id("credential-table-body"));
		List<WebElement> credList = credentialsTable.findElements(By.tagName("tr"));

		Assertions.assertNotEquals(0, credList.size());

		// Check if the password is not equal to the original password (Encrypted).
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		Assertions.assertNotEquals("123", driver.findElement(By.id("table-cred-password")).getText());

	}

	@Test
	@Order(7)
	public void editCredentialsThenVerifiesPasswordEncryptedThenChangesDisplayedTest (){

		String editedUrl = "https://www.youtube.com";
		String editedUsername = "EditedUsername";
		String editedPassword = "EditedPassword";

		//Login if needed.
		boolean isLoggedIn = driver.getCurrentUrl().equals("http://localhost:" + this.port + "/home");
		if(!isLoggedIn){
			doLogIn("user","123");
		}

		//Create a credential.
		//createCredentials("https://www.google.com", "user","123");

		//Go to credentials tab.
//		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
//		WebElement credTab = driver.findElement(By.id("nav-credentials-tab"));
//		credTab.click();

		// Check if the password is not equal to the original password (Encrypted).
//		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
//		Assertions.assertNotEquals("123", driver.findElement(By.id("table-cred-password")).getText());

		//Edit credential
		editCredentials(editedUrl,editedUsername,editedPassword);

		//Go to credentials tab.
		getHomePage();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement credTab = driver.findElement(By.id("nav-credentials-tab"));
		credTab.click();

		// Check if the url,  username and password are not equal to the original ones.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		Assertions.assertEquals(editedUrl, driver.findElement(By.id("table-credentialUrl")).getText());
		Assertions.assertEquals(editedUsername, driver.findElement(By.id("table-cred-username")).getText());
		Assertions.assertNotEquals(editedPassword, driver.findElement(By.id("table-cred-password")).getText());
	}

	@Test
	@Order(8)
	public void deleteCredentialsThenVerifiesIfNoLongerDisplayedTest () {
		//Login if needed.
		boolean isLoggedIn = driver.getCurrentUrl().equals("http://localhost:" + this.port + "/home");
		if(!isLoggedIn){
			doLogIn("user","123");
		}

		//Create credentials.
		//createCredentials("https://www.google.com", "user","123");

		//delete credentials.
		deleteCredentials();

		//Go to credentials tab.
		getHomePage();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement credTabButton = driver.findElement(By.id("nav-credentials-tab"));
		credTabButton.click();

		//Check if credential is deleted.
		WebElement credentialsTable = driver.findElement(By.id("credential-table-body"));
		List<WebElement> credList = credentialsTable.findElements(By.tagName("tr"));

		Assertions.assertEquals(0, credList.size());

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling redirecting users
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric:
	 * https://review.udacity.com/#!/rubrics/2724/view
	 */
	@Test
	@Order(9)
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");

		// Check if we have been redirected to the login page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling bad URLs
	 * gracefully, for example with a custom error page.
	 *
	 * Read more about custom error pages at:
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	@Order(10)
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");

		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code.
	 *
	 * Read more about file size limits here:
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	@Order(11)
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("successMessage")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){

		// Visit the sign-up page.
		getSignupPage();

		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

	}

	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String username, String password)
	{
		// Log in to our dummy account.
		getLoginPage();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(username);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		//Verify if the login was successful.
		boolean loginError = false;
		try {
			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error-msg")));
			loginError = true;
		} catch (TimeoutException e) {
			e.getMessage();
		}

		if (loginError) {
			doMockSignUp("user","one","user","123");
		}

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	private void doLogout (){

		Assertions.assertEquals("Home",driver.getTitle());

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout-button")));
		WebElement logoutButton = driver.findElement(By.id("logout-button"));
		logoutButton.click();
	}

	private void createNote (String title, String description){

		//Click Notes Tab.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement tabButtonNotes = driver.findElement(By.id("nav-notes-tab"));
		tabButtonNotes.click();

		//Click create Note.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addNewNoteButton")));
		WebElement notesButton = driver.findElement(By.id("addNewNoteButton"));
		notesButton.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteModal")));

		//Create Note.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement inputNoteTitle = driver.findElement(By.id("note-title"));
		inputNoteTitle.click();
		inputNoteTitle.sendKeys(title);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement inputNoteDescription = driver.findElement(By.id("note-description"));
		inputNoteDescription.click();
		inputNoteDescription.sendKeys(description);

		WebElement saveNoteButton = driver.findElement(By.id("saveNote"));
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("saveNote")));
		saveNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-message")));
		Assertions.assertTrue(driver.findElement(By.id("success-message")).getText().contains("New note added!"));


	}

	private void editNote(String editedTitle, String editedDescription) {

		// Navigate to the Home Page.
		getHomePage();

		// Click on the Notes tab.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement tabButtonNotes = driver.findElement(By.id("nav-notes-tab"));
		tabButtonNotes.click();

		// Click the "Edit" button.
		WebElement editButton = driver.findElement(By.id("editNoteButton"));
		webDriverWait.until(ExpectedConditions.visibilityOf(editButton));
		webDriverWait.until(ExpectedConditions.elementToBeClickable(editButton));
		editButton.click();

		// Wait for the modal to open and edit the fields.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteModal")));

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement inputNoteTitle = driver.findElement(By.id("note-title"));
		inputNoteTitle.click();
		inputNoteTitle.clear();
		inputNoteTitle.sendKeys(editedTitle);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement inputNoteDescription = driver.findElement(By.id("note-description"));
		inputNoteDescription.click();
		inputNoteDescription.clear();
		inputNoteDescription.sendKeys(editedDescription);

		// Save the changes.
		WebElement saveNoteButton = driver.findElement(By.id("saveNote"));
		webDriverWait.until(ExpectedConditions.elementToBeClickable(saveNoteButton));
		saveNoteButton.click();

		// Verify if the edit was successful.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-message")));
		Assertions.assertTrue(driver.findElement(By.id("success-message")).getText().contains("New note added!"));

	}

	private void deleteNote (){

		// Navigate to the Home Page.
		getHomePage();

		// Click on the Notes tab.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement tabButtonNotes = driver.findElement(By.id("nav-notes-tab"));
		tabButtonNotes.click();

		//Check if note list is not empty then delete note.
		WebElement notesTable = driver.findElement(By.id("notes-table-body"));
		List<WebElement> notesList = notesTable.findElements(By.tagName("tr"));

		if(!notesList.isEmpty()) {
			WebElement deleteButton = driver.findElement(By.id("deleteNoteButton"));
			webDriverWait.until(ExpectedConditions.visibilityOf(deleteButton));
			webDriverWait.until(ExpectedConditions.elementToBeClickable(deleteButton));
			deleteButton.click();

			// Verify if delete successfully.
			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-message")));
			Assertions.assertTrue(driver.findElement(By.id("success-message")).getText().contains("Note deleted!"));
		}

	}

	private void createCredentials(String url, String userName, String password){

		//Go to credentials tab.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement tabButtonNotes = driver.findElement(By.id("nav-credentials-tab"));
		tabButtonNotes.click();

		//Press Add credentials button.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addNewCredentialButton")));
		WebElement addCredentialsButton= driver.findElement(By.id("addNewCredentialButton"));
		addCredentialsButton.click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));

		// Fill out the credentials.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement inputURL = driver.findElement(By.id("credential-url"));
		inputURL.click();
		inputURL.sendKeys(url);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement inputUsername = driver.findElement(By.id("credential-username"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement inputPassword = driver.findElement(By.id("credential-password"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Save credential.
		WebElement submitNote = driver.findElement(By.id("saveCredential"));
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("saveCredential")));
		submitNote.click();

		//Check if success message pop up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-message")));
		Assertions.assertTrue(driver.findElement(By.id("success-message")).getText().contains("New credential added!"));

	}

	private void editCredentials (String editedUrl, String editedUserName, String editedPassword){

		//Go to credentials tab.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement tabButtonNotes = driver.findElement(By.id("nav-credentials-tab"));
		tabButtonNotes.click();

		// Click the "Edit" button.
		WebElement editButton = driver.findElement(By.id("editCredentialButton"));
		webDriverWait.until(ExpectedConditions.visibilityOf(editButton));
		webDriverWait.until(ExpectedConditions.elementToBeClickable(editButton));
		editButton.click();

		// Wait for the modal to open and edit the fields.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement inputCredentialsUrl = driver.findElement(By.id("credential-url"));
		inputCredentialsUrl.click();
		inputCredentialsUrl.clear();
		inputCredentialsUrl.sendKeys(editedUrl);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement inputCredentialsUsername = driver.findElement(By.id("credential-username"));
		inputCredentialsUsername.click();
		inputCredentialsUsername.clear();
		inputCredentialsUsername.sendKeys(editedUserName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement inputCredentialsPassword = driver.findElement(By.id("credential-password"));
		inputCredentialsPassword.click();
		inputCredentialsPassword.clear();
		inputCredentialsPassword.sendKeys(editedPassword);

		// Save the changes.
		WebElement saveCredentialButton = driver.findElement(By.id("saveCredential"));
		webDriverWait.until(ExpectedConditions.elementToBeClickable(saveCredentialButton));
		saveCredentialButton.click();

		// Verify if the edit was successful.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-message")));
		Assertions.assertTrue(driver.findElement(By.id("success-message")).getText().contains("New credential added!"));

	}

	private void deleteCredentials (){

		//Go to credentials tab.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement credTabButton = driver.findElement(By.id("nav-credentials-tab"));
		credTabButton.click();

		//Check if note list is not empty then delete credential.
		WebElement credentialTable = driver.findElement(By.id("credential-table-body"));
		List<WebElement> credList = credentialTable.findElements(By.tagName("tr"));

		if(!credList.isEmpty()) {
			WebElement deleteButton = driver.findElement(By.id("deleteCredentialButton"));
			webDriverWait.until(ExpectedConditions.visibilityOf(deleteButton));
			webDriverWait.until(ExpectedConditions.elementToBeClickable(deleteButton));
			deleteButton.click();

			// Verify if delete successfully.
			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-message")));
			Assertions.assertTrue(driver.findElement(By.id("success-message")).getText().contains("Credential deleted!"));
		}
	}

}
