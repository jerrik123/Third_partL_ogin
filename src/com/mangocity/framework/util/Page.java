package com.mangocity.framework.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mangocity.framework.base.domain.entity.Entity;

/**
 * 对数据对象的分页操作
 */
public class Page implements Serializable {

	private static final long serialVersionUID = 7635109235523072044L;

	private static final int DEFAULT_PAGE_SIZE = 10;

	private static final int DEFAULT_PAGE_NO = 1;

	private int pageNo = DEFAULT_PAGE_NO;

	private int totalNum = 0;

	private int pageSize = DEFAULT_PAGE_SIZE;

	private List<Entity> pageList = new ArrayList<Entity>();

	public Page() {
	}

	public Page(int pageNo, int pageSize, int totalNum) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.totalNum = totalNum;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		pageNo = pageNo > 0 ? pageNo : DEFAULT_PAGE_NO;
		this.pageNo = pageNo;
	}

	public int getTotalPage() {
		return (totalNum - 1) / pageSize + 1;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		pageSize = pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;
		this.pageSize = pageSize;
	}

	public List<Entity> getPageList() {
		if (pageList == null) {
			return new ArrayList<Entity>();
		}
		return pageList;
	}

	public void setPageList(List<Entity> pageList) {
		this.pageList = pageList;
	}

	public int getStartIndex() {

		int startNo = (pageNo - 1) * pageSize;
		if (startNo > totalNum) {
			pageNo = 1;
			startNo = (pageNo - 1) * pageSize;
		}
		return startNo;
	}

	public void setTotalNum(int totalNum) {
		totalNum = totalNum < 0 ? 0 : totalNum;
		this.totalNum = totalNum;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public String toString() {
		return "Page{" + "pageNo=" + pageNo + ", pageSize=" + pageSize
				+ ", pageList=" + pageList + "}";
	}
}
