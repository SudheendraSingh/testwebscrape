package com.sainsbury.scrape.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sainsbury.scrape.model.RipeFruit;

@Service
public class FruitScrapeServiceImpl implements FruitScrapeService {

	private static Logger logger = LogManager.getLogger();

	@Override
	public List<RipeFruit> getRipeFruitsAfterScraping(String urlToScrape) {
		WebClient webClient = getWebClient();
		List<RipeFruit> ripeFruits = new ArrayList<>();
		try {
			HtmlPage ripeAndReadyPage = webClient.getPage(urlToScrape);
			List<HtmlAnchor> ripeAndReadyFruitAnchors = ripeAndReadyPage.getByXPath("//div[@class='productInfo']/h3/a");
			if (ripeAndReadyFruitAnchors.isEmpty()) {
				logger.info("No ripe and ready fruits found !");
				return ripeFruits;
			} else {
				for (HtmlAnchor ripeAndReadyFruitAnchor : ripeAndReadyFruitAnchors) {
					HtmlPage ripeAndReadyFruitPage = ripeAndReadyFruitAnchor.click();
					ripeFruits.add(getFruitDetails(ripeAndReadyFruitPage));
				}
			}
		} catch (IOException ioException) {
			logger.error(ioException.getMessage(), ioException);
		}
		return ripeFruits;
	}

	/**
	 * @return
	 */
	private WebClient getWebClient() {
		WebClient webClient = new WebClient();
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setTimeout(12000);
		ProxyConfig proxyConfig = new ProxyConfig("10.6.20.5", 8887);
		webClient.getOptions().setProxyConfig(proxyConfig);
		return webClient;
	}

	/**
	 * @param client
	 * @param ripeAndReadyFruitPage
	 * @throws IOException
	 */
	private RipeFruit getFruitDetails(HtmlPage ripeAndReadyFruitPage) throws IOException {
		String webPageSize = getWebPageSize(ripeAndReadyFruitPage.getUrl());
		HtmlElement title = (HtmlElement) ripeAndReadyFruitPage
				.getByXPath("//div[@class='productTitleDescriptionContainer']/h1").get(0);
		HtmlElement unitPrice = (HtmlElement) ripeAndReadyFruitPage
				.getByXPath("//div[@class='pricing']/p[@class='pricePerUnit']").get(0);
		List<HtmlElement> descriptionElementList = ripeAndReadyFruitPage.getByXPath("//h3[text()='Description']");
		return new RipeFruit(title.getTextContent(), webPageSize, getUnitPrice(unitPrice.getTextContent()),
				getDescription(descriptionElementList));

	}

	/**
	 * This method will get the web page size. The size returned is in Kb.
	 * 
	 * @param client
	 * @param url
	 * @return
	 * @throws FailingHttpStatusCodeException
	 * @throws IOException
	 */
	private static String getWebPageSize(URL url) throws FailingHttpStatusCodeException, IOException {
		NumberFormat formatter = new DecimalFormat("#0.0");
		Double webPageSize = 0.0;
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.6.20.5", 8887));
		URLConnection openConnection = url.openConnection(proxy);
		try (BufferedReader in = new BufferedReader(new InputStreamReader(openConnection.getInputStream()))) {
			Stream<String> lines = in.lines();
			webPageSize += lines.mapToInt(String::length).sum();
			lines.close();
		}
		if (webPageSize > 1000) {
			return new StringBuilder(formatter.format(webPageSize / 1000)).append("Kb").toString();
		}
		return "0.0Kb";
	}

	/**
	 * This method returns the unit price after stripping £ symbol and some suffix.
	 * 
	 * @param price
	 *            the price from the web page
	 * @return the price in double
	 */
	private Double getUnitPrice(String price) {
		StringBuilder trimmedPriceStringBuilder = new StringBuilder(price.trim());
		int indexOfSlash = trimmedPriceStringBuilder.indexOf("/");
		String priceTrimmed = trimmedPriceStringBuilder.toString().substring(1, indexOfSlash);
		return new Double(priceTrimmed);
	}

	/**
	 * This method gets description from multiple tags for the item.
	 * 
	 * @param descriptionElement
	 *            the description list
	 * @return the description for the item.
	 */
	private String getDescription(List<HtmlElement> descriptionElement) {
		StringBuilder description = new StringBuilder();
		descriptionElement.stream().forEach(element -> {
			String textContent = element.getNextElementSibling().getTextContent();
			if (StringUtils.isNotBlank(textContent.trim()) || "\n".equals(textContent.trim())) {
				description.append(" ").append(textContent);
			}
		});
		return description.toString();
	}
}