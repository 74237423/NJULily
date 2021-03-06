package businesslogic.accountbillbl;

import java.rmi.RemoteException;
import java.util.ArrayList;

import po.AccountBillPO;
import vo.AccountBillVO;
import dataenum.BillState;
import dataenum.BillType;
import dataservice.accountbilldataservice.AccountBillDataService;

public class AccountBillShow {

	private AccountBillDataService accountBillData;
	private ArrayList<AccountBillVO> billsVO;
	
	public AccountBillShow() {
		AccountBill accountBill = new AccountBill();
		accountBillData = accountBill.getAccountBillData();
	}

	public ArrayList<AccountBillVO> showPay() throws RemoteException {
		return showChoose(BillType.PAY);
	}

	public ArrayList<AccountBillVO> showExpense() throws RemoteException {
		return showChoose(BillType.EXPENSE);
	}

	public ArrayList<AccountBillVO> showPayApproving() throws RemoteException {
		return showChoose(BillType.PAY, BillState.APPROVALING);
	}

	public ArrayList<AccountBillVO> showExpenseApproving() throws RemoteException {
		return showChoose(BillType.EXPENSE, BillState.APPROVALING);
	}

	public ArrayList<AccountBillVO> showPayPass() throws RemoteException {
		return showChoose(BillType.PAY, BillState.SUCCESS);
	}

	public ArrayList<AccountBillVO> showExpensePass() throws RemoteException {
		return showChoose(BillType.EXPENSE, BillState.SUCCESS);
	}

	public ArrayList<AccountBillVO> showPayFailure() throws RemoteException {
		return showChoose(BillType.PAY, BillState.FAILURE);
	}

	public ArrayList<AccountBillVO> showExpenseFailure() throws RemoteException {
		return showChoose(BillType.EXPENSE, BillState.FAILURE);
	}

	public ArrayList<AccountBillVO> showPayDraft() throws RemoteException {
		return showChoose(BillType.PAY, BillState.DRAFT);
	}

	public ArrayList<AccountBillVO> showExpenseDraft() throws RemoteException {
		return showChoose(BillType.EXPENSE, BillState.DRAFT);
	}

	/**
	 * @return 全部的收款单和付款单
	 * @author cylong
	 * @version 2014年12月1日 下午3:21:44
	 * @throws RemoteException
	 */
	private ArrayList<AccountBillVO> show() throws RemoteException {
		if (billsVO != null) {
			return billsVO;
		}
		ArrayList<AccountBillPO> billsPO = accountBillData.show();
		billsVO = AccountBillTrans.billsPOToBillsVO(billsPO);
		return billsVO;
	}

	/**
	 * 根据选择的类型显示
	 * @param type
	 * @return
	 * @author Zing
	 * @version Dec 12, 2014 2:17:03 AM
	 * @throws RemoteException
	 */
	private ArrayList<AccountBillVO> showChoose(BillType type) throws RemoteException {
		ArrayList<AccountBillVO> choose = new ArrayList<AccountBillVO>();
		ArrayList<AccountBillVO> show = show();
		for(AccountBillVO vo : show) {
			if (vo.type == type) {
				choose.add(vo);
			}
		}
		return choose;
	}

	/**
	 * 根据选择的类型和状态显示
	 * @param type
	 * @param state
	 * @return
	 * @author Zing
	 * @version Dec 12, 2014 2:17:17 AM
	 * @throws RemoteException
	 */
	private ArrayList<AccountBillVO> showChoose(BillType type, BillState state) throws RemoteException {
		ArrayList<AccountBillVO> choose = new ArrayList<AccountBillVO>();
		ArrayList<AccountBillVO> show = show();
		for(AccountBillVO vo : show) {
			if (vo.type == type && vo.state == state) {
				choose.add(vo);
			}
		}
		return choose;
	}

}
