package net.jeeshop.services.manage.secureOrder.impl;import javax.annotation.Resource;import org.springframework.stereotype.Service;import net.jeeshop.core.ServersManager;import net.jeeshop.services.manage.secureOrder.SecureOrderService;import net.jeeshop.services.manage.secureOrder.bean.SecureOrder;import net.jeeshop.services.manage.secureOrder.dao.SecureOrderDao;/** * 保险订单管理 * @author lin */@Service("secureOrderServiceManage")public class SecureOrderServiceImpl extends ServersManager<SecureOrder, SecureOrderDao> implements		SecureOrderService {	 @Override	 @Resource(name = "secureOrderDaoManage")	public void setDao(SecureOrderDao secureOrderdao) {		this.dao = secureOrderdao;	}}