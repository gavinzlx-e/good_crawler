package main.image;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * @author Yasser Ganjisaffar
 */

/*
 * This class shows how you can crawl images on the web and store them in a
 * folder. This is just for demonstration purposes and doesn't scale for large
 * number of images. For crawling millions of images you would need to store
 * downloaded images in a hierarchy of folders
 */
public class ImageCrawler extends WebCrawler {
	private static final Logger logger = LoggerFactory.getLogger(ImageCrawler.class);

	private static final Pattern filters = Pattern
			.compile(".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	private static final Pattern imgPatterns = Pattern.compile(".*(\\.(bmp|png|jpe?g?))$");

	private static File storageFolder;
	private static String[] crawlDomains;

	public static void configure(String[] domain, String storageFolderName) {
		crawlDomains = domain;

		storageFolder = new File(storageFolderName);
		if (!storageFolder.exists()) {
			storageFolder.mkdirs();
		}
	}

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {

		String href = url.getURL().toLowerCase();

		if (filters.matcher(href).matches()) {
			//如果是资源文件。如mp3.js等。则不要访问
			logger.info("new page to check  :false:"+url.getURL());
			return false;
		}

		if (imgPatterns.matcher(href).matches()) {
			logger.info("new page to check  :true:"+url.getURL());
			//图片文件，目标文件，则访问
			return true;
		}


		//在我们域名里的链接。需要访问
		if (href.indexOf("hegongchang")>0  ) {
			logger.info("new page to check  :true:"+url.getURL());
			return true;
		}

		logger.info("new page to check  :false:"+url.getURL());
		return false;
	}

	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();

		//图片： 且大于 10k 图片才要。
		if (!imgPatterns.matcher(url).matches() ||
				!((page.getParseData() instanceof BinaryParseData)
						|| (page.getContentData().length < (10 * 1024)))) {
			return;
		}

		// get a unique name for storing this image
		String extension = url.substring(url.lastIndexOf('.'));
		String hashedName = UUID.randomUUID()+"";

		// store image    uuid
		//		String filename = storageFolder.getAbsolutePath() + "/" + hashedName+htmlParseData.getTitle()+ extension;
		//	 rul  http://pic.qjimage.com/design_rf004/thu/designrf1831230.jpg
		String fileString=url.replaceAll("\\/", "#");
		String filename = storageFolder.getAbsolutePath() + "/" +fileString;
		try {
			Files.write(page.getContentData(), new File(filename));
			logger.info("Stored: ", url);
		} catch (IOException iox) {
			logger.error("Failed to write file: " + filename, iox);
		}
	}

	@Test
	public void filenametest(){
		String url ="http://pic.qjimage.com/design_rf004/thu/designrf1831230.jpg";
		String fileString=url.replaceAll("\\/", "#");
		String filename =  "/" +fileString;
		logger.info("path: "+filename);
	}
}