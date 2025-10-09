package Locators;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

    public class LoginPageLocators {
	//private By microsoftLoginBtn = By.xpath("//button[contains(text(),'Microsoft')]");
	
	private By microsoftLoginBtn =By.cssSelector("img.ms-2");
	
	//private By otherTileText =By.id("otherTileText");
	
	private By i0116 =By.id("i0116");
	private By useAnotherAccountOption = By.xpath("//div[@id='otherTileText']");
    private By emailField = By.xpath("//input[@type='email']");
    private By passwordField = By.xpath("//input[@type='password']");
    //private By Confirmbutton=By.xpath("//input[@id='idSIButton9']");
    private By Confirmbutton = By.xpath("//input[@type='submit']");
    //private By Confirmbutton=By.xpath("//input[@class='win-button button_primary high-contrast-overrides button ext-button primary ext-primary']");
    
  
    
    //private By inputUserName = By.xpath("//input[@placeholder='Enter your username']");
    
    private By inputUserName = By.xpath("//input[@placeholder=' Enter your username' and contains(@name,'not-uname')]");

    
	/*
	 * private By inputPassword = By.cssSelector(
	 * "input[type='password'], input[name='password'], input#password, input[placeholder*='password' i]"
	 * );
	 */
    private By inputPassword = By.xpath(
        "//input[@name='not-pswrd' and (normalize-space(@placeholder)='Enter your password' or contains(translate(@placeholder,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'enter your password'))]"
    );
   
           // private By inputPassword = By.xpath("//input[@type='password']");
    
  
    private By buttonSignIn = By.xpath("//button[contains(@class,'custom-signin-button') and normalize-space(.//span)='Sign In']");
    private By linkChangePassword = By.xpath("//a[normalize-space(text())='Change Password']");
    private By linkForgotPassword = By.xpath("//a[normalize-space(text())='Forgot Password?']");

   // private   By inputPassword = By.xpath("//input[@placeholder=' Enter your Password']");
    
    
    
    
    public By getMicrosoftLoginBtn() { return microsoftLoginBtn; }
    //public By getotherTileText() { return otherTileText; }
    public By geti0116() { return i0116; }
    public By useAnotherAccountOption() { return useAnotherAccountOption; }
    public By getEmailField() { return emailField; }
    //public By getSignInButton() { return SignInButton; }
    public By getPasswordField() { return passwordField; }
    public By getConfirmbutton() { return Confirmbutton; }
    
    public By getinputUserName() { return inputUserName; }
    public By getinputPassword() { return inputPassword; }
    public By getbuttonSignIn() { return buttonSignIn; }
    public By getlinkChangePassword() { return linkChangePassword; }
    public By getlinkForgotPassword() { return linkForgotPassword; }
    
  
    
    
}
