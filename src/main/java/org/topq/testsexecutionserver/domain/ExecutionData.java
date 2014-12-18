package org.topq.testsexecutionserver.domain;

import java.util.ArrayList;
import java.util.List;

public class ExecutionData {

	private int draw;
	private int recordsTotal;
	private int recordsFiltered;
	private List<ExecutionRow> data;
	
	public ExecutionData() {
		draw = 1;
		recordsTotal = 57;
		recordsFiltered = 57;
		data = new ArrayList<ExecutionRow>();
	}
	
	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public int getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public int getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(int recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public List<ExecutionRow> getData() {
		return data;
	}

	public void setData(List<ExecutionRow> data) {
		this.data = data;
	}

}
