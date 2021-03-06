package blservice.recordblservice;

import dataenum.BillType;
import dataenum.Storage;

public class BusinessStateInputInfo {
	/** 起始时间 */
	public String beginDate;
	/** 结束时间 */
	public String endDate;
	/** 客户名 */
	public String clientName;
	/** 业务员 */
	public String salesman;
	/** 仓库 */
	public Storage storage;
	/** 单据类型 */
	public BillType billType;
	
	/**
	 * 经营历程表的筛选条件
	 * @param beginDate
	 * @param endDate
	 * @param billType
	 * @param clientName
	 * @param salesman
	 * @param storage
	 */
	public BusinessStateInputInfo(String beginDate, String endDate, BillType billType, String clientName, String salesman, Storage storage) {
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.billType = billType;
		this.clientName = clientName;
		this.salesman = salesman;
		this.storage = storage;
	}
}
