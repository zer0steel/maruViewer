package com.home.service;

import java.util.List;

import com.home.vo.ComicsVo;

import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ListViewService {
	
	private static ListViewService service = new ListViewService();
	private ListViewService () {}
	public static ListViewService getInstance() { return service; }
	
	private ListView<ComicsVo> lv;
	
	int firstClickTime;
	private static final int DOUBLE_CLICK_TIME_OUT = 500; 
	public void setDoubleClickEvent(ListView<ComicsVo> list , DClickStrategy clickStrategy) {
		list.setOnMouseClicked(e -> {
			if(firstClickTime == 0)
				firstClickTime = (int)System.currentTimeMillis();
			else{
				int secondClickTime = (int)System.currentTimeMillis();
				if(secondClickTime - firstClickTime <= DOUBLE_CLICK_TIME_OUT)
					clickStrategy.execute();
				firstClickTime = 0;
			}
		});
	}
	
	public void setListView(ListView<ComicsVo> lv) {
		this.lv = lv;
	}
	
	public void addComics(List<ComicsVo> list) {
		lv.setItems(FXCollections.observableList(list));
		changeDisplayListView();
	}
	
	public ComicsVo getComics() {
		return this.lv.getSelectionModel().getSelectedItem();
	}
	
	private void changeDisplayListView() {
		this.lv.setCellFactory(new Callback<ListView<ComicsVo>, ListCell<ComicsVo>>() {
			
			@Override
			public ListCell<ComicsVo> call(ListView<ComicsVo> lv) {
				// TODO Auto-generated method stub
				ListCell<ComicsVo> lc = new ListCell<ComicsVo>() {
					@Override
					protected void updateItem(ComicsVo c, boolean empty) {
						super.updateItem(c, empty);
						if (c != null) {
							setText(c.getTitle());
						}
					}
				};
				return lc;
			}
		});
	}
}
