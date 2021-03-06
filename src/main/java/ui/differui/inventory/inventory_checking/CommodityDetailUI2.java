package ui.differui.inventory.inventory_checking;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import ui.commonui.myui.MyWindow;

public class CommodityDetailUI2 extends MyWindow implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	public static JButton button_close;
	
	public CommodityDetailUI2(String ID){
				
		CommodityDetailPanel2 cdp = new CommodityDetailPanel2(ID);
		cdp.setOpaque(true);
		cdp.setBackground(new Color(0, 0, 0, 0.35f));
		cdp.setBounds(240, 65, 800, 610);
		this.add(cdp);
		
		button_close = new JButton();
		button_close.addActionListener(this);
		this.add(button_close);
		
	}
	
	public void actionPerformed(ActionEvent events) {
		
		if(events.getSource() == button_close){
			this.setVisible(false);
		}
		
	}
}
