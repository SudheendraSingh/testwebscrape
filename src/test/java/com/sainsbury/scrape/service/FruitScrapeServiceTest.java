package com.sainsbury.scrape.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.sainsbury.scrape.model.RipeFruit;

@RunWith(JUnit4.class)
public class FruitScrapeServiceTest {

	FruitScrapeServiceImpl fruitScrapeService = new FruitScrapeServiceImpl();

	@Test
	public void testSuccess() {
		List<RipeFruit> ripeFruits = fruitScrapeService.getRipeFruitsAfterScraping(
				"https://www.sainsburys.co.uk/webapp/wcs/stores/servlet/CategoryDisplay?langId=44&categoryId=185749&storeId=10151&krypto=ra2ZlCQ%2BdFvgpXxQq6xOXGwJdgBGKCxF0kd4Nhcvy0%2Bq0XchDxVOTHXSNzvKu3WccKFNZljdIOdffi8D2PKgt7TLuB85bVzh0WHo0oW5jT6z1%2B9BvXpEqU38Pb2LV1j8GmPeEjG9qNl3TBte0QXliIHULjbAQTiN%2FGvHQLhqTFlUGh14enHxA8zFrEXMTt9k1nqW5IZBj1fgqezFqQruQDdi9KYcFLF%2FMpuKpLEA37zh%2B1pR%2Fi5JTnAWr%2Bpq5qszkqCMi8QtVFhZv%2B0VOYa8fd68eZiu5s6Fl07Vbwh9qkaX9CqMky%2FSk1TKZQOZcoIurEXEkpl41HXjckTQJylkWZjFr%2FMVAds117d0oE%2FwaparluuDwpthKT%2B%2B%2FabZbr3k#langId=44&storeId=10151&catalogId=10123&categoryId=185749&parent_category_rn=12518&top_category=12518&pageSize=20&orderBy=FAVOURITES_FIRST&searchTerm=&beginIndex=0&hideFilters=true");
		assertNotNull("ripe furits are not returned for the filter", ripeFruits);
		assertEquals("Incorrent number of ripe furits are not returned for the filter", 12, ripeFruits.size());
		assertEquals("Incorrent total", "23.15",
				String.valueOf(ripeFruits.stream().mapToDouble(RipeFruit::getUnitPrice).sum()));
	}

	@Test
	public void testInvalidUrl() {
		List<RipeFruit> ripeFruits = fruitScrapeService.getRipeFruitsAfterScraping(
				"https://www.sainsburys.co.uk/webapp/wcs/stores/servlet/CategoryDisplay?langId=44");
		assertNotNull("ripe furits are not returned for the filter", ripeFruits);
		assertEquals("Incorrent number of ripe furits are not returned for the filter", 0, ripeFruits.size());
	}
}
