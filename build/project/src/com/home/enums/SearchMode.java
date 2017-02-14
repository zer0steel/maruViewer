package com.home.enums;

public enum SearchMode {
	RECENT_COMICS("�ֱٸ�ȭ"), ALL_COMICS("��縸ȭ");
	private String mode;
	
	private SearchMode(String mode) {
		this.mode = mode;
	}
	
	public String getString() {
		return this.mode;
	}
	
	public static SearchMode valueOfString(String mode) {
		if(mode.equals(SearchMode.RECENT_COMICS.getString()))
			return SearchMode.RECENT_COMICS;
		else
			return SearchMode.ALL_COMICS;
	}
}
