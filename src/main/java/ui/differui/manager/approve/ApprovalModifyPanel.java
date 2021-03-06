package ui.differui.manager.approve;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import ui.commonui.exitfinish.ExitFinishFrame;
import ui.commonui.myui.MyColor;
import ui.commonui.myui.MyPanel;
import ui.commonui.myui.MyTable;
import ui.commonui.warning.WarningFrame;
import vo.AccountBillVO;
import vo.CashBillVO;
import vo.CashItemVO;
import vo.InventoryBillVO;
import vo.PurchaseVO;
import vo.ValueObject;
import vo.commodity.CommodityItemVO;
import vo.sale.SalesVO;
import blservice.approvalblservice.ApprovalBLService;
import businesslogic.approvalbl.ApprovalController;
import dataenum.BillType;
import dataenum.ResultMessage;

public class ApprovalModifyPanel extends MyPanel implements ActionListener{

	private static final long serialVersionUID = 1L;

	MyTable table ;
	JButton bt_return, bt_finish;
	JTextField textField;
	
	public static JButton modify;
	
	ValueObject currentBill;
	BillType currentType;
	
	public ApprovalModifyPanel(BillType billType, ValueObject bill){
		
		currentBill = bill;
		currentType = billType;
		
		int width = 800;
		int height = 400;
		
		Color foreColor = Color.WHITE;
		Color backColor = MyColor.getColor();
		
		this.setBounds((1280 - width) / 2, (720 - height) / 2, width, height);
		
		//information bar
		JLabel infoBar = new JLabel("修改单据数据",JLabel.CENTER);
		infoBar.setBounds(0, 0, width, 20);
		infoBar.setOpaque(true);
		infoBar.setForeground(foreColor);
		infoBar.setBackground(backColor);
		this.add(infoBar);
		
		initTable(billType, bill);
			
		bt_return = new JButton("返回");
		bt_return.setBounds(800 - 145, 350, 120, 25);
		bt_return.addActionListener(this);
		bt_return.setBackground(Color.WHITE);
		bt_return.setForeground(backColor);
		bt_return.setVisible(true);
		this.add(bt_return); 
		
		bt_finish = new JButton("确认");
		bt_finish.setBounds(800 - 145 - 145, 350, 120, 25);
		bt_finish.addActionListener(this);
		bt_finish.setBackground(Color.WHITE);
		bt_finish.setForeground(backColor);
		bt_finish.setVisible(true);
		this.add(bt_finish);
		
		JLabel word = new JLabel("备注：");
		word.setBounds(25, 300, 50, 25);
		word.setBackground(null);
		word.setForeground(Color.WHITE);
		word.setOpaque(false);
		word.setVisible(true);
		this.add(word);
		
		textField = new JTextField();
		textField.setBounds(25 + 50, 300, 700, 25);
		textField.setVisible(true);
		textField.setForeground(foreColor);
		textField.setBackground(backColor);
		this.add(textField);
		
		modify = new JButton();
		modify.addActionListener(this);
		this.add(modify);
	}
	
	public void actionPerformed(ActionEvent events){
		
		if(events.getSource() == bt_return){
			ApprovalModifyUI.button_close.doClick();
		}
		
		if(events.getSource() == bt_finish){
			if(table.isEditing())
				table.getCellEditor().stopCellEditing();
			
			ExitFinishFrame eff = new ExitFinishFrame("ApprovalModify");
			eff.setVisible(true);
		}
		
		if(events.getSource() == modify){
			finish(currentType);
		}
	}
	
