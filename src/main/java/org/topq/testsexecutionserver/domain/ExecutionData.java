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
		insertData();
	}
	
	private void insertData() {
		ExecutionRow er1 = new ExecutionRow();
		er1.setExecutionNumber(1);
		er1.setScenario("default");
		er1.setAgent("master");
		er1.setTimeStamp("17.12.14 18:34");
		er1.setStatus("pass");
		data.add(er1);
		
		ExecutionRow er2 = new ExecutionRow();
		er2.setExecutionNumber(2);
		er2.setScenario("web-sanity");
		er2.setAgent("auto-web-sanity");
		er2.setTimeStamp("17.12.14 14:14");
		er2.setStatus("fail");
		data.add(er2);
		
		ExecutionRow er3 = new ExecutionRow();
		er3.setExecutionNumber(3);
		er3.setScenario("default");
		er3.setAgent("auto-reg");
		er3.setTimeStamp("16.12.14 22:12");
		er3.setStatus("pass");
		data.add(er3);
		
		ExecutionRow er4 = new ExecutionRow();
		er4.setExecutionNumber(4);
		er4.setScenario("server-reg");
		er4.setAgent("auto-server-reg");
		er4.setTimeStamp("16.12.14 19:46");
		er4.setStatus("pass");
		data.add(er4);
		
		ExecutionRow er5 = new ExecutionRow();
		er5.setExecutionNumber(5);
		er5.setScenario("web-reg");
		er5.setAgent("master");
		er5.setTimeStamp("15.12.14 18:34");
		er5.setStatus("fail");
		data.add(er5);
		
		ExecutionRow er6 = new ExecutionRow();
		er6.setExecutionNumber(6);
		er6.setScenario("server-sanity");
		er6.setAgent("auto-sanity");
		er6.setTimeStamp("14.12.14 10:06");
		er6.setStatus("pass");
		data.add(er6);
		
		ExecutionRow er7 = new ExecutionRow();
		er7.setExecutionNumber(7);
		er7.setScenario("web-smoke");
		er7.setAgent("master");
		er7.setTimeStamp("13.12.14 23:25");
		er7.setStatus("fail");
		data.add(er7);
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
