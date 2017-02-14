package com.home.service;


import org.jsoup.nodes.Element;

@FunctionalInterface
public interface Strategy <T> {

	public T getHtml(Element e);
}
