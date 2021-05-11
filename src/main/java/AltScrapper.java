
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;

public class AltScrapper {

	public static void main(String[] args) throws Exception {
		
		WebClient webClient = new WebClient();
	    webClient.getOptions().setCssEnabled(false);
	    webClient.getOptions().setJavaScriptEnabled(true);
	    webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
	    webClient.getOptions().setThrowExceptionOnScriptError(false);
	    webClient.getOptions().setRedirectEnabled(true);
	    
	    

	    //fetching the web page
	    String url = "https://www.gpsvisualizer.com/map_input?form=googleearth";
	    //String url = "https://www.reddit.com/r/scraping/";
	    HtmlPage page = webClient.getPage(url);
	    
	    System.out.println(page.getUrl());
	    
	    System.out.println(page.getTitleText());
	    
	    //Select set .kml file
	    HtmlSelect selectFileType = (HtmlSelect) page.getElementByName("googleearth_zip");
	    System.out.println(selectFileType.getOption(0).asNormalizedText());
	    //System.out.println(selectFileType.getOption(1).asText());
	    
	    HtmlOption kmlFile = selectFileType.getOptionByText(".kml (uncompressed)");
	    System.out.println(kmlFile.asNormalizedText());
	    selectFileType.setSelectedAttribute(kmlFile, true);
	    
	    //Select add elevation on file
	    HtmlSelect selectelevation = (HtmlSelect) page.getElementByName("add_elevation");
	    System.out.println(selectelevation.getOption(4).asNormalizedText());
	    
	    HtmlOption europeSRTM1 = selectelevation.getOptionByText("NASA SRTM1 (30m res., NoAm, Europe, more)");
	    System.out.println(europeSRTM1.asNormalizedText());
	    selectelevation.setSelectedAttribute(europeSRTM1, true);
	    
	    //add file
	    HtmlForm myForm = page.getFormByName("main");
	    HtmlFileInput fileInput = myForm.getInputByName("uploaded_file_1");
	    fileInput.setValueAttribute("/media/Stock/Projets/Suratram/Ressources/Traces_WS/puissance/kml_files/01_douce-signoret.kml");
	    HtmlElement submitBtn = page.getElementByName("submitted");
	    
	    //page google
	    HtmlPage page2 = submitBtn.click();
	    System.out.println(page2.getUrl());

	}

}
