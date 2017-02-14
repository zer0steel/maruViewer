package com.home.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.home.vo.ComicsVo;

public class CrawlingService {
	
	private static CrawlingService service = new CrawlingService();
	private CrawlingService () {}
	public static CrawlingService getInstance() { return service; }
	
	private static final String RECENT_COMICS_ADDR = "http://marumaru.in/c/26";
	private static final String MARU_ADDR = "http://marumaru.in";
	private static final String SEARCH_RECENT_COMICS_ADDR = "http://marumaru.in/?m=bbs&bid=mangaup&where=subject&keyword=";
	private static final String SEARCH_ALL_COMICS_ADDR = "http://marumaru.in/?mod=search&keyword=";
	
	public List<ComicsVo> getRecentComicsList() {
		return template(RECENT_COMICS_ADDR, "a[cid]", 
				new Strategy<ComicsVo>() {

			@Override
			public ComicsVo getHtml(Element e) {
				// TODO Auto-generated method stub
				ComicsVo c = new ComicsVo();
				c.setTitle(e.select("div[cid]").first().ownText());
				c.setUrl(MARU_ADDR + e.attr("href"));
				return c;
			}
		});
	}
	
	public List<String> getImgComics(String url) {
		return template(url, ".gallery-template img", new Strategy<String>() {

			@Override
			public String getHtml(Element e) {
				return e.attr("data-src");
			}
		});
	}
	
	public List<ComicsVo> searchAllComics(String keyWord) {
		return template(SEARCH_ALL_COMICS_ADDR + keyWord, "#s_post .postbox a", new Strategy<ComicsVo>() {

			@Override
			public ComicsVo getHtml(Element e) {
				// TODO Auto-generated method stub
				ComicsVo c = new ComicsVo();
				c.setUrl(MARU_ADDR+e.attr("href"));
				c.setTitle(e.text());
				return c;
			}
		});
	}
	
	public List<ComicsVo> searchRecentComics(String keyWord) {
		return template(SEARCH_RECENT_COMICS_ADDR + keyWord, "tr[cid]", new Strategy<ComicsVo>() {

			@Override
			public ComicsVo getHtml(Element e) {
				// TODO Auto-generated method stub
				ComicsVo c = new ComicsVo();
				c.setTitle(e.select("div[cid]").first().ownText());
				c.setUrl(e.select("a").attr("href"));
				return c;
			}
		});
	}
	
	public List<ComicsVo> detailComicsList (String url) {
		 List<ComicsVo> list = template(url, "div#vContent a[target]", new Strategy<ComicsVo>() {

			@Override
			public ComicsVo getHtml(Element e) {
				String title = e.text();
				if(title.length() != 0){
					ComicsVo c = new ComicsVo();
					c.setTitle(title);
					c.setUrl(e.attr("href"));
					return c;
				}
				return null;
			}
		});
		list.remove(list.size()-1);
		return list;
	}
	
	public String getSelectedComicsRecentList(String url) throws IOException {
		Document d = Jsoup.connect(url).get();
		Elements es = d.select("#vContent div div span a");
		return es.attr("href");
	}
	
	private <T> List<T> template(String url, String selector, Strategy<T> strategy) {
		List<T> resultList = new ArrayList<>();
		
		Document d = null;
		try {
			d = Jsoup.connect(url).get();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Elements es = d.select(selector);
		for(Element e : es) {
			T result = strategy.getHtml(e);
			if(result != null)
				resultList.add(result);
		}
		return resultList;
	}

}
