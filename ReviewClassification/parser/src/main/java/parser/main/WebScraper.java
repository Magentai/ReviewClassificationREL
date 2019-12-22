package parser.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class WebScraper {

	public static void main(String[] args) throws IOException {

		String baseUrl = "https://www.tripadvisor.ru" ;
		String searchQuery = "/Hotel_Review-g298520-d5539323-Reviews-Maksim_Gorkiy_Hotel-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS" ;
		String jsonString = "[";
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		try {
			for(int i = 0; i < 10; i++){
				switch(i){
					case 0: searchQuery = "/Hotel_Review-g298520-d5539323-Reviews-Maksim_Gorkiy_Hotel-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 1: searchQuery = "/Hotel_Review-g298520-d1862609-Reviews-Park_Inn_by_Radisson_Kazan-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 2: searchQuery = "/Hotel_Review-g298520-d7281039-Reviews-Kazanskoye_Podvorye_Hostel-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 3: searchQuery = "/Hotel_Review-g298520-d8629415-Reviews-Doubletree_by_Hilton_Kazan_City_Center-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 4: searchQuery = "/Hotel_Review-g298520-d2198603-Reviews-Osobnyak_na_Teatralnoi-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 5: searchQuery = "/Hotel_Review-g298520-d1890656-Reviews-Courtyard_by_Marriott_Kazan_Kremlin-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 6: searchQuery = "/Hotel_Review-g298520-d4656264-Reviews-TatarInn_Hotel-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 7: searchQuery = "/Hotel_Review-g298520-d4499107-Reviews-Ramada_by_Wyndham_Kazan_City_Centre-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 8: searchQuery = "/Hotel_Review-g298520-d1382846-Reviews-Riviera-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 9: searchQuery = "/Hotel_Review-g298520-d1572550-Reviews-Hotel_Ibis_Kazan-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					default: break;
				}
				System.out.println(i);
				String searchUrl = baseUrl + searchQuery;
				HtmlPage page = client.getPage(searchUrl);
				//HtmlElement numberOfReviewsTab = (HtmlElement) page.getByXPath("//div[@class='pageNumbers']");
				List<HtmlElement> pageNumbers = (List<HtmlElement>) page.getByXPath("//a[@class='pageNum ']") ;
				for(int j = 0; j < Integer.valueOf(pageNumbers.get(0).asText()); j++){

					String urlForReview = "";
					if(j != 0){
						urlForReview = baseUrl + searchQuery.subSequence(0, 38) + "-or" + j*5 + "-" + searchQuery.subSequence(39, searchQuery.length());
					}
					else{
						urlForReview = baseUrl + searchQuery;
					}
					HtmlPage pageOfReviews = client.getPage(urlForReview + "#REVIEWS");
					List<HtmlElement> items = (List<HtmlElement>) pageOfReviews.getByXPath("//div[@class='hotels-review-list-parts-SingleReview__mainCol--2XgHm']");
					if(items.isEmpty()){
						System.out.println("No items found!");
					}else{
						for(HtmlElement htmlItem : items){
							HtmlElement scoreTab = ((HtmlElement) htmlItem.getFirstByXPath(".//div[@class='hotels-review-list-parts-RatingLine__bubbles--1oCI4']"));
							HtmlElement score = (HtmlElement) scoreTab.getFirstChild();
							//System.out.println(score.getAttribute("Class"));

							HtmlElement review = ((HtmlElement) htmlItem.getFirstByXPath(".//q[@class='hotels-review-list-parts-ExpandableReview__reviewText--3oMkH']"));
							//System.out.println(review.asText());

							Item item = new Item();
							switch(Integer.valueOf(score.getAttribute("Class").substring(24))){
								case 10: item.setScore(0); break;
								case 20: item.setScore(0); break;
								case 30: item.setScore(1); break;
								case 40: item.setScore(2); break;
								case 50: item.setScore(2); break;
								default: break;
							}
							item.setReview(review.asText());

							ObjectMapper mapper = new ObjectMapper();
							jsonString = jsonString + mapper.writeValueAsString(item) + ",";
						}
					}
				}
			}
			jsonString = jsonString + "]";
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(new File("MyJson2.txt")));
				bw.write(jsonString);
			} finally {
				try {
					bw.close();
				} catch (Exception e) {
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}

	}

}
