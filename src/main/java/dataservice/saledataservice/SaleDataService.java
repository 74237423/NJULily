package dataservice.saledataservice;

import po.SalesPO;
import dataenum.BillType;
import dataservice.CommonDataService;
import dataservice.TableInfoService;

/**
 * 提供销售（销售退货）数据集体载入、保存、增加、删除、查找
 * @author cylong
 * @version Oct 26, 2014 3:52:59 PM
 */
public interface SaleDataService extends CommonDataService<SalesPO> {

	/**
	 * 按照是销售单还是销售退货单返回ID
	 * @param type
	 * @return 销售单或者销售退货单ID
	 * @author cylong
	 * @version 2014年11月28日 下午12:11:03
	 */
	public String getID(BillType type);

	/**
	 * @return SaleInfoService 的实例
	 * @author cylong
	 * @version 2014年12月2日 上午2:43:12
	 */
	public TableInfoService<SalesPO> getInfo();
}