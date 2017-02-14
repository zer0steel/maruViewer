package com.home.application;


import java.util.List;

import com.home.service.ListViewService;
import com.home.vo.ComicsVo;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class SearchResultDialog extends Dialog<ComicsVo>{
	private static ListView<ComicsVo> lv_searchResult;
	private ButtonType btn_ok;
	private ButtonType btn_cancle;
	
	public SearchResultDialog() {
		DialogPane dialogPane = new DialogPane();
		
		lv_searchResult = new ListView<ComicsVo>();
		btn_ok = new ButtonType("선택",ButtonData.YES);
		btn_cancle = new ButtonType("취소",ButtonData.CANCEL_CLOSE);
		
		VBox box = new VBox();
		box.getChildren().add(lv_searchResult);
		
		dialogPane.setContent(box);
		dialogPane.getButtonTypes().add(btn_ok);
		dialogPane.getButtonTypes().add(btn_cancle);
		
		this.setDialogPane(dialogPane);
		
		this.setTitle("검색 결과");
		setResultData();
		
	}
	
	private void setResultData() {
		this.setResultConverter(new Callback<ButtonType, ComicsVo>() {
			
			@Override
			public ComicsVo call(ButtonType param) {
				// TODO Auto-generated method stub
				if(param.getButtonData() == ButtonData.YES)
					return lv_searchResult.getSelectionModel().getSelectedItem();
				
				return null;
			}
		});
		
		lvs.setDoubleClickEvent(lv_searchResult, () -> {
			this.setResultConverter(new Callback<ButtonType, ComicsVo>() {
				
				@Override
				public ComicsVo call(ButtonType param) {
					// TODO Auto-generated method stub
					return lv_searchResult.getSelectionModel().getSelectedItem();
				}
			});
			this.close();
		});
	}

	private static final int ROW_HEIGHT = 24;
	private static ListViewService lvs = ListViewService.getInstance();
	
	public static void setLv_searchResult(List<ComicsVo> searchResult) {
		lvs.setListView(lv_searchResult);
		lvs.addComics(searchResult);
		lv_searchResult.setPrefHeight(searchResult.size() * ROW_HEIGHT);
		lv_searchResult.setMaxHeight(500);
	}
}