	private void finish(BillType billType){
		ApprovalBLService controller = new ApprovalController();
		
		ResultMessage rm = null;
		
		if(billType.equals(BillType.PURCHASE) || billType.equals(BillType.PURCHASEBACK)){
			PurchaseVO vo = (PurchaseVO)currentBill;
			ArrayList<CommodityItemVO> commodities = new ArrayList<CommodityItemVO>();
			
			double sum = 0;
			double price = 0;
			int num = 0;
			
			for(int i = 0; i < table.getRowCount(); i++){
				
				num = Integer.parseInt((String)table.getValueAt(i, 3).toString());
			
				price = Double.parseDouble((String)table.getValueAt(i, 4).toString());
				
				//String ID, int number, double price, String remark, String name, String type
				commodities.add(new CommodityItemVO((String)table.getValueAt(i, 0), num, price, (String)table.getValueAt(i, 5)
						, (String)table.getValueAt(i, 1), (String)table.getValueAt(i, 2)));
				sum = sum + num * price;
			}
			
			//BillType type, String ID, String clientID, String client,
			//String user, Storage storage, ArrayList<CommodityItemVO> commodities, double beforePrice, BillState state
			rm = controller.updateBill(new PurchaseVO(billType, vo.ID, vo.clientID,
					vo.client, vo.user, vo.storage,commodities,sum,vo.state), billType);
			
		}else if(billType.equals( BillType.SALE )|| billType.equals(BillType.SALEBACK)){
			SalesVO vo = (SalesVO)currentBill;
			ArrayList<CommodityItemVO> commodities = new ArrayList<CommodityItemVO>();
			
			double sum = 0;
			double price = 0;
			int num = 0;
			
			for(int i = 0; i < table.getRowCount(); i++){
				num = Integer.parseInt((String)table.getValueAt(i, 3).toString());
				
				price = Double.parseDouble((String)table.getValueAt(i, 4).toString());
				
				sum = sum + num * price;
				
				//String ID, int number, double price, String remark, String name, String type
				commodities.add(new CommodityItemVO((String)table.getValueAt(i, 0), num, price, (String)table.getValueAt(i, 5)
						, (String)table.getValueAt(i, 1), (String)table.getValueAt(i, 2)));
			}
			//String ID, String clientID, String client, Storage storage, String user,
			//String salesman, ArrayList<CommodityItemVO> commodities, String remark, 
			//double beforePrice, double allowance, double voucher, double afterPrice, BillType type, BillState state
			double afterSum = sum - vo.afterPrice - vo.voucher;
			rm = controller.updateBill(new SalesVO(vo.ID, vo.clientID, vo.client, vo.storage
					, vo.user, vo.salesman, commodities, textField.getText()
					, sum, vo.allowance, vo.voucher, afterSum, vo.type, vo.state ), billType);
		}else if(billType.equals(BillType.GIFT) || billType.equals(BillType.LOSS) || 
				billType.equals(BillType.OVERFLOW) || billType.equals(BillType.ALARM)){
			
			InventoryBillVO vo = (InventoryBillVO)currentBill;
			
			ArrayList<CommodityItemVO> commodities = new ArrayList<CommodityItemVO>();
			
			double price = 0;
			int num = 0;
			
			for(int i = 0; i < table.getRowCount(); i++){
				num = Integer.parseInt((String)table.getValueAt(i, 3).toString());
				
				price = 0;
				
				//String ID, int number, double price, String remark, String name, String type
				commodities.add(new CommodityItemVO((String)table.getValueAt(i, 0), num, price, null
						, (String)table.getValueAt(i, 1), (String)table.getValueAt(i, 2)));
			}
			
			//String ID, BillType billType, ArrayList<CommodityItemVO> commodities, String remark, BillState state
			rm = controller.updateBill(new InventoryBillVO(vo.ID, billType, commodities,vo.remark, vo.state), billType);
		}else if(billType.equals(BillType.CASH)){
			
			CashBillVO vo = (CashBillVO)currentBill;
			
			ArrayList<CashItemVO> bills = new ArrayList<CashItemVO>();
			
			double sum = 0;
			
			for(int i = 0; i < table.getRowCount(); i++){
				//String name, double money, String remark
				
				double price = Double.parseDouble((String)table.getValueAt(i, 3));
				sum = sum + price;
				bills.add(new CashItemVO((String)table.getValueAt(i, 1), price, (String)table.getValueAt(i, 2)));
			}
			
			//String ID, String user, String account, ArrayList<CashItemVO> bills, double total, BillState state
			rm = controller.updateBill(new CashBillVO(vo.ID, vo.user, vo.account, bills, sum, vo.state), billType);
		}
		
		bt_return.doClick();
		
		if(rm.equals(ResultMessage.SUCCESS)){
			WarningFrame wf = new WarningFrame("单据修改成功！");
			wf.setVisible(true);
		}else{
			WarningFrame wf = new WarningFrame("单据修改失败！");
			wf.setVisible(true);
		}
			
	}
	
