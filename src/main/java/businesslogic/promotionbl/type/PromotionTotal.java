package businesslogic.promotionbl.type;

import java.util.ArrayList;

import dataenum.PromotionType;
import po.CommodityItemPO;
import po.PromotionPO;
import message.ResultMessage;
import vo.commodity.CommodityItemVO;
import vo.promotion.PromotionTotalVO;
import blservice.promotionblservice.PromoInputInfo;
import blservice.promotionblservice.PromotionTotalBLService;
import businesslogic.promotionbl.Promotion;
import businesslogic.promotionbl.PromotionListItem;
import businesslogic.promotionbl.PromotionTrans;

/**
 * 计算总价的促销策略
 * @author Zing
 * @version Dec 9, 2014 5:35:05 PM
 */
public class PromotionTotal extends Promotion implements PromotionTotalBLService{

	public PromotionTotal() {
		super();
	}
	public ArrayList<PromotionTotalVO> show() {
		PromotionTrans transPOVO = new PromotionTrans();
		return transPOVO.totalPOtoVO(promotionData.show(PromotionType.TOTAL));
	}
	/**
	 * 设置可以进行促销的总价
	 * @param total
	 * @author Zing
	 * @version Dec 9, 2014 7:10:39 PM
	 */
	public void setTotal(double total) {
		list.setTotal(total);
	}

	public void addGifts(CommodityItemVO vo) {
		PromotionListItem item = new PromotionListItem(vo.ID, vo.number);
		list.addGift(item);
	}

	public ResultMessage submit(PromoInputInfo info) {
		setInputInfo(info);
		PromotionPO po = buildPromotion();
 		return promotionData.insert(po);
	}
	
	private PromotionPO buildPromotion() {
		double total = list.getTotal();
		ArrayList<CommodityItemPO> gifts = list.getGifts();
		double allowance = list.getAllowance();
		int voucher = list.getVoucher();
		PromotionPO po = new PromotionPO(ID, beginDate, endDate, null, 0, null, null, total, gifts, allowance, voucher);
		return po;
	}

	private void setInputInfo(PromoInputInfo info) {
		beginDate = info.beginDate;
		endDate = info.endDate;
		list.setAllowance(info.allowance);
		list.setVoucher(info.voucher);
	}

}