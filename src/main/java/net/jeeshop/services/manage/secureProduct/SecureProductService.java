package net.jeeshop.services.manage.secureProduct;import java.util.List;import net.jeeshop.core.Services;import net.jeeshop.services.manage.secureProduct.bean.SecureProduct;import net.jeeshop.services.manage.secureProduct.bean.SecureProductDetail;public interface SecureProductService extends Services<SecureProduct> {	/**	 * 商品上架/下架	 * @param ids	商品ID集合	 * @param	status	状态	 */	void updateProductStatus(String[] ids,int status,String updateAccount);		/**	 * 根据商品ID查询商品库存集合	 * @param productIDs	 * @return	 */	List<SecureProductDetail> selectSecureProductDetail(String id);	List<SecureProduct> selectStockByIDs(List<String> productIDs);	int selectOutOfStockProductCount();	//保存保险产品	int insertSecureProduct(SecureProduct p);	//保存保险子产品	int insertSecureProductDetail(SecureProductDetail p);	//更新保险子产品	int updateSecureProductDetail(SecureProductDetail p);		void updateImg(SecureProduct p);	/**	 * 更新商品表，绑定活动ID	 */	void updateProductBindActivityId(SecureProduct pro);	/**	 * 更新商品表，清除某个已绑定的商品的活动ID	 * @param activityID	 */	void updateResetThisProductActivityID(String activityID);/** * 删除商品，把商品标志位置1 * @param activityID */	void deletes(String[] ids, int deleteFlagY, String username);		/**	 * 根据用户编号查询所绑定的商品	 * @param uid	 * @return	 */	List<SecureProduct>  getAllProductsByUserId(String uid);		/**	 * 查询所有商品	 * @return	 */	List<SecureProduct>  selectProductList(); 		/**	 * 根据商品名称模糊查询	 * @return	 */	List<SecureProduct> queryProductList(SecureProduct e);		/**	* @Description: TODO(给用户绑定产品) 	* @author lyx	* @date 2016年9月21日 下午4:50:39 	* @throws	 */	int bindUserProduct(String ids,String uid);}