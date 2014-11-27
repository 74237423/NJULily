package po;

import java.util.ArrayList;

/**
 * 现金费用单
 * 管理报销等现金操作，单据中包含：单据编号（XJFYD-yyyyMMdd-xxxxx）,
 * 操作员（当前登录用户），银行账户，条目清单，总额。
 * 条目清单中包括：条目名，金额，备注。
 * @author Zing
 * @version 2014年10月31日下午5:55:13
 */
public class CashBillPO extends PersistentObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 编号 */
	private String id;
	/** 操作员 */
	private String user;
	/** 银行账户 */
	private String account;
	/**  条目清单*/
	private ArrayList<CashItemPO> bills;
	/** 汇款总额 */
	private int sumMoney;
	
	public CashBillPO(String id, String user, String account, ArrayList<CashItemPO> bills, int sumMoney) {
		super(id);
		this.id = id;
		this.user = user;
		this.account = account;
		this.bills = bills;
		this.sumMoney = sumMoney;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public String getUser() {
		return user;
	}

	public String getAccount() {
		return account;
	}

	public ArrayList<CashItemPO> getBills() {
		return bills;
	}

	public int getSumMoney() {
		return sumMoney;
	}
	
	
}
