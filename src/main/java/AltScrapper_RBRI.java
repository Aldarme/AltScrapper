
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.html.parser.HTMLParser;




public class AltScrapper_RBRI {
	
	public static void main(String[] args) throws Exception {
		
		WebClient webClient = new WebClient();
		webClient.getOptions().setUseInsecureSSL(true);
	    webClient.getOptions().setCssEnabled(false);
	    webClient.getOptions().setJavaScriptEnabled(false);	    

	    //fetching the web page
	    String url = "https://www.gpsvisualizer.com/map_input?form=googleearth";
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
	    //fileInput.setValueAttribute("/media/Stock/Projets/Suratram/Ressources/Traces_WS/GoogleEarth/ligne1/bus9/kml/segments_7-8/finalFiles/1_LaDouce-Signoret.kml");
	    fileInput.setFiles(new File("/media/Stock/Projets/Suratram/Ressources/Traces_WS/GoogleEarth/ligne1/bus9/kml/segments_7-8/finalFiles/1_LaDouce-Signoret.kml"));
	    HtmlElement submitBtn = page.getElementByName("submitted");
	    
	    //Arrive on the redirected page, where to download the file
	    page = submitBtn.click();
	    System.out.println(page.getUrl());
	    
	    System.out.println(page.getAnchors());
	    
	    // search for the download anchor
        for (final HtmlAnchor anchor : page.getAnchors()) {
            // support kmk / kmz
            if (anchor.asNormalizedText().contains("-map.km")) {
            	System.out.println("GotIt");
                Page page3 = anchor.click();
                
                try (InputStream is = page3.getWebResponse().getContentAsStream()) {
                    try (OutputStream os = new FileOutputStream(new File("/home/promet/Bureau/AltScrapper_Result/test.kml"))) {
                        IOUtils.copy(is, os);
                        System.out.println("File download success");
                    }
                }
            }
        }
	    
	    
	    /*
	    WebWindow window = page.getEnclosingWindow();
	    
	    //System.out.println( page.asNormalizedText() );
	    
	    // click the anchor that forces the file download
	    for(HtmlAnchor elm : page.getAnchors()) {
	    	//System.out.println(elm);
	    	
	    	if(elm.asNormalizedText().contains("-map.kml")) {
	    		System.out.println(elm);
	    		elm.click();
	    	}	    	
	    }
	    
	    UnexpectedPage downloadPage = (UnexpectedPage) window.getEnclosedPage();
	    
	    InputStream downloadedContent = downloadPage.getInputStream();
    	
    	File file = new File("/home/promet/Bureau/test/test.kml");
    	
    	OutputStream output = new FileOutputStream(file, false);
    	
    	downloadedContent.transferTo(output);
		
		*/
		
	}
}
