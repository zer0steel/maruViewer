package com.home.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class WebViewController implements Initializable{
	
	@FXML WebView webView;
	private WebEngine engine;
	
	private String url = null;
	
	private List<String> list = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		engine = webView.getEngine();
		StringBuilder sb = new StringBuilder();
		for(String img : list) 
			sb.append("<img src=").append(url).append(img.replace(" ", "%20")).append(">").append("<br><br>");
		
		sb.append("<br><br><br>");
		engine.loadContent(sb.toString());
	}

	public void setComics(String url, List<String> list) {
		// TODO Auto-generated method stub
		int startIndex = url.indexOf(".");
		int endIndex = url.indexOf("/",startIndex + 1);
		this.url = url.substring(0, endIndex);
		this.list = list;
	}

}
