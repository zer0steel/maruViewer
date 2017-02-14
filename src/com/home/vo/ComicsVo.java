package com.home.vo;

import java.util.List;

public class ComicsVo {
	private String url;
	private String title;
	private List<ComicsVo> comicsList;
	private List<String> imgList;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<ComicsVo> getComicsList() {
		return comicsList;
	}
	public void setComicsList(List<ComicsVo> comicsList) {
		this.comicsList = comicsList;
	}
	@Override
	public String toString() {
		return "ComicsVo [url=" + url + ", title=" + title + ", comicsList=" + comicsList + "]";
	}
	public List<String> getImgList() {
		return imgList;
	}
	public void setImgList(List<String> imgList) {
		this.imgList = imgList;
	}
	
}
