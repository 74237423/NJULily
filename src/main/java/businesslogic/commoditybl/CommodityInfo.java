package businesslogic.commoditybl;

import java.rmi.RemoteException;
import java.util.ArrayList;

import po.CommodityPO;
import vo.commodity.CommodityVO;
import blservice.inventoryblservice.InventoryBLService;
import businesslogic.accountinitbl.info.CommodityInfo_Init;
import businesslogic.inventorybl.InventoryController;
import businesslogic.inventorybl.info.CommodityInfo_Inventory;
import businesslogic.promotionbl.info.CommodityInfo_Promotion;
import businesslogic.purchasebl.info.CommodityInfo_Purchase;
import businesslogic.salebl.info.CommodityInfo_Sale;
import dataenum.BillType;
import dataservice.commoditydataservice.CommodityDataService;

public class CommodityInfo implements CommodityInfo_Sale, CommodityInfo_Purchase, CommodityInfo_Inventory, CommodityInfo_Promotion, CommodityInfo_Init {

	private Commodity commodity;

	private CommodityDataService commodityData;

	private CommodityPO po;

	public CommodityInfo() {
		commodity = new Commodity();
		this.commodityData = commodity.getCommodityData();
	}

	/*
	 * (non-Javadoc)
	 * @see businesslogic.salebl.CommodityInfo_Sale#getType(java.lang.String)
	 */
	public String getType(String ID) throws RemoteException {
		if (po == null) {
			po = commodityData.find(ID);
		}
		return po.getType();
	}

	/*
	 * (non-Javadoc)
	 * @see businesslogic.salebl.CommodityInfo_Sale#getName(java.lang.String)
	 */
	public String getName(String ID) throws RemoteException {
		if (po == null) {
			po = commodityData.find(ID);
		}
		return po.getName();
	}

	/*
	 * (non-Javadoc)
	 * @see businesslogic.inventorybl.info.CommodityInfo_Inventory#getAllID()
	 */
	public ArrayList<String> getAllID() throws RemoteException {
		return commodityData.getAllID();
	}

	/*
	 * (non-Javadoc)
	 * @see businesslogic.inventorybl.info.CommodityInfo_Inventory#getNumber(java.lang.String)
	 */
	public int getNumber(String ID) throws RemoteException {
		if (po == null) {
			po = commodityData.find(ID);
		}
		return po.getInventoryNum();
	}

	/*
	 * (non-Javadoc)
	 * @see businesslogic.inventorybl.info.CommodityInfo_Inventory#getAvePrice(java.lang.String)
	 */
	public double getAvePrice(String ID) throws RemoteException {
		if (po == null) {
			po = commodityData.find(ID);
		}
		int totalNumber = po.getSaleNumber() + po.getPurNumber();
		double totalPrice = po.getSalePrice() * po.getSaleNumber() + po.getPurNumber() * po.getAvePur();
		return totalPrice / (totalNumber == 0 ? 1 : totalNumber); // 防止除数为0
	}

	/*
	 * (non-Javadoc)
	 * @see businesslogic.inventorybl.info.CommodityInfo_Inventory#getPurPrice(java.lang.String)
	 */
	public double getPurPrice(String ID) throws RemoteException {
		if (po == null) {
			po = commodityData.find(ID);
		}
		return po.getPurPrice();
	}

	/**
	 * 返回重组的商品PO给期初建账
	 * @throws RemoteException
	 */
	public ArrayList<CommodityPO> getCommodityPOs() throws RemoteException {
		ArrayList<CommodityPO> POs = commodityData.show();
		ArrayList<CommodityPO> returnPOs = new ArrayList<CommodityPO>();
		for(CommodityPO po : POs) {
			CommodityPO returnPO = new CommodityPO(po.getID(), po.getName(), po.getType(), po.getSortID(), po.getAveSale(), po.getAvePur());
			returnPOs.add(returnPO);
		}
		return returnPOs;
	}

	public ArrayList<CommodityVO> getCommodityVOs(ArrayList<CommodityPO> POs) {
		ArrayList<CommodityVO> VOs = new ArrayList<CommodityVO>();
		for(CommodityPO po : POs) {
			CommodityVO vo = POtoVO(po);
			VOs.add(vo);
		}
		return VOs;
	}

	private CommodityVO POtoVO(CommodityPO po) {
		String ID = po.getID();
		String name = po.getName();
		String type = po.getType();
		String sortID = po.getSortID();
		double aveSale = po.getAveSale();
		double avePur = po.getAvePur();
		CommodityVO vo = new CommodityVO(ID, name, type, sortID, aveSale, avePur);
		return vo;
	}

