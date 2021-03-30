package com.parse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParceApp {
	
	private static Document getWeb(String url) {
		
			try {
				Document document = Jsoup.connect(url)
						.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.82 Safari/537.36")
						.timeout(10000)
						.get();
				return document;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
	}
	
	public static void getInfo(Element temp,	List<String[]>  sb,	int count) {
		String name;
		String info;
		String price;
		String delivery;
		String link;
		String size_of_discount;
		Integer i = count;

		
		
		name = temp.select("a[class=_w7z6o _uj8z7 meqh_en mpof_z0 mqu1_16 _9c44d_2vTdY m9qz_yq ]").text();
		if(name.hashCode()==0) 
		{name=temp.select("a[class=_w7z6o _uj8z7 meqh_en mpof_z0 mqu1_16 _9c44d_2vTdY  ]").text();}

		info = temp.select("dl[class=mp4t_0 m3h2_0 mryx_0 munh_0 mg9e_0 mvrt_0 mj7a_0 mh36_0 meqh_en msa3_z4 _1vx3o]").text();
		size_of_discount = temp.select("div[class=mp0t_ji mpof_vs _9c44d_1VS-Y  _9c44d_3_DDQ  mpof_vs _9c44d_2MDwk  ]").text();
		price = temp.select("div[class=msa3_z4 _9c44d_2K6FN]").text();
		delivery = temp.select("div[class=_9c44d_1xKGX]").text();
		
		link = temp.select("a[class=_w7z6o _uj8z7 meqh_en mpof_z0 mqu1_16 _9c44d_2vTdY  ]").attr("href");
		if(link.hashCode()==0) {
			link = temp.select("a[class=_w7z6o _uj8z7 meqh_en mpof_z0 mqu1_16 _9c44d_2vTdY m9qz_yq ]").attr("href");
		}
		String[] line = {i.toString(), name, info, size_of_discount, price, delivery, link};
		sb.add(line);	    
		
	}
	
	public static int getProductInfo(Document page, int count, List<String[]> sb){
		Elements products = page.getElementsByClass("mx7m_1 mnyp_co mlkp_ag");
		Elements size_of_discount;
		List<String[]> sb1 = sb;
		int count_elem;

		for(Element temp : products) {
			size_of_discount = temp.select("div[class=mp0t_ji mpof_vs _9c44d_1VS-Y  _9c44d_3_DDQ  mpof_vs _9c44d_2MDwk  ]");
			if(size_of_discount.text().contains("-")) {
				count_elem = count++;
				getInfo(temp,sb1,count_elem);
				
			}
		}
		return count;
	}
	
	
	public static void writeInfo(String sb) {
		try (PrintWriter writer = new PrintWriter(new File("test.csv"))) {

		      writer.write(sb.toString());
		}
		      catch (FileNotFoundException e) {
		          System.out.println(e.getMessage());
		        }
		
	}
	
	public static String changePage(String url, int index) {
		
		url = url.replace(url.charAt(url.length()-1), (char) (index + '0'));
		
		return url;
	}
	
	public static List<String[]> buffer() {
		List<String[]> sb = new ArrayList<>();
		 String[] header = {"id", "name", "info", "size_of_discount","price","delivery","link"};
		 sb.add(header);

		return sb;
		
	}
	public static void writeFile(List<String[]> sb) {
		try (CSVWriter writer = new CSVWriter(new FileWriter("C:\\Users\\NiCE\\Desktop\\filecsv\\alegro.csv"))) {
            writer.writeAll(sb);
            System.out.println("done");
        }catch (FileNotFoundException e) {
		      System.out.println(e.getMessage());
		    } catch (IOException e1) {
			e1.printStackTrace();
		}
	}


	public static void main(String[] args) throws IOException {
		String [] url = { 	"https://allegro.pl/kategoria/smartfony-i-telefony-komorkowe-165?bmatch=cl-nbn-e2101-d3681-c3682-ele-1-2-0319&p=1",
							"https://allegro.pl/kategoria/laptopy-491?bmatch=cl-nbn-e2101-d3681-c3682-ele-1-2-0319&p=1",
							"https://allegro.pl/kategoria/tv-i-video-telewizory-257732?bmatch=cl-nbn-e2101-d3681-c3682-ele-1-2-0319&p=1"};
		Document doc;
		int count_of_products =1;
		int index_of_page = 2;
		List<String[]> sb = buffer();
			for(int j=0;j<3;j++) {		
				for(int i = 0; i < 25; i++) {
					doc = getWeb(url[j]);
					count_of_products = getProductInfo(
							doc,count_of_products, sb);
					changePage(url[j], index_of_page);
					index_of_page++;
					if(count_of_products>=100) {
						count_of_products=1;
						break;
					}
					}
			}
		
		writeFile(sb);

			
	}

}
