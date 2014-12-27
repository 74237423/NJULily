package businesslogic.commoditysortbl;

import java.rmi.RemoteException;
import java.util.ArrayList;

import log.LogController;
import message.ResultMessage;
import vo.commodity.CommoditySortVO;
import blservice.commoditysortblservice.CommoditySortBLService;

/**
 * @see blservice.commoditysortblservice.CommoditySortBLService
 * @author cylong
 * @version 2014年12月14日 下午12:58:06
 */
public class CommoditySortController implements CommoditySortBLService {

	private CommoditySort commoditySort;

	/**
	 * @author cylong
	 * @version 2014年12月14日 下午1:00:53
	 */
	public CommoditySortController() {
		commoditySort = new CommoditySort();
	}

	/**
	 * @see blservice.commoditysortblservice.CommoditySortBLService#show()
	 */
	@Override
	public ArrayList<CommoditySortVO> show() {
		try {
			return commoditySort.show();
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @see blservice.commoditysortblservice.CommoditySortBLService#show(java.lang.String)
	 */
	@Override
	public CommoditySortVO show(String ID) {
		try {
			LogController.addLog("根据商品分类ID查看分类，ID＝" + ID);
			return commoditySort.show(ID);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @see blservice.commoditysortblservice.CommoditySortBLService#getID(java.lang.String)
	 */
	@Override
	public String getID(String fatherID) {
		try {
			return commoditySort.getID(fatherID);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @see blservice.commoditysortblservice.CommoditySortBLService#addCommoSort(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public ResultMessage addCommoSort(String sortName, String fatherID) {
		try {
			ResultMessage res = commoditySort.addCommoSort(sortName, fatherID);
			if (!res.equals(ResultMessage.SUCCESS)) {
				return res;
			}
			LogController.addLog("添加商品分类：商品分类" + sortName);
			return res;
		} catch (RemoteException e) {
			e.printStackTrace();
			return ResultMessage.REMOTE_EXCEPTION;
		}
	}

	/**
	 * @see blservice.commoditysortblservice.CommoditySortBLService#deleteCommoSort(java.lang.String)
	 */
	@Override
	public ResultMessage deleteCommoSort(String ID) {
		try {
			ResultMessage res = commoditySort.deleteCommoSort(ID);
			if (!res.equals(ResultMessage.SUCCESS)) {
				return res;
			}
			LogController.addLog("删除商品分类，分类ID＝" + ID);
			return res;
		} catch (RemoteException e) {
			e.printStackTrace();
			return ResultMessage.REMOTE_EXCEPTION;
		}
	}

	/**
	 * @see blservice.commoditysortblservice.CommoditySortBLService#updCommoSort(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public ResultMessage updCommoSort(String ID, String name) {
		try {
			ResultMessage res = commoditySort.updCommoSort(ID, name);
			if (!res.equals(ResultMessage.SUCCESS)) {
				return res;
			}
			LogController.addLog("更新商品分类，分类ID＝" + ID);
			return res;
		} catch (RemoteException e) {
			e.printStackTrace();
			return ResultMessage.REMOTE_EXCEPTION;
		}
	}

}
