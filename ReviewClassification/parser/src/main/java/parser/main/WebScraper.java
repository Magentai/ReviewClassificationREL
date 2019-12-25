package parser.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class WebScraper {

	public static void main(String[] args) throws IOException {

		String baseUrl = "https://www.tripadvisor.ru" ;
		String searchQuery = "/Hotel_Review-g298520-d5539323-Reviews-Maksim_Gorkiy_Hotel-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS" ;
		Date dateNow = new Date();
		SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy_MM_dd");
		Integer counter = 0;
		String jsonFinal = "[";
		BufferedWriter bw1 = null;
		BufferedWriter bw2 = null;
		BufferedWriter bw3 = null;
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		try {
			for(int i = 0; i < 33; i++){
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
					case 10: searchQuery = "/Hotel_Review-g298520-d6387253-Reviews-Moskovskaya-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 11: searchQuery = "/Hotel_Review-g298520-d8328952-Reviews-Blizzzko_Camping_Hostel-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 12: searchQuery = "/Hotel_Review-g298520-d6516838-Reviews-Livadiya-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 13: searchQuery = "/Hotel_Review-g298520-d4662194-Reviews-Kremlin_Hostel_Hostel_v_Tsentre-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 14: searchQuery = "/Hotel_Review-g298520-d2173918-Reviews-Hotel_Bulgar-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 15: searchQuery = "/Hotel_Review-g298520-d3377234-Reviews-Bilyar_Palace_Hotel-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 16: searchQuery = "/Hotel_Review-g298520-d5217951-Reviews-Davidov_Hotel-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 17: searchQuery = "/Hotel_Review-g298520-d1948250-Reviews-Polyot-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 18: searchQuery = "/Hotel_Review-g298520-d4167133-Reviews-Etnika-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 19: searchQuery = "/Hotel_Review-g298520-d2705042-Reviews-Hotel_Prestige_House_Verona-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 20: searchQuery = "/Hotel_Review-g298520-d1554334-Reviews-Ilmar_City_Hotel-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 21: searchQuery = "/Hotel_Review-g298520-d2480297-Reviews-Regina_on_Peterburgskaya-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 22: searchQuery = "/Hotel_Review-g298520-d4167130-Reviews-Express_Hotel_Hostel-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 23: searchQuery = "/Hotel_Review-g298520-d6522208-Reviews-Balkish-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 24: searchQuery = "/Hotel_Review-g298520-d2690263-Reviews-Akspai_Hotel-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 25: searchQuery = "/Hotel_Review-g298520-d2692145-Reviews-Hotel_Castro-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 26: searchQuery = "/Hotel_Review-g298520-d2704079-Reviews-Algorithm_Hotel-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 27: searchQuery = "/Hotel_Review-g298520-d6866648-Reviews-Hotel_on_Rotornaya-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 28: searchQuery = "/Hotel_Review-g298520-d2348571-Reviews-Fatima-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 29: searchQuery = "/Hotel_Review-g298520-d2705042-Reviews-Hotel_Prestige_House_Verona-Kazan_Republic_of_Tatarstan_Volga_District.html#REVIEWS"; break;
					case 30: searchQuery = "/Hotel_Review-g298484-d1162781-Reviews-Beta_Hotel_Izmailovo-Moscow_Central_Russia.html#REVIEWS"; break;
					case 31: searchQuery = "/Hotel_Review-g298484-d1535833-Reviews-Mandarin_Moscow_Hotel-Moscow_Central_Russia.html#REVIEWS"; break;
					case 32: searchQuery = "/Hotel_Review-g298484-d1436770-Reviews-Assambleya_Nikitskaya-Moscow_Central_Russia.html#REVIEWS"; break;
					default: break;
				}
				System.out.println(i);
				String searchUrl = baseUrl + searchQuery;
				HtmlPage page = client.getPage(searchUrl);
				//HtmlElement numberOfReviewsTab = (HtmlElement) page.getByXPath("//div[@class='pageNumbers']");
				List<HtmlElement> pageNumbers = (List<HtmlElement>) page.getByXPath("//a[@class='pageNum ']") ;
				if(pageNumbers.size() > 0){
					for(int j = 0; j < Integer.valueOf(pageNumbers.get(0).asText()); j++){

						String urlForReview = "";
						if(j != 0){
							urlForReview = baseUrl + searchQuery.subSequence(0, 38) + "-or" + j*5 + "-" + searchQuery.subSequence(39, searchQuery.length());
						}
						else{
							urlForReview = baseUrl + searchQuery;
						}
						HtmlPage pageOfReviews = client.getPage(urlForReview + "#REVIEWS");
						List<HtmlElement> items = (List<HtmlElement>) pageOfReviews.getByXPath("//div[@class='location-review-review-list-parts-SingleReview__mainCol--1hApa']");
						if(items.isEmpty()){
							System.out.println("No items found!");
						}else{
							for(HtmlElement htmlItem : items){
								HtmlElement scoreTab = ((HtmlElement) htmlItem.getFirstByXPath(".//div[@class='location-review-review-list-parts-RatingLine__bubbles--GcJvM']"));
								HtmlElement score = (HtmlElement) scoreTab.getFirstChild();
								//System.out.println(score.getAttribute("Class"));

								HtmlElement review = ((HtmlElement) htmlItem.getFirstByXPath(".//q[@class='location-review-review-list-parts-ExpandableReview__reviewText--gOmRC']"));
								//System.out.println(review.asText());

								Item item = new Item();

								switch (counter % 4){
									case 0:{
										item.setStudent_name("Semyonov Petr");
										item.setStudent_group(651);
										item.setStudent_number(15);
										item.setDate(formatForDateNow.format(dateNow));
										item.setData_source(baseUrl + searchQuery);
										break;
									}
									case 1:{
										item.setStudent_name("Gabdrahmanov Ruslan");
										item.setStudent_group(651);
										item.setStudent_number(9);
										item.setDate(formatForDateNow.format(dateNow));
										item.setData_source(baseUrl + searchQuery);
										break;
									}
									case 2:{
										item.setStudent_name("Muhtarova Diana");
										item.setStudent_group(651);
										item.setStudent_number(4);
										item.setDate(formatForDateNow.format(dateNow));
										item.setData_source(baseUrl + searchQuery);
										break;
									}
									case 3:{
										item.setStudent_name("Rafikova Inna");
										item.setStudent_group(651);
										item.setStudent_number(13);
										item.setDate(formatForDateNow.format(dateNow));
										item.setData_source(baseUrl + searchQuery);
										break;
									}
								}

								if(score.getAttribute("Class").equals("ui_bubble_rating bubble_10") || score.getAttribute("Class").equals("ui_bubble_rating bubble_20")){				//	ui_bubble_rating bubble_50
									// Заполнить json и отправить его в m1
									item.setTonality("m1");
									String filename = searchQuery.subSequence(22, 30) + "_" + counter.toString() + ".txt";
									item.setFilename("m1" + "//" + filename);
									writeToTheFiles("m1", filename, review.asText());
									ObjectMapper mapper = new ObjectMapper();
									jsonFinal = jsonFinal + mapper.writeValueAsString(item) + ",";
									counter++;
								}
								else if(score.getAttribute("Class").equals("ui_bubble_rating bubble_30")){			//	ui_bubble_rating bubble_50
									// Заполнить json и отправить его в zero
									item.setTonality("zero");
									String filename = searchQuery.subSequence(22, 30) + "_" + counter.toString() + ".txt";
									item.setFilename("zero" + "//" + filename);
									writeToTheFiles("zero", filename, review.asText());
									ObjectMapper mapper = new ObjectMapper();
									jsonFinal = jsonFinal + mapper.writeValueAsString(item) + ",";
									counter++;
								}
								else if(score.getAttribute("Class").equals("ui_bubble_rating bubble_40") || score.getAttribute("Class").equals("ui_bubble_rating bubble_50")){			//	ui_bubble_rating bubble_50
									// Заполнить json и отправить его в p1
									item.setTonality("p1");
									String filename = searchQuery.subSequence(22, 30) + "_" + counter.toString() + ".txt";
									item.setFilename("p1" + "//" + filename);
									writeToTheFiles("p1", filename, review.asText());
									ObjectMapper mapper = new ObjectMapper();
									jsonFinal = jsonFinal + mapper.writeValueAsString(item) + ",";
									counter++;
								}
							}
						}
					}
				}
				writeInfo("description.json", jsonFinal);
				jsonFinal = "";
			}
			jsonFinal = "]";
			writeInfo("description.json", jsonFinal);

		} catch(Exception e){
			e.printStackTrace();
		}

	}

	private static void writeToTheFiles(String directory, String filename, String json){

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File(directory + File.separator + filename), true));
			bw.write(json);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeInfo(String filename, String json) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File(filename),true));
			bw.write(json);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
