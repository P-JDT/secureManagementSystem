package net.jeeshop.web.action.manage.secureProduct;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;

import net.jeeshop.core.ManageContainer;
import net.jeeshop.core.system.bean.User;
import net.jeeshop.services.manage.order.bean.Order;
import net.jeeshop.services.manage.secureProduct.SecureProductService;
import net.jeeshop.services.manage.secureProduct.bean.SecureProduct;
import net.jeeshop.services.manage.secureProduct.bean.SecureProductDetail;
import net.jeeshop.services.manage.system.impl.UserService;
import net.jeeshop.web.action.BaseController;
import net.jeeshop.web.util.LoginUserHolder;
import net.jeeshop.web.util.RequestHolder;

/**
 * 商品信息管理
 * 
 * @author jqsl2012@163.com
 * @author dylan
 * 
 */
@Controller
@RequestMapping("/manage/secureProduct/")
public class SecureProductAction extends BaseController<SecureProduct> {
	private static final Logger logger = LoggerFactory.getLogger(SecureProductAction.class);
	private static final long serialVersionUID = 1L;
	@Autowired
	private SecureProductService secureProductService;
	@Autowired
	private UserService userService;
	private static final String page_toOrderPage = "/manage/order/orderPage";
	private static final String page_toList = "/manage/secureProduct/secureProductList";
	private static final String page_toListAdmin = "/manage/secureProduct/admin/secureProductListAdmin";
	//添加子产品页面
	private static final String page_toSubAdd = "/manage/secureProduct/admin/secureProductDetailAdd";
	//修改子产品页面
	private static final String page_toSubEdit = "/manage/secureProduct/admin/secureProductDetailAdd";
	//修改主产品
	private static final String page_toEdit = "/manage/secureProduct/admin/secureProductEdit";
	//添加主产品
	private static final String page_toAdd = "/manage/secureProduct/admin/secureProductAdd";
	private static final String page_toOrders = "/manage/secureProduct/secureProductOrders";
	private static final String selectListTap = "/manage/userProduct/secureProductListTap";
	
	private static final String showBindProduct = "/manage/userProduct/secureProductListTap";
	private static final String showAllProduct = "/manage/userProduct/allSecureProductList";

