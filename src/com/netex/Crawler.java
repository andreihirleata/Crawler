package com.netex;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Crawler {

    private WebDriver driver;

    private List<WebElement> widths;
    private List<WebElement> crossSections;
    private List<WebElement> diameters;
    File file = new File("Tyres.csv");
    FileWriter writer = new FileWriter(file);

    public Crawler(WebDriver driver) throws IOException {
        this.driver = driver;
    }

    public void crawl(){
        Select selectWidth = new Select(driver.findElement(By.id("drpTyreWidth")));
        Select selectCrossSection = new Select(driver.findElement(By.id("drpTyreCrossSection")));
        Select selectDiameter = new Select(driver.findElement(By.id("drpTyreDiameter")));

        widths = new Select(driver.findElement(By.id("drpTyreWidth"))).getOptions();
         for (int i = 0; i < widths.size(); i++){
           selectWidth.selectByIndex(i);
           waitForIndex();
           if(noSearchResults())
               continue;
           crossSections = new Select(driver.findElement(By.id("drpTyreCrossSection"))).getOptions();
           for (int j = 0; j < crossSections.size(); j++){
               selectCrossSection.selectByIndex(j);
               waitForIndex();
               if(noSearchResults())
                   continue;
               diameters = new Select(driver.findElement(By.id("drpTyreDiameter"))).getOptions();
               for (int k = 0; k < diameters.size(); k++){
                   selectDiameter.selectByIndex(k);
                   waitForIndex();
                   if(noSearchResults())
                       continue;
                   searchDimension(widths.get(i).getText(), crossSections.get(j).getText(), diameters.get(k).getText());

               }
           }
       }
    }

    public void searchDimension(String width, String crossSection, String diameter){
        WebDriver search = new ChromeDriver();
        String searchUrl = "http://www.reifen.com/de/TyreSize/List/CarSummer/" + width + "-" + crossSection + "-" + diameter + "?TyreListSort=&Page=1&PageSize=99999";
        search.get(searchUrl);
        List<WebElement> brand = search.findElements(By.className("secondHeaderStyle"));
        List<WebElement> dimensions = search.findElements(By.cssSelector("p[class^='thirdHeaderStyle']"));
        // For some reason, the description of some tyres has a different class/xpath than others,
        // So, I used a partial cssSelector, then had to remove all the empty ones.
        for (int x = 0; x < dimensions.size(); x++){
            if (dimensions.get(x).getText().equals("")){
                dimensions.remove(x);
            }
        }
        List<WebElement> price = search.findElements(By.className("price"));




        try {

            for (int i = 0; i < brand.size(); i++) {
                writeToFile(writer, width, crossSection, diameter, brand.get(i).getText(),dimensions.get(i).getText(), price.get(i).getText());
            }
        }catch(IndexOutOfBoundsException|IOException e){
            e.printStackTrace();
        }
        search.close();

    }

    public void waitForIndex(){
        try{
            Thread.sleep(1500);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean noSearchResults(){
        boolean bool = driver.findElement(By.xpath("//*[@id=\"tyreCountContainer\"]/span")).getText().equals("0");
        return bool;
    }

    public void writeToFile(FileWriter w, String width, String crossSection, String diameter, String brand, String desc, String price) throws IOException{


        writer.write(width + ";" + crossSection + ";" + diameter + ";" + brand + ";" + desc + ";" + price + "\n");
        writer.flush();


    }
}

