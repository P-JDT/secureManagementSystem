package net.jeeshop.services.manage.secureProduct.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import net.jeeshop.core.dao.BaseDao;
import net.jeeshop.core.dao.page.PagerModel;
import net.jeeshop.core.util.MathUtil;
import net.jeeshop.services.common.userProduct;
import net.jeeshop.services.manage.secureProduct.bean.SecureProduct;
import net.jeeshop.services.manage.secureProduct.dao.SecureProductDao;

@Repository("secureProductDaoManage")
public class SecureProductDaoImpl implements SecureProductDao {
	@Resource
	private BaseDao dao;

	public void setDao(BaseDao dao) {
		this.dao = dao;
	}

	public PagerModel selectPageList(SecureProduct e) {
		return dao.selectPageList("manage.secureProduct.selectPageList", "manage.secureProduct.selectPageCount", e);
	}

	public List selectList(SecureProduct e) {
		return dao.selectList("manage.secureProduct.selectList", e);
	}

	public SecureProduct selectOne(SecureProduct e) {
		return (SecureProduct) dao.selectOne("manage.secureProduct.selectOne", e);
	}

	public int delete(SecureProduct e) {
		return dao.delete("manage.secureProduct.delete", e);
	}

	public int update(SecureProduct e) {
		return dao.update("manage.secureProduct.update", e);
	}

	public int deletes(String[] ids, int delete_flag, String updateAccount) {

		SecureProduct e = new SecureProduct();
		for (int i = 0; i < ids.length; i++) {
			e.setId(ids[i]);
			e.setDelete_flag(1);
			e.setUpdateAccount(updateAccount);
			delete(e);
		}
		return 0;
	}

	public int insert(SecureProduct e) {
		return dao.insert("manage.secureProduct.insert", e);
	}

	public int deleteById(int id) {
		return dao.delete("manage.secureProduct.deleteById", id);
	}

	public SecureProduct selectById(String id) {
		return (SecureProduct) dao.selectOne("manage.secureProduct.selectById", id);
	}

	@Override
	public void deleteAttributeLinkByProductID(int parseInt) {
		dao.delete("manage.secureProduct.deleteAttributeLinkByProductID", parseInt);
	}

	@Override
	public List<SecureProduct> selectStockByIDs(List<String> productIDs) {
		return dao.selectList("manage.secureProduct.selectStockByIDs", productIDs);
	}

	@Override
	public int selectOutOfStockProductCount() {
		return (Integer) dao.selectOne("manage.secureProduct.selectOutOfStockProductCount");
	}

	@Override
	public void updateImg(SecureProduct p) {
		dao.update("manage.secureProduct.updateImg", p);
	}

	@Override
	public void updateProductStatus(SecureProduct p) {
		dao.update("manage.secureProduct.updateProductStatus", p);
	}

	@Override
	public void updateProductBindActivityId(SecureProduct pro) {
		dao.update("manage.secureProduct.updateProductBindActivityId", pro);
	}

	@Override
	public void updateResetThisProductActivityID(String activityID) {
		dao.update("manage.secureProduct.updateResetThisProductActivityID", activityID);
	}

	@Override
	public List getAllProductsByUserId(String uid) {
		return dao.selectList("manage.secureProduct.getAllProductsByUserId", uid);
	}

	@Override
	public List<SecureProduct> selectProductList() {
		return dao.selectList("manage.secureProduct.getAllProducts");
	}

	@Override
	public List<SecureProduct> queryProductList(SecureProduct e) {
		return dao.selectList("manage.secureProduct.getAllProducts", e.getName());
	}

	@Override
	public void deleteUsersBind(String uid) {
		dao.delete("manage.secureProduct.deleteUsersProduct", uid);
	}

	@Override
	public int addUsersBind(String ids, String uid) {
		String arr[] = ids.substring(1, ids.length()).split(",");
		int k = 0;
		for (int i = 0; i < arr.length; i++) {
			userProduct up = new userProduct();
			up.setId(MathUtil.getUUID());
			up.setUid(uid);
			up.setPid(arr[i]);
			try {
				dao.insertByUUID("manage.secureProduct.insertUsersProduct", up);
			} catch (Exception e) {
				e.printStackTrace();
			}
			k = i;
		}
		return k;
	}
}
