package ui.differui.manager.strategy;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



import javax.swing.JLabel;
import javax.swing.JPanel;



public class StrategyManagementUI extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	public StrategyManagementUI(){
		this.setLayout(null);
		this.setBounds(0, 0, 1280, 720);
		this.setOpaque(false);
		
		JLabel infoBar = new JLabel("制定促销策略");
		infoBar.setFont(new Font("华文细黑", Font.BOLD, 18));
		infoBar.setBounds(80, 14, 1100, 20);
		infoBar.setForeground(Color.GRAY);
		infoBar.setOpaque(false);
		this.add(infoBar);
		
		addPromotionTable();
		
		addButtons();
		
		
	}
	
	private void addPromotionTable() {
		PromotionData data = new PromotionData();
		String[] headersClient = {"编号","起始时间","结束时间","客户等级","应送赠品","折扣","代金券"};
		MyTable clientTable = new MyTable(headersClient, data.getClientPromotion());
		MyJScrollPane clientScrollPane = new MyJScrollPane(clientTable, 80, 50, 500, 150);

		String[] headersCommo = {"编号","起始时间","结束时间","购买商品","应送赠品","折扣","代金券"};
		MyTable commodityTable = new MyTable(headersCommo, data.getCommodityPromotion());
		MyJScrollPane commodityScrollPane = new MyJScrollPane(commodityTable, 80, 200, 500, 150);

		String[] headersTotal = {"编号","起始时间","结束时间","购买总价","应送赠品","折扣","代金券"};
		MyTable totalTable = new MyTable(headersTotal, data.getTotalPromotion());
		MyJScrollPane totalScrollPane = new MyJScrollPane(totalTable, 80, 350, 500, 150);
		
		this.add(clientScrollPane);
		this.add(commodityScrollPane);
		this.add(totalScrollPane);	
		
	}

	private void addButtons() {
		
		
	}

	public void actionPerformed(ActionEvent events) {
		
	}
}
