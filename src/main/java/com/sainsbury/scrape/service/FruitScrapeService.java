package com.sainsbury.scrape.service;

import java.util.List;

import com.sainsbury.scrape.model.RipeFruit;

public interface FruitScrapeService {
	/**
	 * This method will consume the webpage whose URL is passed and will return a
	 * list of ripe fruit details.
	 * 
	 * @param urlToScrape
	 *            the URL of the webpage to scrape.
	 * @return the list of ripe fruits
	 */
	List<RipeFruit> getRipeFruitsAfterScraping(String urlToScrape);
}
