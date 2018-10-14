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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sainsbury.scrape.model.RipeFruit;

@Service
@ConfigurationProperties("proxy")
public class FruitScrapeServiceImpl implements FruitScrapeService {

	private static final String SINGLE_DECIMAL_FORMAT = "#0.0";
	private static final String SIZE_KB = "Kb";
	private static final String ZERO_SIZE = "0.0Kb";
	private static final String XPATH_DESCRIPTION = "//h3[text()='Description']";
	private static final String XPATH_UNIT_PRICE = "//div[@class='pricing']/p[@class='pricePerUnit']";
	private static final String XPATH_TITLE = "//div[@class='productTitleDescriptionContainer']/h1";
	private static final String XPATH_ANCHORS = "//div[@class='productInfo']/h3/a";
	private static Logger logger = LogManager.getLogger();

	@Value("${proxy.host}")
	private String proxyHost;

	@Value("${proxy.port}")
	private Integer proxyPort;

	@Override
	public List<RipeFruit> getRipeFruitsAfterScraping(String urlToScrape) {
		WebClient webClient = getWebClient();
		List<RipeFruit> ripeFruits = new ArrayList<>();
		try {
			HtmlPage ripeAndReadyPage = webClient.getPage(urlToScrape);
			List<HtmlAnchor> ripeAndReadyFruitAnchors = ripeAndReadyPage.getByXPath(XPATH_ANCHORS);
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
		if (StringUtils.isNotBlank(proxyHost)) {
			ProxyConfig proxyConfig = new ProxyConfig(proxyHost, proxyPort);
			webClient.getOptions().setProxyConfig(proxyConfig);
		}
		return webClient;
	}

	/**
	 * @param client
	 * @param ripeAndReadyFruitPage
	 * @throws IOException
	 */
	private RipeFruit getFruitDetails(HtmlPage ripeAndReadyFruitPage) throws IOException {
		String webPageSize = getWebPageSize(ripeAndReadyFruitPage.getUrl());
		HtmlElement title = (HtmlElement) ripeAndReadyFruitPage.getByXPath(XPATH_TITLE).get(0);
		HtmlElement unitPrice = (HtmlElement) ripeAndReadyFruitPage.getByXPath(XPATH_UNIT_PRICE).get(0);
		List<HtmlElement> descriptionElementList = ripeAndReadyFruitPage.getByXPath(XPATH_DESCRIPTION);
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
	private String getWebPageSize(URL url) throws FailingHttpStatusCodeException, IOException {
		NumberFormat formatter = new DecimalFormat(SINGLE_DECIMAL_FORMAT);
		Double webPageSize = 0.0;
		URLConnection openConnection = null;
		if (StringUtils.isNotBlank(proxyHost)) {
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
			openConnection = url.openConnection(proxy);
		} else {
			openConnection = url.openConnection();
		}

		try (BufferedReader in = new BufferedReader(new InputStreamReader(openConnection.getInputStream()))) {
			Stream<String> lines = in.lines();
			webPageSize += lines.mapToInt(String::length).sum();
			lines.close();
		}
		if (webPageSize > 1000) {
			return new StringBuilder(formatter.format(webPageSize / 1000)).append(SIZE_KB).toString();
		}
		return ZERO_SIZE;
	}

	/**
	 * This method returns the unit price after stripping £ symbol and some
	 * suffix.
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