package com.home.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.home.application.Main;
import com.home.application.SearchResultDialog;
import com.home.enums.SearchMode;
import com.home.service.ListViewService;
import com.home.service.ComicsService;
import com.home.service.CrawlingService;
import com.home.vo.ComicsVo;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainController implements Initializable{
	
	@FXML private Text t_title;
	@FXML private ListView<ComicsVo> lv_ComicsList;
	@FXML private ChoiceBox<String> cb_keyField;
	@FXML private TextField tf_keyWord;
	@FXML private Button btn_search;
	@FXML private Button btn_viewComics;
	@FXML private Button btn_searchCancle;

	private Dialog<ComicsVo> searchResultDialog;
	private Alert alert = new Alert(AlertType.INFORMATION);
	
	private ComicsService cs = ComicsService.getInstance();
	private CrawlingService service = CrawlingService.getInstance();
	private ListViewService lvs = ListViewService.getInstance();
	
	public static SearchMode currSearchMode = null;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setComboBox();
		setListView();
		setButtonClickListener();
		searchResultDialog = new SearchResultDialog();
		btn_searchCancle.setDisable(true);
		Platform.runLater(() -> { tf_keyWord.requestFocus(); });
		lvs.setDoubleClickEvent(lv_ComicsList,() -> {
			viewComics();
		});
	}

	private void setButtonClickListener() {
		// TODO Auto-generated method stub
		btn_viewComics.setOnMouseClicked(e -> { viewComics(); });
		btn_search.setOnMouseClicked(e -> { searchComics(); });
		btn_searchCancle.setOnMouseClicked(e -> { searchCancle(); });
	}
	
	public void searchComics() {
		String keyField = cb_keyField.getSelectionModel().getSelectedItem();
		String keyWord = tf_keyWord.getText();
		List<ComicsVo> searchResult = cs.searchComics(keyField, keyWord);
		if(searchResult == null)
			showAlert("검색단어가 입력되지 않았습니다!","단어를 입력해 주세요.");
		else if(searchResult.size() == 0)
			showAlert("검색결과가 존재하지 않습니다!","영어의 경우 대소문자 구분하니 다시 검색해 주세요.");
		else{
			setSearchResult(searchResult);
			t_title.setText(keyWord + " 검색 결과");
			btn_searchCancle.setDisable(false);
		}
	}

	private void setSearchResult(List<ComicsVo> searchResult) {
		if(currSearchMode == SearchMode.RECENT_COMICS)
			lvs.addComics(searchResult);
		else if(searchResult.size() == 1){
			String url = searchResult.get(0).getUrl(); 
			lvs.addComics(service.detailComicsList(url));
		}
		else
			displaySearchResultDialog(searchResult);
	}
	
	private void displaySearchResultDialog(List<ComicsVo> searchResult) {
		SearchResultDialog.setLv_searchResult(searchResult);
		Optional<ComicsVo> selectedComics = searchResultDialog.showAndWait();
		lvs.setListView(this.lv_ComicsList);
		if(selectedComics.isPresent()){
			ComicsVo c = selectedComics.get();
			t_title.setText(c.getTitle());
			lvs.addComics(service.detailComicsList(c.getUrl()));
		}
	}

	private void viewComics() {
		try {
			ComicsVo selectedComics = lv_ComicsList.getSelectionModel().getSelectedItem();
			if(selectedComics == null)
				return;
			
			ComicsVo c = getImageList(selectedComics.getUrl());
			if(c != null)
				openWebView(c);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private ComicsVo getImageList(String url) throws IOException {
		String detailUrl = null;
		if(currSearchMode == null || currSearchMode == SearchMode.RECENT_COMICS){
			//최근 만화 목록에서 접근한 경우, 한번 더 거쳐가야 한다.
			try{
				detailUrl = service.getSelectedComicsRecentList(url);
			} catch(IllegalArgumentException e) {
				showAlert("만화를 불러오지 못했습니다. 관리자에게 문의해 주세요","에러내용\n"+e);
				return null;
			}
		}
		else
			detailUrl = url;
		
		List<String> imgList = service.getImgComics(detailUrl);
		ComicsVo c = new ComicsVo();
		c.setUrl(detailUrl);
		c.setImgList(imgList);
		return c;
	}
	private void showAlert(String header,String content) {
		alert.setTitle("알림");
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}
	
	private void searchCancle() {
		currSearchMode = null;
		lvs.addComics(service.getRecentComicsList());
		btn_searchCancle.setDisable(true);
		t_title.setText("최근 만화 목록");
		tf_keyWord.setText("");
	}

	private void setListView() {
		lvs.setListView(this.lv_ComicsList);
		lvs.addComics(service.getRecentComicsList());
	}

	private void setComboBox() {
		ObservableList<String> keyField = 
				FXCollections.observableArrayList(SearchMode.RECENT_COMICS.getString(),SearchMode.ALL_COMICS.getString());
		cb_keyField.setValue(SearchMode.RECENT_COMICS.getString());
		cb_keyField.itemsProperty().set(keyField);
	}
	
	private void openWebView(ComicsVo c) throws IOException {
		FXMLLoader loader = new FXMLLoader(Main.viewFXML);
		loader.setControllerFactory(new Callback<Class<?>, Object>() {
			
			@Override
			public Object call(Class<?> param) {
				WebViewController controller = new WebViewController();
				controller.setComics(c.getUrl(),c.getImgList());
				return controller;
			}
		});
		VBox box = loader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(box));
		stage.show();
	}
}
