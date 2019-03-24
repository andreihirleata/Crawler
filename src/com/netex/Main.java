package com.netex;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;

/**
 * Make sure your path to the chromedriver executable is SET properly before running!!!
 * This crawler works by iterating through the dimensions, checks if there are any search results,
 * then opens a new window for each dimensions, gets and writes the elements to the CSV file;
 * When the first Chrome window opens, just sit back and let it do it's thing.
 * Crawling the entire website takes about 2 hours! @17000 tyres
 *
 */
public class Main {

   public static WebDriver driver;
   public static String baseUrl = "http://www.reifen.com";

    public static void main(String[] args) throws IOException {

        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get(baseUrl);
        Crawler crawler = new Crawler(driver);
        crawler.crawl();


    }

}