	private void initTable(BillType billType, ValueObject bill){
		
		
		if(billType.equals(BillType.PURCHASE) || billType.equals(BillType.PURCHASEBACK)){
			purchaseTable((PurchaseVO)bill);	
		}else if(billType.equals(BillType.SALE )|| billType.equals(BillType.SALEBACK)){
			saleTable((SalesVO)bill);
		}else if(billType.equals(BillType.GIFT) || billType.equals(BillType.LOSS) || 
				billType.equals(BillType.OVERFLOW) || billType.equals(BillType.ALARM)){
			inventoryTable((InventoryBillVO)bill);
		}else if(billType.equals(BillType.PAY)||billType.equals(BillType.EXPENSE)){
			accountTable((AccountBillVO)bill);
		}else if(billType.equals(BillType.CASH)){
			cashTable((CashBillVO)bill);
		}
		initJsp();
	}
	
	private void initJsp(){
		JScrollPane jsp = new JScrollPane(table);
		jsp.setBounds(25, 40, 750, 250);
		jsp.getViewport().setBackground(new Color(0,0,0,0.3f));
		jsp.setOpaque(false);
		jsp.setVisible(true);
		this.add(jsp);
	}
	
	private void purchaseTable(PurchaseVO bill){
		String[] headers = {"商品编号","商品名称","商品型号","商品数量","商品价格","商品备注"};
		table = new MyTable(headers, true);
		
		
		ArrayList<CommodityItemVO> list = bill.commodities;
		
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		
		for(int i = 0; i < list.size(); i++ ){
			CommodityItemVO cvo = list.get(i);
			Object[] rowData = {cvo.ID, cvo.name, cvo.type, cvo.number, cvo.price, cvo.remark};
			tableModel.addRow(rowData);
		}
	}
	
	private void saleTable(SalesVO bill){
		String[] headers = {"商品编号","商品名称","商品型号","商品数量","商品价格","商品备注"};
		table = new MyTable(headers, true);
		ArrayList<CommodityItemVO> list = bill.commodities;
		
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		
		for(int i = 0; i < list.size(); i++ ){
			CommodityItemVO cvo = list.get(i);
			Object[] rowData = {cvo.ID, cvo.name, cvo.type, cvo.number, cvo.price, cvo.remark};
			tableModel.addRow(rowData);
		}
	}
	
	private void inventoryTable(InventoryBillVO bill){
		String[] headers = {"商品编号","商品名称","商品型号","商品数量"};
		table = new MyTable(headers, true);

		ArrayList<CommodityItemVO> list = bill.commodities;
		
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		
		for(int i = 0; i < list.size(); i++ ){
			CommodityItemVO cvo = list.get(i);
			Object[] rowData = {cvo.ID, cvo.name, cvo.type, cvo.number};
			tableModel.addRow(rowData);
		}
	}
	
	private void accountTable(AccountBillVO bill){
		String[] headers = {"转账编号","客户名称","账户名称","转账金额"};
		table = new MyTable(headers, true);
		
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
			
		Object[] rowData = {bill.ID, bill.clientName, bill.bills.get(0).accountName, bill.sumMoney};
		tableModel.addRow(rowData);
		
	}
	
	private void cashTable(CashBillVO bill){
		String[] headers = {"条目序号","条目名称","备注","条目金额"};
		
		table = new MyTable(headers, true);
		
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		
		for(int i = 0; i < bill.bills.size(); i++){
			
			if(bill.bills.get(i).remark.equals(null)){
				Object[] rowData = {i + 1, bill.bills.get(i).name, "无", bill.bills.get(i).money};
				tableModel.addRow(rowData);
			}else{
				Object[] rowData = {i + 1, bill.bills.get(i).name, bill.bills.get(i).remark, bill.bills.get(i).money};
				tableModel.addRow(rowData);
			}
						
		}
	
	}
}
