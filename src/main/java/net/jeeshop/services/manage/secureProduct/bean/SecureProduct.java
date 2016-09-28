package net.jeeshop.services.manage.secureProduct.bean;import java.io.Serializable;import java.util.List;import net.jeeshop.services.manage.attribute.bean.Attribute;import net.jeeshop.services.manage.spec.bean.Spec;/** * 商品 *  * @author huangf *  */public class SecureProduct extends net.jeeshop.services.common.Product implements Serializable {	private static final long serialVersionUID = 1L;	private String remark;// 备注		private int deleteFlag;// 删除标志	private String uid;// 业务员id		private String currency;//币种	private String deductible;//免责说明	private String amounts;//总保险金额	private String premiums;//总保险费	private String appointment;//特别约定	private String insuranceClause;//保险条款	private String introduce;// 商品简介	private String startDate;//开始时间	private String endDate;//结束时间	private List<SecureProductDetail> secureProductDetailList;//商品图片列表		private String picture_url;// 图片地址	private int top;// 促销前10个	private String endtime;// 查询录入的结束时间	private String ids;// where id in (1,2,3);	private List<Attribute> attrList;//商品属性集合	private List<Attribute> parameterList;//商品参数集合	private String[] attrSelectIds;//选中的商品属性ID集合	private String[] parameterNames;//商品参数名称列表	private String[] parameterIds;//商品参数id列表	private List<String> imagesList;//商品图片列表		private List<String> queryCatalogIDs;//查询的类别ID集合	public static final int Product_status_y = 1;//已上架	public static final int Product_status_n = 2;//已下架	public static final int Delete_flag_y = 0;//未删除	public static final int Delete_flag_n = 1;//已删除	private boolean selectOutOfStockProduct;//true:同时查询缺货商品	private List<String> productIds;//商品ID集合	private List<Spec> specList;//商品规格列表//	private Spec[] specArray;//保存的		public void clear() {		super.clear();		top = 0;		endtime = null;		ids = null;				if(attrList!=null && attrList.size()>0){			for(int i=0;i<attrList.size();i++){				attrList.get(i).clear();			}			attrList.clear();		}		if(parameterList!=null && parameterList.size()>0){			for(int i=0;i<parameterList.size();i++){				parameterList.get(i).clear();			}			parameterList.clear();		}				super.clearArray(attrSelectIds);		super.clearArray(parameterNames);		super.clearArray(parameterIds);		super.clearList(imagesList);		super.clearList(queryCatalogIDs);		super.clearList(productIds);		this.selectOutOfStockProduct = false;				if(specList!=null){			for(int i=0;i<specList.size();i++){				specList.get(i).clear();			}			specList.clear();			specList = null;		}//		specArray = null;	}		public String getStartDate() {		return startDate;	}	public void setStartDate(String startDate) {		this.startDate = startDate;	}	public String getEndDate() {		return endDate;	}	public void setEndDate(String endDate) {		this.endDate = endDate;	}	public String getIntroduce() {		return introduce;	}	public void setIntroduce(String introduce) {		this.introduce = introduce;	}	public List<SecureProductDetail> getSecureProductDetailList() {		return secureProductDetailList;	}	public void setSecureProductDetailList(List<SecureProductDetail> secureProductDetailList) {		this.secureProductDetailList = secureProductDetailList;	}	public String getCurrency() {		return currency;	}	public void setCurrency(String currency) {		this.currency = currency;	}	public String getDeductible() {		return deductible;	}	public void setDeductible(String deductible) {		this.deductible = deductible;	}	public String getAmounts() {		return amounts;	}	public void setAmounts(String amounts) {		this.amounts = amounts;	}	public String getPremiums() {		return premiums;	}	public void setPremiums(String premiums) {		this.premiums = premiums;	}	public String getAppointment() {		return appointment;	}	public void setAppointment(String appointment) {		this.appointment = appointment;	}	public String getInsuranceClause() {		return insuranceClause;	}	public void setInsuranceClause(String insuranceClause) {		this.insuranceClause = insuranceClause;	}	public String getPicture_url() {		return picture_url;	}	public void setPicture_url(String picture_url) {		this.picture_url = picture_url;	}	public String getUid() {		return uid;	}	public void setUid(String uid) {		this.uid = uid;	}		public int getDeleteFlag() {		return deleteFlag;	}	public void setDeleteFlag(int deleteFlag) {		this.deleteFlag = deleteFlag;	}	public String getRemark() {		return remark;	}	public void setRemark(String remark) {		this.remark = remark;	}	public String[] getAttrSelectIds() {		return attrSelectIds;	}	public void setAttrSelectIds(String[] attrSelectIds) {		this.attrSelectIds = attrSelectIds;	}	public String[] getParameterNames() {		return parameterNames;	}	public void setParameterNames(String[] parameterNames) {		this.parameterNames = parameterNames;	}	public List<Attribute> getAttrList() {		return attrList;	}	public void setAttrList(List<Attribute> attrList) {		this.attrList = attrList;	}	public List<Attribute> getParameterList() {		return parameterList;	}	public void setParameterList(List<Attribute> parameterList) {		this.parameterList = parameterList;	}	public String getIds() {		return ids;	}	public void setIds(String ids) {		this.ids = ids;	}	public String getEndtime() {		return endtime;	}	public void setEndtime(String endtime) {		this.endtime = endtime;	}	public int getTop() {		return top;	}	public void setTop(int top) {		this.top = top;	}	public String[] getParameterIds() {		return parameterIds;	}	public void setParameterIds(String[] parameterIds) {		this.parameterIds = parameterIds;	}	public List<String> getQueryCatalogIDs() {		return queryCatalogIDs;	}	public void setQueryCatalogIDs(List<String> queryCatalogIDs) {		this.queryCatalogIDs = queryCatalogIDs;	}	public List<String> getImagesList() {		return imagesList;	}	public void setImagesList(List<String> imagesList) {		this.imagesList = imagesList;	}	public boolean isSelectOutOfStockProduct() {		return selectOutOfStockProduct;	}	public void setSelectOutOfStockProduct(boolean selectOutOfStockProduct) {		this.selectOutOfStockProduct = selectOutOfStockProduct;	}	public List<String> getProductIds() {		return productIds;	}	public void setProductIds(List<String> productIds) {		this.productIds = productIds;	}	public List<Spec> getSpecList() {		return specList;	}	public void setSpecList(List<Spec> specList) {		this.specList = specList;	}//	public Spec[] getSpecArray() {//		return specArray;//	}////	public void setSpecArray(Spec[] specArray) {//		this.specArray = specArray;//	}}