	private SecureProductAction() {
		super.page_toList = page_toList;
		super.page_toAdd = page_toAdd;
		super.page_toEdit = page_toEdit;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public SecureProductService getService() {
		return secureProductService;
	}
	public void setSecureProductService(SecureProductService secureProductService) {
		this.secureProductService = secureProductService;
	}
	
	/**
	 * 跳到支付页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("toOrderPage")
	public String toOrderPage(Order e, ModelMap model) throws Exception {		
		return page_toOrderPage;
	}

	/**
	 * 添加商品
	 */
	@Override
	public String toAdd(@ModelAttribute("e") SecureProduct e, ModelMap model) throws Exception {
		return page_toAdd;
	}
	
	//列表页面点击 编辑商品
	@RequestMapping(value = "toOrder")
	public String toOrder(@ModelAttribute("e")SecureProduct e,ModelMap model) throws Exception {
		//RequestHolder.getRequest().setAttribute("catalogs", systemManager.getCatalogs());
//		getSession().setAttribute("insertOrUpdateMsg", "");
		e = getService().selectById(e.getId());
		List<SecureProductDetail> secureProductDetail=getService().selectSecureProductDetail(e.getId());
		if(e==null || StringUtils.isBlank(e.getId())){
			throw new NullPointerException("根据商品ID查询不到指定的产品！");
		}
		RequestHolder.getRequest().setAttribute("secureProductDetail", secureProductDetail);
		model.addAttribute("e", e);
		return page_toOrders;
		
	}
	//列表页面点击 编辑商品
	@Override
	public String toEdit(@ModelAttribute("e")SecureProduct e, ModelMap model) throws Exception {
//		getSession().setAttribute("insertOrUpdateMsg", "");
		return toEdit0(e, model);
	}

	/**
	 * 修改商品的类别，会联动清除商品已有的属性和参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "updateProductCatalog", method = RequestMethod.POST)
	public String updateProductCatalog(SecureProduct e, ModelMap model) throws Exception {
//		getSession().setAttribute("insertOrUpdateMsg", "");
		return toEdit0(e, model);
	}
	/**
	 * 修改子产品
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "toEditSubProduct", method = RequestMethod.POST)
	public String toEditSubProduct(SecureProduct e, ModelMap model) throws Exception {
		if(StringUtils.isBlank(e.getId())){
			throw new NullPointerException("产品ID不能为空！");
		}
		
		List <SecureProductDetail> secureProductDetailList= getService().selectSecureProductDetail(e.getId());
		if(e==null || StringUtils.isBlank(e.getId())){
			throw new NullPointerException("根据ID查询不到指定的产品！");
		}
		model.addAttribute("e", e);
		return page_toSubEdit;
	}	
	/**
	 * 添加或编辑商品后程序回转编辑
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "toEdit2")
	public String toEdit2(SecureProduct e, ModelMap model) throws Exception {
		return toEdit0(e, model);
	}
	/**
	 * 添加或编辑商品后程序回转编辑
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "toEditProduct")
	public String toEditProduct(SecureProduct e, ModelMap model) throws Exception {
		return toEdit0(e, model);
	}
	

	/**
	 * 根据商品ID，加载商品全部信息
	 */
	private String toEdit0(SecureProduct e, ModelMap model) throws Exception {
        //RequestHolder.getRequest().setAttribute("catalogs", systemManager.getCatalogs());
		if(StringUtils.isBlank(e.getId())){
			throw new NullPointerException("商品ID不能为空！");
		}
		
		e = getService().selectById(e.getId());
		if(e==null || StringUtils.isBlank(e.getId())){
			throw new NullPointerException("根据商品ID查询不到指定的商品！");
		}
		model.addAttribute("e", e);
		return page_toEdit;
	}

	//分页查询商品
	@Override
	public String selectList(HttpServletRequest request,@ModelAttribute("e") SecureProduct e) throws Exception {
		try {
//            RequestHolder.getRequest().setAttribute("catalogs", systemManager.getCatalogs());
//			e.setQueryCatalogIDs(SystemManager.getInstance().getCatalogsById(e.getCatalogID()));
			User user = LoginUserHolder.getLoginUser();
			
			if(user.getRid().equals("1")||user.getRid().equals("2")){
				super.selectList(request, e);
				return page_toListAdmin;
			}else{
				e.setUid(user.getId());
				super.selectList(request, e);
				return page_toList;
			}
		
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
	
	@Override
	protected void setParamWhenInitQuery(SecureProduct e) {
		super.setParamWhenInitQuery(e);
		String selectOutOfStockProduct = RequestHolder.getRequest().getParameter("selectOutOfStockProduct");
		if(StringUtils.isNotBlank(selectOutOfStockProduct)){
			//后台--首页 需要查询缺货商品
			e.setSelectOutOfStockProduct(Boolean.valueOf(selectOutOfStockProduct));
		}
	}

	/**
	 * ajax查询指定商品的图片集合
	 * @return
	 */
	@Deprecated
	@RequestMapping("ajaxLoadImgList")
	@ResponseBody
	public String ajaxLoadImgList(){
		String id = RequestHolder.getRequest().getParameter("id");
		String path = RequestHolder.getRequest().getSession().getServletContext().getRealPath("/");
		System.out.println("path=" + path);
//		path = path.substring(0, path.indexOf("WEB-INF"));
//		System.out.println("path=" + path);
		path = path+"/upload/"+id+"/";
		System.out.println("path=" + path);
		
		File dir = new File(path);
		File[] fiels = dir.listFiles();
		List<String> fileList = new LinkedList<String>();
		if(fiels!=null && fiels.length>0){
			String www_address = systemManager.getProperty("www_address");
            for (int i=0;i<fiels.length;i++){
				fileList.add(www_address + "/upload/"+id+"/"+fiels[i].getName());
			}
		}
		String json = JSON.toJSONString(fileList);
		System.out.println(json);
		try {
			return (json);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 *  添加产品
	 */
	@RequestMapping(value = "insertSecureProduct", method = RequestMethod.POST)
	public String insertSecureProduct(HttpServletRequest request, SecureProduct e, ModelMap model) throws Exception {
		logger.error(">>>insert secureProduct...");		
		e.setStatus(1);
		e.setDeleteFlag(0);
		e.setCreateAccount(getAccount());
		int pid=getService().insertSecureProduct(e);
		//request.getSession().setAttribute("pid",pid);	
		model.addAttribute("e", e);
		return page_toSubAdd;
	}
	/**
	 *  跳转到添加子产品页面
	 */
	public String toAddSubProduct(@ModelAttribute("e") SecureProduct e, ModelMap model){
		return page_toSubAdd;
	}
	/**
	 * 更新产品
	 */
	public String updateSubProduct(HttpServletRequest request, SecureProductDetail e) throws Exception {
		e.setUpdateAccount(getAccount());
		getService().updateSecureProductDetail(e);
		return null;
	}
	
	/**
	 *  添加子产品toAddSubProduct
	 */
	@RequestMapping(value = "insertSubProduct", method = RequestMethod.POST)
	public String insertSubProduct(HttpServletRequest request, SecureProductDetail e) throws Exception {
		String pid = RequestHolder.getRequest().getParameter("pid");
		logger.error(">>>insert secureProductDetail...");
		e.setPid(pid);
		e.setCreateAccount(getAccount());
		int id=getService().insertSecureProductDetail(e);
		//处理子产品数据
//		SecureProductDetail f=new SecureProductDetail();
//		if(e.getSpecList()!=null && e.getSpecList().size()>0){
//			for(int i=0;i<e.getSpecList().size();i++){
//				logger.error("=======insertOrUpdateSpec.e.getSpecArray() = " + e.getSpecList().get(i));
//				Spec spec = e.getSpecList().get(i);
//				
//				if(StringUtils.isBlank(spec.getSpecColor())){
//					continue;
//				}
//				
//				spec.setProductID(e.getId());
//				if(StringUtils.isBlank(spec.getId())){
//					specService.insert(spec);
//				}else{
//					specService.update(spec);
//				}
//			}
//		}else{
//			logger.error("=======insertOrUpdateSpec.e.getSpecArray() is null");
//		}
		return page_toAdd;
	}

	//获取后台管理人员的账号
	private String getAccount(){
		User user = LoginUserHolder.getLoginUser();
		if(user==null){
			throw new NullPointerException("登陆超时！");
		}
		return user.getUsername();
	}
	
	/**
	 * 更新产品
	 */
	@Override
	public String update(HttpServletRequest request, SecureProduct e, RedirectAttributes flushAttrs) throws Exception {
		e.setUpdateAccount(getAccount());
		getService().update(e);
		return "redirect:toEdit2?id="+ e.getId();
	}
	/**
     * 公共的批量删除数据的方法，子类可以通过重写此方法实现个性化的需求。
     *
     * @return
     * @throws Exception
     */
	@Override	    
    public String deletes(HttpServletRequest request, String[] ids, SecureProduct e, RedirectAttributes flushAttrs) throws Exception {	    	
		User user = LoginUserHolder.getLoginUser();
		
		getService().deletes(ids,SecureProduct.Delete_flag_y,user.getUsername());
        addMessage(flushAttrs, "操作成功！");
        return "redirect:selectList";
    }


	/**
	 * 上架指定商品
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "updateUpProduct", method = RequestMethod.POST)
	public String updateUpProduct(@ModelAttribute("e")SecureProduct e) throws Exception{
		if(StringUtils.isBlank(e.getId())){
			throw new NullPointerException();
		}
		
		User user = LoginUserHolder.getLoginUser();
		secureProductService.updateProductStatus(new String[]{e.getId()},SecureProduct.Product_status_y,user.getUsername());
//		getSession().setAttribute("insertOrUpdateMsg", "上架成功！");
//		getResponse().sendRedirect(getEditUrl(e.getId()));
		return "redirect:toEdit2?id="+e.getId();
	}
	
	/**
	 * 下架指定商品
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "updateDownProduct", method = RequestMethod.POST)
	public String updateDownProduct(@ModelAttribute("e")SecureProduct e) throws Exception{
		if(StringUtils.isBlank(e.getId())){
			throw new NullPointerException();
		}

		User user = LoginUserHolder.getLoginUser();
		secureProductService.updateProductStatus(new String[]{e.getId()},SecureProduct.Product_status_n,user.getUsername());
//		getSession().setAttribute("insertOrUpdateMsg", "下架成功！");
//		getResponse().sendRedirect(getEditUrl(e.getId()));
		return "redirect:toEdit2?id="+e.getId();
	}
	
	/**
	 * 商品上架
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "updateUp", method = RequestMethod.POST)
	public String updateUp(@ModelAttribute("e")SecureProduct e,String[] ids) throws Exception{		
//		String[] ids = e.getId().split(",");
//		 List<String> tmp = new ArrayList<String>();
//	        for(String str:ids){
//	            if(str!=null && str.length()!=0){
//	                tmp.add(str);
//	            }
//	        }
//	        ids = tmp.toArray(new String[0]);
		updateStatus(ids,SecureProduct.Product_status_y);
		return "redirect:selectList";
		//return selectList(RequestHolder.getRequest(), new SecureProduct());
	}
	
	/**
	 * 商品下架
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "updateDown", method = RequestMethod.POST)
	public String updateDown(@ModelAttribute("e")SecureProduct e,String[] ids) throws Exception{
//		String[] ids = e.getId().split(",");
//		
//		 List<String> tmp = new ArrayList<String>();
//	        for(String str:ids){
//	            if(str!=null && str.length()!=0){
//	                tmp.add(str);
//	            }
//	        }
//	        ids = tmp.toArray(new String[0]);
		updateStatus(ids, SecureProduct.Product_status_n);
		return selectList(RequestHolder.getRequest(), new SecureProduct());
	}
	
	private void updateStatus(String[] ids, int status){
		User user = LoginUserHolder.getLoginUser();
		secureProductService.updateProductStatus(ids,status,user.getUsername());
	}
	
	/**
	 * 根据选择的商品图片名称来删除商品图片
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "deleteImageByImgPaths", method = RequestMethod.POST)
	public String deleteImageByImgPaths(@ModelAttribute("e")SecureProduct e, String[] imagePaths) throws IOException{
		String id = e.getId();
		if(imagePaths!=null & imagePaths.length>0){
			SecureProduct ee = secureProductService.selectById(id);
			if(StringUtils.isNotBlank(ee.getImages())){
				String[] images = ee.getImages().split(ManageContainer.product_images_spider);
				//和该商品的图片集合比对，找出不删除的图片然后保存到库
				for(int i=0;i<imagePaths.length;i++){
					for(int j=0;j<images.length;j++){
						if(imagePaths[i].equals(images[j])){
							images[j] = null;
							break;
						}
					}
					imagePaths[i] = null;
				}
				StringBuilder buff = new StringBuilder();
				for(int j=0;j<images.length;j++){
					if(images[j]!=null){
						buff.append(images[j]+",");
					}
				}
				ee.clear();
				ee.setId(id);
				ee.setImages(buff.toString());
				if(ee.getImages().equals("")){
					ee.setImages(ManageContainer.product_images_spider);//全部删除了
				}
				secureProductService.update(ee);
			}
			imagePaths = null;
		}
//		getResponse().sendRedirect(getEditUrl(id));
		return "redirect:toEdit2?id="+id;
	}

	

	
	/**
	 * 把所有商品的大图更新为小图
	 * @return
	 */
	public String test10() {
		logger.error("test10...");
		List<SecureProduct> list =secureProductService.selectList(new SecureProduct());
		for(int i=0;i<list.size();i++){
			SecureProduct pp = list.get(i);
			String img = pp.getPicture();
			if(StringUtils.isBlank(img)){
				continue;
			}
			String[] arr = img.split("_");
			if(arr.length==2){
				String fx = img.substring(img.lastIndexOf("."));
				SecureProduct p = new SecureProduct();
				p.setId(pp.getId());
				p.setPicture(arr[0]+"_1"+fx);
				
				if(pp.getIsnew().toString().equals("0")){
					p.setIsnew(SecureProduct.Product_isnew_n);
				}else{
					p.setIsnew(SecureProduct.Product_isnew_y);
				}
				
				if(pp.getSale().toString().equals("0")){
					p.setSale(SecureProduct.Product_sale_n);
				}else{
					p.setSale(SecureProduct.Product_sale_y);
				}
				
				logger.error("p.getPicture = " + p.getPicture());
				secureProductService.updateImg(p);				
//				throw new NullPointerException();
			}
		}
		
		return null;
	}
	
	/**
	 * @description 根据用户编号获取该业务员所有产品列表
	 * @author lin
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("productList")
	public String productList(HttpServletRequest request, ModelMap model) throws Exception {
		// 用户编号
		String uid = request.getParameter("uid");
		// 用户名存入session 供显示用
		if (request.getParameter("uname") != null) {
			RequestHolder.getSession().setAttribute("userName", request.getParameter("uname"));
			RequestHolder.getSession().setAttribute("uid", uid);
		}
		List<SecureProduct> l = secureProductService.getAllProductsByUserId(uid);
		request.setAttribute("product", l);
		return showBindProduct;
	}

	/**
	 * 查询所有商品 标记已绑定的
	 * 
	 * @param request
	 * @param ids
	 *            已绑定的产品编号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getAllProduct")
	public String getAllProduct(HttpServletRequest request, String ids, @ModelAttribute("e") SecureProduct e)
			throws Exception {
		try {
			HttpSession sess = RequestHolder.getSession();
			List productList = secureProductService.selectProductList();
			String repeatIds;
			if (ids != null && ids != "") {
				repeatIds = ids.substring(1, ids.length());
			} else {
				repeatIds = "";
			}
			if (sess.getAttribute(repeatIds) != null)
				sess.removeAttribute("repeatIds");
			sess.setAttribute("repeatIds", repeatIds);
			request.setAttribute("productList", productList);

			return showAllProduct;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 *@Description: TODO(模糊查询)
	 *@author lyx 
	 *@date 2016年9月21日 下午3:29:09
	 *@return String 
	 */
	@RequestMapping("queryList")
	public String queryList(HttpServletRequest request, @ModelAttribute("e") SecureProduct e) throws Exception {
		try {
			String qName = request.getParameter("qName");
			e.setName(qName);
			List<SecureProduct> productList = secureProductService.queryProductList(e);
			request.setAttribute("productList", productList);
			return showAllProduct;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * @Description: TODO(绑定) 
	 * @author lyx
	 * @date 2016年9月21日 下午4:10:01 
	 * @return String 返回类型 @throws
	 */
	@RequestMapping("bindUserProduct")
	public String bindUserProduct(HttpServletRequest request, @ModelAttribute("e") SecureProduct e ) {
		String userId = "";
		try {
			String ids = request.getParameter("ids");
			String uid ="";
			userId = RequestHolder.getSession().getAttribute("uid").toString();//获取当前用户名
			if(RequestHolder.getSession().getAttribute("userName")!=null){
				 uid = RequestHolder.getSession().getAttribute("uid").toString();
			}
			int i = secureProductService.bindUserProduct(ids,uid);
			logger.info("共插入了"+i+"条记录");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		List<SecureProduct> l = secureProductService.getAllProductsByUserId(userId);
		request.setAttribute("product", l);
		return showBindProduct;
	}
}
