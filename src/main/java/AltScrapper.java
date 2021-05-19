
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;


public class AltScrapper {
	
	private String dirPath = "/media/Stock/Projets/Suratram/Ressources/Traces_WS/GoogleEarth/ligne1/bus9/kml/segments_7-8/finalFiles/";
	private String outputDirPath = "/home/promet/Bureau/AltScrapper_Result/";
	
	private Boolean DEBUG_HtmlUnit = false;
	private Boolean DEBUG_fileDown = true;
	private int notDDOSLimit = 1000;
	
	
	public static void main(String[] args) throws Exception {
		/***
		 * AltScrapper Core
		 */
		
		AltScrapper altScrapper = new AltScrapper();
		
		altScrapper.multiFiles_addElev(altScrapper.dirPath, altScrapper.outputDirPath);
		
	}
	
	public void multiFiles_addElev(String dirPath, String outputDirPath_p) throws Exception{
		/***
		 * Get files from given directory: "dirPath"
		 * and process them to add elevation
		 */
		
		File dirPath_object = new File(dirPath);
		
		String Names[] = dirPath_object.list();
		
		for (String elm : Names) {
			if(elm.contains(".kml")) {
				
				if(DEBUG_HtmlUnit == true) {
					System.out.println(elm);
				}
				
				elevationAdder(dirPath, elm, outputDirPath_p);
			}
			
			Thread.sleep(notDDOSLimit);
		}
	}
	
	private void elevationAdder(String dirPath_p, String inputFile_p, String OutputDir_p) throws Exception{
		/***
		 * Allow to process given file to add altitude data
		 * and store them in given output directory
		 */
		WebClient webClient = new WebClient();
		webClient.getOptions().setUseInsecureSSL(true);
	    webClient.getOptions().setCssEnabled(false);
	    webClient.getOptions().setJavaScriptEnabled(false);	    

	    //fetching the web page
	    String url = "https://www.gpsvisualizer.com/map_input?form=googleearth";
	    HtmlPage page = webClient.getPage(url);
	    
	    if(DEBUG_HtmlUnit == true) {
	    	System.out.println(page.getUrl());	    
	    	System.out.println(page.getTitleText());
	    }	    
	    
	    //select set .kml file
	    HtmlSelect selectFileType = (HtmlSelect) page.getElementByName("googleearth_zip");
	    
	    if(DEBUG_HtmlUnit == true) {
	    	System.out.println(selectFileType.getOption(0).asNormalizedText());
	    }
	    
	    //set file extension as ".kml"
	    HtmlOption kmlFile = selectFileType.getOptionByText(".kml (uncompressed)");
	    selectFileType.setSelectedAttribute(kmlFile, true);
	    
	    if(DEBUG_HtmlUnit == true) {
	    	System.out.println(kmlFile.asNormalizedText());
	    }
	    
	    //add elevation on file
	    HtmlSelect selectelevation = (HtmlSelect) page.getElementByName("add_elevation");
	    
	    if(DEBUG_HtmlUnit == true) {
	    	System.out.println(selectelevation.getOption(4).asNormalizedText());
	    }

	    //chose the gps geographic area
	    HtmlOption europeSRTM1 = selectelevation.getOptionByText("NASA SRTM1 (30m res., NoAm, Europe, more)");
	    selectelevation.setSelectedAttribute(europeSRTM1, true);
	    
	    if(DEBUG_HtmlUnit == true) {
	    	System.out.println(europeSRTM1.asNormalizedText());
	    }
	    
	    //add file
	    HtmlForm myForm = page.getFormByName("main");
	    HtmlFileInput fileInput = myForm.getInputByName("uploaded_file_1");
	    fileInput.setFiles(new File(dirPath_p + inputFile_p));
	    
	    //click button to submit the file
	    HtmlElement submitBtn = page.getElementByName("submitted");
	    page = submitBtn.click(); //and arrive on the redirected downloading page
	    
	    if(DEBUG_HtmlUnit == true) {
	    	System.out.println(page.getUrl());
	    }
	    
	    
	    // search for the download anchor
        for (final HtmlAnchor anchor : page.getAnchors()) {
            // support kmk / kmz / kml file
            if (anchor.asNormalizedText().contains("-map.km")) {
            	if(DEBUG_HtmlUnit == true) {
            		System.out.println("wanted anchor find");
        	    }            	
                Page page3 = anchor.click();
                
                try (InputStream is = page3.getWebResponse().getContentAsStream()) {
                    try (OutputStream os = new FileOutputStream(new File(OutputDir_p + inputFile_p))) {
                        IOUtils.copy(is, os);
                        if(DEBUG_fileDown == true) {
                        	System.out.println("file: " + inputFile_p + " copy succedded");
                	    }
                    }
                }
            }
        }
        
        webClient.close();
	}
		

	
	public void file_addElev() throws Exception{
		/***
		 * temp. Dev. and debug function
		 * Allow to process a given file to add altitude data
		 */
		WebClient webClient = new WebClient();
		webClient.getOptions().setUseInsecureSSL(true);
	    webClient.getOptions().setCssEnabled(false);
	    webClient.getOptions().setJavaScriptEnabled(false);	    

	    //fetching the web page
	    String url = "https://www.gpsvisualizer.com/map_input?form=googleearth";
	    HtmlPage page = webClient.getPage(url);
	    
	    System.out.println(page.getUrl());
	    
	    System.out.println(page.getTitleText());
	    
	    //select set .kml file
	    HtmlSelect selectFileType = (HtmlSelect) page.getElementByName("googleearth_zip");
	    System.out.println(selectFileType.getOption(0).asNormalizedText());
	    
	    //set file extension as ".kml"
	    HtmlOption kmlFile = selectFileType.getOptionByText(".kml (uncompressed)");
	    System.out.println(kmlFile.asNormalizedText());
	    selectFileType.setSelectedAttribute(kmlFile, true);
	    
	    //add elevation on file
	    HtmlSelect selectelevation = (HtmlSelect) page.getElementByName("add_elevation");
	    System.out.println(selectelevation.getOption(4).asNormalizedText());
	    
	    //chose the gps geographic area
	    HtmlOption europeSRTM1 = selectelevation.getOptionByText("NASA SRTM1 (30m res., NoAm, Europe, more)");
	    System.out.println(europeSRTM1.asNormalizedText());
	    selectelevation.setSelectedAttribute(europeSRTM1, true);
	    
	    //add file
	    HtmlForm myForm = page.getFormByName("main");
	    HtmlFileInput fileInput = myForm.getInputByName("uploaded_file_1");
	    fileInput.setFiles(new File("/media/Stock/Projets/Suratram/Ressources/Traces_WS/GoogleEarth/ligne1/bus9/kml/segments_7-8/finalFiles/1_LaDouce-Signoret.kml"));
	    
	    //click button to submit the file
	    HtmlElement submitBtn = page.getElementByName("submitted");
	    page = submitBtn.click(); //and arrive on the redirected downloading page
	    System.out.println(page.getUrl());
	    
	    // search for the download anchor
        for (final HtmlAnchor anchor : page.getAnchors()) {
            // support kmk / kmz / kml file
            if (anchor.asNormalizedText().contains("-map.km")) {
            	System.out.println("wanted anchor find");
                Page page3 = anchor.click();
                
                try (InputStream is = page3.getWebResponse().getContentAsStream()) {
                    try (OutputStream os = new FileOutputStream(new File("/home/promet/Bureau/AltScrapper_Result/test.kml"))) {
                        IOUtils.copy(is, os);
                        System.out.println("file copy succedded");
                    }
                }
            }
        }
        
        webClient.close();
	}
	
}