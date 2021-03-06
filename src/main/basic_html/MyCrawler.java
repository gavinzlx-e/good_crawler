package main.basic_html;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {
	private static Logger logger=LoggerFactory.getLogger(MyCrawler.class);

	private OutputStreamWriter writer=null;
	private String path="/data/crawl/root/";
	private String postfixString=".html";

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif"
			+ "|png|mp3|mp3|zip|gz))$");

	/**
	 * This method receives two parameters. The first parameter is the page
	 * in which we have discovered this new url and the second parameter is
	 * the new url. You should implement this function to specify whether
	 * the given url should be crawled or not (based on your crawling logic).
	 * In this example, we are instructing the crawler to ignore urls that
	 * have css, js, git, ... extensions and to only accept urls that start
	 * with "http://www.ics.uci.edu/". In this case, we didn't need the
	 * referringPage parameter to make the decision.
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches()
				&& 
				(href.startsWith("http://www.tutorialspoint.com/db2")
				||href.startsWith("http://www.tutorialspoint.com/maven/")
				||	href.startsWith("http://www.tutorialspoint.com/db2")
				||	href.startsWith("http://www.tutorialspoint.com/db2"));
	}

	/**
	 * This function is called when a page is fetched and ready
	 * to be processed by your program.
	 */
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		System.out.println("URL: " + url);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();

			String titleString=htmlParseData.getTitle();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();

			Set<WebURL> links = htmlParseData.getOutgoingUrls();
			logger.info( "titile: " + titleString);
			logger.info("Text length: " + text.length());
			logger.info("Text length: " + text.length());
			logger.info("Html length: " + html.length());
			logger.info("Number of outgoing links: " + links.size());
			try {
				writer=new OutputStreamWriter(new FileOutputStream(path+titleString+postfixString));
				writer.write(html);
				writer.flush();
				writer.close();

			} catch (FileNotFoundException e) {
				logger.error("error"+e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
