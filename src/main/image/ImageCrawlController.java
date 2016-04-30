package main.image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * @author Yasser Ganjisaffar
 */
public class ImageCrawlController {

	private static final Logger logger = LoggerFactory.getLogger(ImageCrawlController.class);

	static String rootFolder =  "/data/crawl/root";
	static int numberOfCrawlers = 7;
	static String storageFolder = "/data/crawl/image";
//	static String[] crawlDomains = {"http://www.quanjing.com/","http://image.quanjing.com/"};

	static String[] crawlDomains = {"http://1024.hegongchang.red/pw/"};
	
	
	public static void main(String[] args) throws Exception {
		
		if (args.length < 3) {
			logger.info("Needed parameters: ");
			logger.info("\t rootFolder (it will contain intermediate crawl data)");
			logger.info("\t numberOfCralwers (number of concurrent threads)");
			logger.info("\t storageFolder (a folder for storing downloaded images)");
		}else{

			rootFolder = args[0];
			numberOfCrawlers = Integer.parseInt(args[1]);
			storageFolder = args[2];
		}


		CrawlConfig config = new CrawlConfig();

//		config.setMaxPagesToFetch(1000);
		config.setCrawlStorageFolder(rootFolder);
		config.setResumableCrawling(true); //接着上次的搜寻

		/*
		 * Since images are binary content, we need to set this parameter to
		 * true to make sure they are included in the crawl.
		 */
		config.setIncludeBinaryContentInCrawling(true);

		
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
		for (String domain : crawlDomains) {
			controller.addSeed(domain);
		}

		ImageCrawler.configure(crawlDomains, storageFolder);

		logger.info("start to crawle....");
		controller.start(ImageCrawler.class, numberOfCrawlers);
		
	}
}