package com.example.source_code_fetcher_ui;

import org.openqa.selenium.WebDriver;

public class CustomWebDriver {
    WebDriver webDriver;
    String url;
    String filename;

    public CustomWebDriver(WebDriver webDriver, String url, String filename) {
        this.webDriver = webDriver;
        this.url = url;
        this.filename = filename;
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public String getUrl() {
        return url;
    }

    public String getFilename() {
        return filename;
    }
}
