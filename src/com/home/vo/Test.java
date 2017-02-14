package com.home.vo;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String url = "http://cndic.naver.com/search/all?q=";
		Document d = Jsoup.connect(url+"°­¾ÆÁö").get();
		
		Elements es = d.select(".search_result dl");
		for(Element e : es) {
			System.out.println(e.select("dt").first());
		}
	}

}