	/**
	 * 更改商品数据，包括库存数量、最近进价、最近售价、平均进价、平均售价、销售数量、进货数量
	 * @throws RemoteException
	 */
	public void changeCommodityInfo(String ID, int number, double price, BillType billType) throws RemoteException {
		CommodityPO po = commodityData.find(ID);
		// 更改PO的数据
		int nowSaleNumber = 0;
		int nowPurNumber = 0;
		switch(billType) {
		case SALE: // 如果是销售单，需要减少库存数量，更改最近售价，更改平均售价，增加销售数量
			int nowCommodityNumber = po.getInventoryNum() - number;
			nowSaleNumber = po.getSaleNumber() + number;
			po.setInventoryNum(nowCommodityNumber);
			// 判断数量是否需要建立报警单
			isAlarm(po, nowCommodityNumber, po.getAlarmNumber());
			po.setRecentSalePrice(price);
			po.setAveSale((po.getAveSale() * po.getSaleNumber() + number * price) / nowSaleNumber);
			po.setSaleNumber(nowSaleNumber);
			break;
		case SALEBACK: // 如果是销售退款单，需要增加库存数量，更改平均售价，减少销售数量
			nowSaleNumber = po.getSaleNumber() - number;
			po.setInventoryNum(po.getInventoryNum() + number);
			po.setAveSale((po.getAveSale() * po.getSaleNumber() - number * price) / nowSaleNumber);
			po.setSaleNumber(nowSaleNumber);
			// 增加退货商品的数量
			po.setSaleBackNumber(po.getSaleBackNumber() + number);
			break;
		case PURCHASE: // 如果是销售退款单，需要增加库存数量，更改平均售价，减少销售数量
			nowPurNumber = po.getPurNumber() + number;
			po.setInventoryNum(po.getInventoryNum() + number);
			po.setRecentPurPrice(price);
			po.setAvePur((po.getAvePur() * po.getPurNumber() + number * price) / nowPurNumber);
			po.setPurNumber(nowPurNumber);
			break;
		case PURCHASEBACK: // 如果是进货退货单，需要减少库存数量，更改平均进价，减少进货数量
			nowPurNumber = po.getPurNumber() - number;
			po.setInventoryNum(po.getInventoryNum() - number);
			po.setAvePur((po.getAvePur() * po.getPurNumber() - number * price) / nowPurNumber);
			po.setPurNumber(nowPurNumber);
			break;
		default:
			break;
		}
		commodityData.update(po);
	}
	
	/** 判断是否数量少于警戒数量 */
	private void isAlarm(CommodityPO po, int now, int alarm) {
		if (now > alarm) {
			return;
		}
		InventoryBLService alarmBill = new InventoryController();
		alarmBill.getAlarmID();
		alarmBill.addCommodity(po.getID(), alarm - now);
		alarmBill.submit(po.getName() + "在销售完毕后库存数量低于警戒数量，需要补充");
	}

	/**
	 * 库存单据（赠送、报损、报溢）
	 * @throws RemoteException
	 */
	public void changeNumber(String ID, int number, BillType billType) throws RemoteException {
		CommodityPO po = commodityData.find(ID);
		switch(billType) {
		case OVERFLOW:
		case LOSS:
			po.setInventoryNum(number);
			break;
		case GIFT:
			int nowNumber = po.getInventoryNum() - number;
			po.setInventoryNum(nowNumber);
			break;
		default:
			break;
		}
		commodityData.update(po);
		}
	
	@Override
	public boolean checkNumber(String ID, int number, BillType billType) throws RemoteException {
		CommodityPO po = commodityData.find(ID);
		switch(billType) {
		case GIFT:
			int nowNumber = po.getInventoryNum() - number;
			if (nowNumber < 0) {
				return false;
			}
			break;
		default:
			break;
		}
		return true;
	}

	/**
	 * 更新商品信息
	 * @throws RemoteException
	 */
	public void setDelete(String ID, boolean canDelete) throws RemoteException {
		CommodityPO po = commodityData.find(ID);
		po.setCanDelete(canDelete);
		commodityData.update(po);
	}

	@Override
	public boolean checkNumber(String ID, int number) throws RemoteException {
		CommodityPO po = commodityData.find(ID);
		if (po.getInventoryNum() < number) {
			return false;
		}
		return true;
	}

}
