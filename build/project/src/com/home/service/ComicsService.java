package com.home.service;

import java.util.List;

import com.home.controller.MainController;
import com.home.enums.SearchMode;
import com.home.vo.ComicsVo;

public class ComicsService {
	private static ComicsService instance = new ComicsService();
	private ComicsService () {}
	public static ComicsService getInstance() { return instance; }
	
	private CrawlingService service = CrawlingService.getInstance();
	
	public List<ComicsVo> searchComics(String keyField, String keyWord) {
		if(keyWord.length() == 0){
			MainController.currSearchMode = null;
			return null;
		}
		
		MainController.currSearchMode = SearchMode.valueOfString(keyField);
		if(SearchMode.valueOfString(keyField) == SearchMode.RECENT_COMICS)
			return service.searchRecentComics(keyWord);
		else
			return service.searchAllComics(keyWord);
	}
}
