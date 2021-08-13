package com.example.source_code_fetcher_ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LauncherController {
    @FXML
    private Label welcomeText;
    @FXML
    private Label windowsText;
    @FXML
    private Label urlText;
    @FXML
    private Label filePathText;

    @FXML
    private TextField windowsTextField;
    @FXML
    private TextField urlTextField;
    @FXML
    private TextField filePathTextField;

    @FXML
    private Button launchButton;
    @FXML
    private Button stopButton;

    boolean programIsShutDown;

    public LauncherController() {
        this.programIsShutDown = false;
    }

    class MyRunnable implements Runnable {
        int numOfWindows;
        String url;
        String filePath;

        public MyRunnable(int numOfWindows, String url, String filePath) {
            this.numOfWindows = numOfWindows;
            this.url = url;
            this.filePath = filePath;
        }

        @Override
        public void run() {
            openBrowser(numOfWindows, url, filePath);
        }
    }

    @FXML
    protected void onLaunchButtonClick() {
        int numOfWindows = Integer.parseInt(windowsTextField.getText());
        String url = urlTextField.getText();
        String filePath = filePathTextField.getText();
        prepareShutDownState();
        openBrowserInASeparateThread(numOfWindows, url, filePath);
    }

    private void prepareShutDownState() {
        launchButton.setVisible(false);

        windowsTextField.setVisible(false);
        windowsText.setVisible(false);

        urlTextField.setVisible(false);
        urlText.setVisible(false);

        filePathTextField.setVisible(false);
        filePathText.setVisible(false);

        welcomeText.setText("Press 'stop' button to terminate the program!");

        stopButton.setVisible(true);
    }

    private void openBrowserInASeparateThread(int numOfWindows, String url, String filePath) {
        MyRunnable myRunnable = new MyRunnable(numOfWindows, url, filePath);
        Thread myThread = new Thread(myRunnable);
        myThread.setDaemon(true);
        myThread.start();
    }

    private void openBrowser(int numOfWindows, String url, String filePath) {
        System.setProperty("webdriver.gecko.driver", "/usr/local/bin/geckodriver");

        List<CustomWebDriver> customWebDrivers = new ArrayList<>();
        populateWebDriversCollection(customWebDrivers, numOfWindows, url, filePath);

        customWebDrivers.parallelStream().forEach(customWebDriver -> {
            WebDriver webDriver = customWebDriver.getWebDriver();
            String u = customWebDriver.getUrl();
            String fileName = customWebDriver.getFilename();
            webDriver.get(u);
            try {
                while(!programIsShutDown) {
                    try (FileWriter fileWriter = new FileWriter(fileName, true)) {
                        TimeUnit.SECONDS.sleep(10);
                        String pageSource = webDriver.getPageSource();
                        fileWriter.write(pageSource);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                webDriver.quit();
            }
        });
    }

    private void populateWebDriversCollection(List<CustomWebDriver> customWebDrivers,
                                              int numOfWindows,
                                              String url,
                                              String filePath) {
        for (int i = 0; i < numOfWindows; i++) {
            WebDriver webDriver = new FirefoxDriver();
            String fileName = filePath.concat(String.format("/file%s.txt", i + 1));
            customWebDrivers.add(new CustomWebDriver(webDriver, url, fileName));
        }
    }

    public void onStopButtonClick() throws InterruptedException {
        programIsShutDown = true;
        TimeUnit.SECONDS.sleep(10);
        Stage stage = (Stage) stopButton.getScene().getWindow();
        stage.close();
        System.exit(0);
    }
}