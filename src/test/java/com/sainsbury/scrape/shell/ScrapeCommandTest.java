package com.sainsbury.scrape.shell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import com.sainsbury.scrape.model.RipeFruit;
import com.sainsbury.scrape.service.FruitScrapeService;

@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
public class ScrapeCommandTest {

	@InjectMocks
	ScrapeCommand scrapeCommand;

	@Mock
	private FruitScrapeService fruitScrapeService;

	@Test
	public void testNullUrl() {
		assertNull("Invalid URL should return null", scrapeCommand.scrapeweb(null));
	}

	@Test
	public void testInvalidUrl() {
		assertEquals("Invalid URL should return null", "{\"results\":[],\"total\":0.0}",
				scrapeCommand.scrapeweb("url"));
	}

	@Test
	public void testValidUrlSingleFruit() {
		List<RipeFruit> fruits = new ArrayList<>();
		RipeFruit avocado = new RipeFruit("Sainsbury's Avocado, Ripe & Ready x2", "30.2Kb", 1.80, "Avocaodos");
		fruits.add(avocado);
		Mockito.when(fruitScrapeService.getRipeFruitsAfterScraping("url")).thenReturn(fruits);
		String scrapeweb = scrapeCommand.scrapeweb("url");
		assertEquals("Invalid URL should return null",
				"{\"results\":[{\"title\":\"Sainsbury's Avocado, Ripe & Ready x2\",\"size\":\"30.2Kb\",\"unitPrice\":1.8,\"description\":\"Avocaodos\"}],\"total\":1.8}",
				scrapeweb);
	}

	@Test
	public void testValidUrlTwoFruits() {
		List<RipeFruit> fruits = new ArrayList<>();
		fruits.add(new RipeFruit("Sainsbury's Avocado, Ripe & Ready x2", "30.2Kb", 1.80, "Avocaodos"));
		fruits.add(new RipeFruit("Sainsbury's Kiwi Fruit, Ripe & Ready x4", "22.2Kb", 1.75, "Kiwi"));
		Mockito.when(fruitScrapeService.getRipeFruitsAfterScraping("url")).thenReturn(fruits);
		String scrapeweb = scrapeCommand.scrapeweb("url");
		assertEquals("Invalid URL should return null",
				"{\"results\":[{\"title\":\"Sainsbury's Avocado, Ripe & Ready x2\",\"size\":\"30.2Kb\",\"unitPrice\":1.8,\"description\":\"Avocaodos\"},{\"title\":\"Sainsbury's Kiwi Fruit, Ripe & Ready x4\",\"size\":\"22.2Kb\",\"unitPrice\":1.75,\"description\":\"Kiwi\"}],\"total\":3.55}",
				scrapeweb);
	}

}
