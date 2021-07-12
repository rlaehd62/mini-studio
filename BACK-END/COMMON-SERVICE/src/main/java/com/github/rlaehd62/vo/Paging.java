package com.github.rlaehd62.vo;

import org.springframework.data.domain.Page;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Paging
{
	private final static int SIZE = 10;
	private int startPage;
	private int endPage;
	private int currentPage;
	private int totalPage;
	
	public Paging(Page<?> page)
	{
		totalPage = page.getTotalPages();
		currentPage = page.getPageable().getPageNumber();
        startPage = 1 * SIZE * (currentPage / SIZE);
        endPage = startPage + SIZE - 1;
	}
}
