# testwebscrape

The test is written using
1. Java 8
2. Spring Boot 2
3. Spring Shell 2
4. Htmlunit

To run the test, please download the project and from the root run the maven command: mvn clean install spring-boot:run
On the command prompt, you will see a prompt scrape-shell:>
Enter the command: scrapeweb <URL>

The URL's tested are:
https://www.sainsburys.co.uk/webapp/wcs/stores/servlet/CategoryDisplay?langId=44&categoryId=185749&storeId=10151&krypto=ra2ZlCQ%2BdFvgpXxQq6xOXGwJdgBGKCxF0kd4Nhcvy0%2Bq0XchDxVOTHXSNzvKu3WccKFNZljdIOdffi8D2PKgt7TLuB85bVzh0WHo0oW5jT6z1%2B9BvXpEqU38Pb2LV1j8GmPeEjG9qNl3TBte0QXliIHULjbAQTiN%2FGvHQLhqTFlUGh14enHxA8zFrEXMTt9k1nqW5IZBj1fgqezFqQruQDdi9KYcFLF%2FMpuKpLEA37zh%2B1pR%2Fi5JTnAWr%2Bpq5qszkqCMi8QtVFhZv%2B0VOYa8fd68eZiu5s6Fl07Vbwh9qkaX9CqMky%2FSk1TKZQOZcoIurEXEkpl41HXjckTQJylkWZjFr%2FMVAds117d0oE%2FwaparluuDwpthKT%2B%2B%2FabZbr3k#langId=44&storeId=10151&catalogId=10123&categoryId=185749&parent_category_rn=12518&top_category=12518&pageSize=20&orderBy=FAVOURITES_FIRST&searchTerm=&beginIndex=0&hideFilters=true

https://www.sainsburys.co.uk/webapp/wcs/stores/servlet/CategoryDisplay?langId=44

Drawbacks:
1. When I tried to apply some filters on the screen so that I can write integration tests for different scenarios, there were limited results. However there seems to be some redirect and the code does not seem to parse the html as it does with the original URL. For example, the following URL loads all 12 and then a redirect applies filter to show 5 
https://www.sainsburys.co.uk/webapp/wcs/stores/servlet/CategoryDisplay?langId=44&categoryId=185749&storeId=10151&krypto=ra2ZlCQ%2BdFvgpXxQq6xOXGwJdgBGKCxF0kd4Nhcvy0%2Bq0XchDxVOTHXSNzvKu3WccKFNZljdIOdffi8D2PKgt7TLuB85bVzh0WHo0oW5jT6z1%2B9BvXpEqU38Pb2LV1j8GmPeEjG9qNl3TBte0QXliIHULjbAQTiN%2FGvHQLhqTFlUGh14enHxA8zFrEXMTt9k1nqW5IZBj1fgqezFqQruQDdi9KYcFLF%2FMpuKpLEA37zh%2B1pR%2Fi5JTnAWr%2Bpq5qszkqCMi8QtVFhZv%2B0VOYa8fd68eZiu5s6Fl07Vbwh9qkaX9CqMky%2FSk1TKZQOZcoIurEXEkpl41HXjckTQJylkWZjFr%2FMVAds117d0oE%2FwaparluuDwpthKT%2B%2B%2FabZbr3k#langId=44&storeId=10151&catalogId=10123&categoryId=185749&parent_category_rn=12518&top_category=12518&pageSize=20&orderBy=FAVOURITES_FIRST&searchTerm=&beginIndex=0&hideFilters=true&facet=4294966372
Hence the tests appear light. If the above as working, I could have written more integration tests for different outcomes.
2. Could not use lombok as it needs some IDE configuration. I did not want to complicate running the test if the evaluator does not use lombok.
3. Used spring shell for the first time and would have liked to write some tests around it.