package net.jeeshop.web.action.manage.secureProduct;import java.util.HashMap;import java.util.Iterator;import java.util.LinkedHashMap;import java.util.List;import java.util.Map;import javax.servlet.http.HttpServletRequest;import javax.servlet.http.HttpServletResponse;import javax.servlet.http.HttpSession;import org.apache.commons.httpclient.HttpClient;import org.apache.commons.httpclient.methods.PostMethod;import org.apache.commons.httpclient.params.HttpMethodParams;import org.apache.commons.lang.StringUtils;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Controller;import org.springframework.ui.ModelMap;import org.springframework.web.bind.annotation.ModelAttribute;import org.springframework.web.bind.annotation.RequestMapping;import org.springframework.web.bind.annotation.RequestMethod;import org.springframework.web.servlet.mvc.support.RedirectAttributes;import net.jeeshop.core.system.bean.User;import net.jeeshop.core.util.DateTimeUtil;import net.jeeshop.core.util.UUIDGeneratorUtil;import net.jeeshop.core.util.webServiceUtil;import net.jeeshop.services.common.telOperVO;import net.jeeshop.services.common.telTradeRequestVO;import net.jeeshop.services.common.userProduct;import net.jeeshop.services.manage.DetailsVO.bean.DetailsVO;import net.jeeshop.services.manage.NvhlApplicantVO.NvhlApplicantService;import net.jeeshop.services.manage.NvhlApplicantVO.bean.NvhlApplicantVO;import net.jeeshop.services.manage.NvhlBaseVO.NvhlBaseService;import net.jeeshop.services.manage.NvhlBaseVO.bean.NvhlBaseVO;import net.jeeshop.services.manage.NvhlBaseVO.bean.NvhlCommonVO;import net.jeeshop.services.manage.NvhlInsuredVO.bean.NvhlInsuredVO;import net.jeeshop.services.manage.pageRecord.bean.PageRecord;import net.jeeshop.services.manage.secureProduct.SecureProductService;import net.jeeshop.services.manage.secureProduct.bean.SecureProduct;import net.jeeshop.services.manage.secureProduct.bean.SecureProductDetail;import net.jeeshop.services.manage.system.impl.UserService;import net.jeeshop.web.action.BaseController;import net.jeeshop.web.util.LoginUserHolder;import net.jeeshop.web.util.RequestHolder;import net.sf.json.JSONArray;import net.sf.json.JSONObject;/** * 产品管理的Action * @author sunshuo * */@Controller@RequestMapping("/manage/secureProduct/")public class SecureProductAction extends BaseController<SecureProduct> {	private static final Logger logger = LoggerFactory.getLogger(SecureProductAction.class);	private static final long serialVersionUID = 1L;	@Autowired	private NvhlBaseService nvhlBaseService;	@Autowired	private NvhlApplicantService nvhlApplicantService;	@Autowired	private SecureProductService secureProductService;	@Autowired	private UserService userService;	//超级管理员的列表页面	private static final String page_toListAdmin = "/manage/secureProduct/admin/secureProductList";	//修改主产品	private static final String page_toEdit = "/manage/secureProduct/admin/secureProductEdit";	//添加主产品	private static final String page_toAdd = "/manage/secureProduct/admin/secureProductEdit";	//下订单页面	private static final String page_toOrders = "/manage/secureProduct/admin/secureProductInfo";	//结算页面	private static final String page_toPay = "/manage/secureProduct/secureProductPay";		private static final String showBindProduct = "/manage/userProduct/secureProductListTap";	private static final String showAllProduct = "/manage/userProduct/allSecureProductList";		private String resultInfo = ""; //页面处理显示结果		public String getResultInfo() {		return resultInfo;	}	private String jsonTest = "{\"telTradeRtnVO\":{\"dataTranArea\":{\"appNoFlag\":\"\",\"packageList\":[{\"baseVO\":{\"CAgtAgrNo\":\"\",\"CAppNo\":\"0100101000106300420160000395\",\"CDptCde\":\"1001010001\",\"CProdNo\":\"063004\"}}]},\"resultVO\":{\"resultCode\":\"1\"}}}";	private SecureProductAction() {		super.page_toList = page_toListAdmin;		super.page_toAdd = page_toAdd;		super.page_toEdit = page_toEdit;	}	public NvhlBaseService getNvhlBaseService() {		return nvhlBaseService;	}	public void setNvhlBaseService(NvhlBaseService nvhlBaseService) {		this.nvhlBaseService = nvhlBaseService;	}		public NvhlApplicantService getNvhlApplicantService() {		return nvhlApplicantService;	}	public void setNvhlApplicantService(NvhlApplicantService nvhlApplicantService) {		this.nvhlApplicantService = nvhlApplicantService;	}	public UserService getUserService() {		return userService;	}	public void setUserService(UserService userService) {		this.userService = userService;	}	public SecureProductService getService() {		return secureProductService;	}	public void setSecureProductService(SecureProductService secureProductService) {		this.secureProductService = secureProductService;	}	/**	 * 	* @param	* @Description: 跳转到支付页面	* @author sunshuo	* @date 2016年10月21日 上午10:41:13 	* @return String    返回类型 	* @throws	 */	@RequestMapping(value="toPayPage")	public String toPayPage(HttpServletRequest request, @ModelAttribute("secure") SecureProduct secure, @ModelAttribute("common") NvhlCommonVO common,@ModelAttribute("base") NvhlBaseVO base,ModelMap model,@ModelAttribute("applicantVO") NvhlApplicantVO applicantVO,@ModelAttribute("insuredVO") NvhlInsuredVO insuredVO) throws Exception {						String secureProductID = RequestHolder.getRequest().getParameter("id");		String CProdNo = RequestHolder.getRequest().getParameter("CProdNo");//产品编码		RequestHolder.getSession().setAttribute("id", secureProductID);		RequestHolder.getSession().setAttribute("CProdNo", CProdNo);		User user = LoginUserHolder.getLoginUser();		NvhlApplicantVO applicants=new NvhlApplicantVO();		applicants.setCreateAccount(getAccount());		List<NvhlApplicantVO> applcantList = getNvhlApplicantService().selectList(applicants);		handleAttr(applcantList);		request.setAttribute("applcantList", applcantList);		secure.setUid(user.getId());		secure.setId(secureProductID);		super.selectList(request, secure);		model.addAttribute("secure",secure);		model.addAttribute("common",common);		model.addAttribute("base", base);		model.addAttribute("applicantVO", applicantVO);		model.addAttribute("insuredVO", insuredVO);		return page_toPay;	}	/**	* @param	* @Description: TODO(提交用户信息 判定是否符合 接口1000) 	* @author lyx	* @date 2016年10月26日 下午1:09:36 	* @return String    返回类型 	* @throws	 */	public List<NvhlApplicantVO> handleAttr(List<NvhlApplicantVO> e){		if(e!=null&&e.size()>0){					}		for(int i=0;i<e.size();i++){			e.get(i).setAppNmePlay(e.get(i).getAppNmePlay()+":"+e.get(i).getCertfCdePlay());		}		return e;	}		/**	* @param	* @Description: TODO(提交用户信息 判定是否符合 接口1000) 	* @author lyx	* @date 2016年10月26日 下午1:09:36 	* @return String    返回类型 	* @throws	 */	@RequestMapping(value="toCommit", method = RequestMethod.POST)	public String toCommit(HttpServletResponse res,HttpServletRequest request, @ModelAttribute("secure") SecureProduct secure, @ModelAttribute("common") NvhlCommonVO common,@ModelAttribute("base") NvhlBaseVO base,			ModelMap model,@ModelAttribute("applicantVO") NvhlApplicantVO applicantVO,@ModelAttribute("insuredVO") NvhlInsuredVO insuredVO, RedirectAttributes flushAttrs) throws Exception {						HttpSession session = RequestHolder.getSession();		//接口地址		String url = "http://60.212.43.251:6001/telnetscs/services/totalInterfaceParseNode?wsdl"; //clientCodeAndAppNo（--1000接口）		//PrintWriter pw = res.getWriter();		try{			String returnJson = jsonTest;  //WebService1001(url,applicantVO,insuredVO,base);			JSONObject  jo = JSONObject.fromObject(jsonTest);						//返回码 			String resultCode = jo.getJSONObject("telTradeRtnVO").getJSONObject("resultVO").get("resultCode").toString();			if(!StringUtils.isNotBlank(resultCode)){				resultCode = "0";				flushAttrs.addFlashAttribute("message", "验证失败请仔细核对信息重新提交！");				return page_toPay;				//pw.write(resultCode);							}			//投保单号 投保人代码 被保人编码 			//String cAppNo = jo.getJSONObject("telTradeRtnVO").getJSONObject("dataTranArea").getJSONArray("packageList").getJSONObject(0).getJSONObject("baseVO").get("CAppNo").toString();			//String appCde = jo.getJSONObject("telTradeRtnVO").getJSONObject("dataTranArea").getJSONArray("packageList").getJSONObject(0).getJSONObject("applicantVO").get("appCde").toString();			//String insuredCde = jo.getJSONObject("telTradeRtnVO").getJSONObject("dataTranArea").getJSONArray("packageList").getJSONObject(0).getJSONObject("insuredVO").get("insuredCde").toString();			//session.setAttribute("cAppNo", cAppNo); //把三个信息存入session			//session.setAttribute("appCde", appCde);			//session.setAttribute("insuredCde", insuredCde);			//投保人代码			applicantVO.setAppCde("1");			applicantVO.setAppCdePlay("11111");			//被保人代码			insuredVO.setInsuredCde("2");			insuredVO.setInsuredCdePlay("2222");			//投保单号			base.setCAppNo("223333");			model.addAttribute("secure",secure);			model.addAttribute("common",common);			model.addAttribute("base", base);			model.addAttribute("applicantVO", applicantVO);			model.addAttribute("insuredVO", insuredVO);			resultCode = "1";			//flushAttrs.addFlashAttribute("message", "验证成功，请支付！");			addMessage(flushAttrs, "验证成功，请支付！");			//pw.write(resultCode);					}		catch(Exception e){			e.printStackTrace();		}		return page_toPay;	}	/**	 * 	* @param	* @Description: 支付 处理	* 	1.拼接json 串 调用1000接口	*	2.根据产品code 获取产品信息	*	3.封装base对象 	*	4.插入base对象（订单） 投保人、 被保人对象 	* @author sunshuo	* @date 2016年10月21日 上午10:40:21 	* @return String    返回类型 	* @throws	 */	@RequestMapping(value="toPay", method = RequestMethod.POST)	public String toPay(HttpServletRequest request, @ModelAttribute("secure") SecureProduct secure, @ModelAttribute("common") NvhlCommonVO common,@ModelAttribute("base") NvhlBaseVO base,			ModelMap model,@ModelAttribute("applicantVO") NvhlApplicantVO applicantVO,@ModelAttribute("insuredVO") NvhlInsuredVO insuredVO, RedirectAttributes flushAttrs) throws Exception {								HttpSession sess = RequestHolder.getSession();		 String cAppNo =""; String appCde =""; String insuredCde ="";		if(sess.getAttribute("cAppNo")!=null){		   cAppNo = sess.getAttribute("cAppNo").toString();  //投保单号		   base.setCAppNo(cAppNo);		}		//投保人代码		if(sess.getAttribute("appCde")!=null){			appCde = sess.getAttribute("appCde").toString();  //投保单号			applicantVO.setAppCde(appCde);		}		//被保人代码		if(sess.getAttribute("InsuredCde")!=null){			 insuredCde = sess.getAttribute("InsuredCde").toString();  //投保单号			 insuredVO.setInsuredCde(insuredCde);		}				String url = "http://60.212.43.251:6001/telnetscs/services/totalInterfaceParseNode?wsdl"; //getPayNo（--1001接口）		String resultJson = WebService1001(url,applicantVO,insuredVO,base);				//		WebService1003("123",applicantVO,insuredVO,base);//		WebService1000("123",applicantVO,insuredVO,base);		//JSONObject json =JSONObject.fromObject(result);		//			//支付申请号//			//String payNo = WebService1001("123",applicantVO,insuredVO,base);//			insertOrders(secure,common,base,applicantVO,insuredVO);//			return  page_toPay;//		}else{//			//TODO:最后修改为错误页面//			return  page_toPay;		insertOrders(secure,common,base,applicantVO,insuredVO);		flushAttrs.addFlashAttribute("message", "支付成功!");//		}		return "redirect:back";	}	/**	 * 	* @param	* @Description:支付成功后保存订单	* @author sunshuo	* @date 2016年10月21日 下午3:15:47 	* @return String     	* @throws	 */	public void insertOrders(SecureProduct secure,NvhlCommonVO common,NvhlBaseVO base,NvhlApplicantVO applicantVO,NvhlInsuredVO insuredVO){		secure.setDeleteFlag(0);				if(RequestHolder.getSession().getAttribute("id")!=null){			if(RequestHolder.getSession().getAttribute("CProdNo")!=null){ 				secure.setCProdNo(RequestHolder.getSession().getAttribute("CProdNo").toString());			}		}		SecureProduct currentSecure = secureProductService.selectOne(secure);				setCommonProperty(applicantVO,common);		setCommonProperty(insuredVO,common);		setNvhlBaseProperty(base,currentSecure,applicantVO,insuredVO);						try{			int baseNum = getNvhlBaseService().insert(base);			int applicantVONum = getNvhlBaseService().insertApplication(applicantVO);			int insuredVONum = getNvhlBaseService().insertInsured(insuredVO);			logger.info(baseNum + "条记录插入 base表");			logger.info(applicantVONum + "条记录插入 applicantVO 投保人信息表");			logger.info(insuredVONum + "条记录插入 insuredVO 被保人信息表");		}		catch(Exception e ){			e.printStackTrace();		}				}	/**	 * 	* @param	* @Description:设置基本信息NvhlBaseVO	* @author sunshuo	* @date 2016年10月21日 下午3:15:47 	* @return void 	* @throws	 */	public void  setNvhlBaseProperty(NvhlBaseVO base,SecureProduct currentSecure,NvhlApplicantVO app ,NvhlInsuredVO ins){		UUIDGeneratorUtil uuidCreate=new UUIDGeneratorUtil();		base.setId(uuidCreate.getUUID());		base.setCProdName(currentSecure.getCProdName()); //险种名称		base.setCProdNo(currentSecure.getCProdNo()); //险种代码		base.setNAmtRmbExch(currentSecure.getNAmtRmbExch());//保额币种		base.setNAmt(currentSecure.getNAmt());//保额合计		base.setNPrmRmbExch(currentSecure.getNPrmRmbExch());//保费币种		base.setNPrm(currentSecure.getNPrm());//保费合计			base.setAppCde(app.getAppCde()); //投保人编码		base.setInsuredCde(ins.getInsuredCde()); //被保人编码						base.setCreateAccount(getAccount());		base.setStatus("0");//0支付中		base.setDeleteFlag("0");//0未删除	}	/**	 * 	* @param	* @Description:投保人被保人页面有名称相同的属性，此方法是给投保人相同的属性赋值 	* @author sunshuo	* @date 2016年10月21日 下午3:15:47 	* @return Object    返回类型 	* @throws	 */	public void  setCommonProperty( NvhlApplicantVO applicantVO,NvhlCommonVO common){		applicantVO.setClntMrk(common.getTClntMrk());		applicantVO.setCertfCde(common.getTCertfCde());		applicantVO.setCertfCls(common.getTCertfCls());		applicantVO.setClntAddr(common.getTClntAddr());		applicantVO.setZipCde(common.getTZipCde());		applicantVO.setEmail(common.getTEmail());		applicantVO.setMobile(common.getTMobile());		applicantVO.setCountry(common.getTCountry());		applicantVO.setCusRiskLvl(common.getTCusRiskLvl());		applicantVO.setCustRiskRank(common.getTCustRiskRank());		applicantVO.setCreateAccount(getAccount());	}	/**	 * 	* @param	* @Description:投保人被保人页面有名称相同的属性，此方法是 给被保人相同的属性赋值 	* @author sunshuo	* @date 2016年10月21日 下午3:15:47 	* @return void	* @throws	 */	public void  setCommonProperty(NvhlInsuredVO insuredVO,NvhlCommonVO common){		insuredVO.setClntMrk(common.getBClntMrk());		insuredVO.setCCertfCde(common.getBCCertfCde());		insuredVO.setCCertfCls(common.getBCCertfCls());		insuredVO.setClntAddr(common.getBClntAddr());		insuredVO.setZipCde(common.getBZipCde());		insuredVO.setCEmail(common.getBCEmail());		insuredVO.setMobile(common.getBMobile());		insuredVO.setCountry(common.getBCountry());		insuredVO.setCusRiskLvl(common.getBCusRiskLvl());		insuredVO.setCustRiskRank(common.getBCustRiskRank());		insuredVO.setCreateAccount(getAccount());	}	/**	 * 	* @param	* @Description: 获取投保单号，投保人编号，被保人编号	* @author sunshuo	* @date 2016年10月21日 上午10:42:36 	* @return String    返回类型 	* @throws	 */	public String WebService1000(String url ,NvhlApplicantVO applicantVO,@ModelAttribute("insuredVO") NvhlInsuredVO insuredVO,NvhlBaseVO baseVO){		//最底层 packageList		Map outerMap = new LinkedHashMap();				Map dataTranAreaMap = new LinkedHashMap();				//---------------packageListMap--------------------		Map packageListMap = new HashMap();		packageListMap.put("insuredVO", insuredVO);		packageListMap.put("applicantVO", applicantVO);		packageListMap.put("baseVO", baseVO);		//---------------packageListMap *****--------------				JSONArray packageListMapObject = JSONArray.fromObject(packageListMap);				dataTranAreaMap.put("tradType", "6NN003");		dataTranAreaMap.put("packageList", packageListMapObject);		dataTranAreaMap.put("appNoFlag", "3");				telTradeRequestVO ttr = getDefaultTelTradeRequest("6NN003",dataTranAreaMap);					outerMap.put("telTradeRequestVO",ttr);				telOperVO teloper = getDefaultTelOperVO("123456","123456","123456","123456","123456","123456","123456");				outerMap.put("telOperVO", teloper);				JSONObject json = JSONObject.fromObject(outerMap);				//调用webservice接口 		String result = webServiceUtil.getSupplyInfo(json.toString(), url);				return result;	}	/**	 * 	* @param	* @Description: 获取支付申请号	* @author sunshuo	* @date 2016年10月21日 上午10:42:56 	* @return String    返回类型 	* @throws	 */	public String WebService1001(String url ,NvhlApplicantVO applicantVO,@ModelAttribute("insuredVO") NvhlInsuredVO insuredVO,NvhlBaseVO baseVO){		//最底层 packageList		Map outerMap = new LinkedHashMap();				telTradeRequestVO telTradeRequestVO =null;		Map dataTranArea=new LinkedHashMap();		Map baseVOMap=new LinkedHashMap();				//---------------dataTranArea--------------------		DetailsVO detailsVo=new DetailsVO();		String[]emptyArr={""};		JSONArray detailsList = JSONArray.fromObject(detailsVo);				baseVOMap.put("amount", baseVO.getNPrm());//交易金额		baseVOMap.put("PayType", '2');//支付类型		baseVOMap.put("PayWay", '3');	//支付方式					baseVOMap.put("PolicyStartdate", '3');//过期时间		baseVOMap.put("SubCompany", '3');//分公司代码		baseVOMap.put("CurrencyType", '2');//币种		baseVOMap.put("InsuredName", '2');//缴费人名称		baseVOMap.put("DepartmentCode", '2');//业务归属部门				dataTranArea.put("baseVO", baseVOMap);		 dataTranArea.put("detailsList", detailsList);		 dataTranArea.put("entryObjList",emptyArr);		 dataTranArea.put("packageVO", null);		 dataTranArea.put("tradType","7NN004");		//---------------dataTranArea *****--------------								//---------------telTradeRequestVO--------------------		 telTradeRequestVO= getDefaultTelTradeRequest(null,dataTranArea);		//---------------telTradeRequestVO *****--------------			//---------------outerMap--------------------		telOperVO teloper=getDefaultTelOperVO("123456","123456","123456","123456","123456","123456","123456");				outerMap.put("telOperVO",teloper);				outerMap.put("telTradeRequestVO",telTradeRequestVO);			//---------------outerMap *****--------------						JSONObject json = JSONObject.fromObject(outerMap);				//调用webservice接口 		String result = webServiceUtil.getSupplyInfo(json.toString(), url);				return result;	}	/**	 * 	* @param	* @Description: 获取支付申请号	* @author sunshuo	* @date 2016年10月21日 上午10:43:14 	* @return String    返回类型 	* @throws	 */		public void WebService1003(String url ,NvhlApplicantVO applicantVO,@ModelAttribute("insuredVO") NvhlInsuredVO insuredVO,NvhlBaseVO baseVO){		//最底层 packageList		Map outerMap = new LinkedHashMap();				telTradeRequestVO telTradeRequestVO = null;		Map dataTranArea=new LinkedHashMap();		Map packageList=new LinkedHashMap();				Map cvrgList=new LinkedHashMap();		Map payList=new LinkedHashMap();		//---------------dataTranArea--------------------		String[]emptyArr={""};				cvrgList.put("CvrgNo","");//险别代码		cvrgList.put("Amt","");//保额		cvrgList.put("Rate","");//费率		Object[]cvrgListArr={cvrgList};				payList.put("NTms","");//期次		payList.put("CProdNo", null);//产品编号		payList.put("CPayorNme", null);//付款人姓名		payList.put("CPayorCde", null);//付款人代码		payList.put("NPayablePrm", null);//应付保费		payList.put("TPayBgnTm", null);//缴费起始日期		payList.put("TPayEndTm", null);//缴费截止日期		payList.put("NPaidPrm", null);//实收保费		payList.put("CCurCde", null);//	付款币种			payList.put("NPayNo", null);//支付申请号 		Object[]payListArr={payList};				packageList.put("packageNO","");		packageList.put("applicantVO", applicantVO);		packageList.put("insuredVO", insuredVO);		packageList.put("baseVO", baseVO);		packageList.put("cvrgList", cvrgListArr);		packageList.put("payList", payListArr);				Object[]packageListArr={packageList};			dataTranArea.put("cancelPlyVO", null);		 dataTranArea.put("packageList", packageListArr);		 dataTranArea.put("queryPlyVO",null);		 dataTranArea.put("queryAppVO", null);		 dataTranArea.put("packageVO", null);		 dataTranArea.put("tradType","");		//---------------dataTranArea *****--------------								//---------------telTradeRequestVO--------------------		  telTradeRequestVO= getDefaultTelTradeRequest(null,dataTranArea);//		telTradeRequestVO.put("dataTranArea", dataTranArea);//		telTradeRequestVO.put("transNo", "afb201606171656001291");//		telTradeRequestVO.put("transType", "3NN001");//		telTradeRequestVO.put("transDate", "2016-06-17");//		telTradeRequestVO.put("transTime", "08:56:14");//		telTradeRequestVO.put("subtransNo", "");//		telTradeRequestVO.put("pageRecord", null);				//---------------telTradeRequestVO *****--------------			//---------------outerMap--------------------		telOperVO teloper=getDefaultTelOperVO("123456","123456","123456","123456","123456","123456","123456");				outerMap.put("telOperVO",teloper);				outerMap.put("telTradeRequestVO",telTradeRequestVO);		outerMap.put("telTradeRtnVO",null);			//---------------outerMap *****--------------						JSONObject json = JSONObject.fromObject(outerMap);				//调用webservice接口 //		String result = webServiceUtil.getSupplyInfo(json.toString(), url);		//		return result;	}	/**	 * 	* @param	* @Description: 封装 teloper 实体	* @author sunshuo	* @date 2016年10月21日 下午3:53:38 	* @return telTradeRequestVO    返回类型 	* @throws	 */	public telOperVO getDefaultTelOperVO(String id,String cPassWd,String accequ,String cOperId,String operTm,String ip,String macAddress){		telOperVO teloper = new telOperVO();		teloper.setId(id);		teloper.setCPassWd(cPassWd);		teloper.setAccequ(accequ);		teloper.setCOperId(cOperId);		teloper.setOperTm(operTm);		teloper.setIp(ip);		teloper.setMacAddress(macAddress);		return teloper;	}	/**	 * 	* @param	* @Description: 封装 PageRecord 实体	* @author sunshuo	* @date 2016年10月21日 下午3:53:38 	* @return telTradeRequestVO    返回类型 	* @throws	 */	public PageRecord getDefaultPageRecord(){		PageRecord pr = new PageRecord();				return pr;	}	/**	 * 	* @param	* @Description: 封装 telTradeRequestVO 实体	* @author sunshuo	* @date 2016年10月21日 下午3:53:38 	* @return telTradeRequestVO    返回类型 	* @throws	 */	public telTradeRequestVO getDefaultTelTradeRequest(String tradeTye,Map dataTranArea){		telTradeRequestVO ttr = new telTradeRequestVO();		ttr.setDataTranArea(dataTranArea);		ttr.setTid(null);		ttr.setTransNo(null);		ttr.setTransTime(null);		ttr.setTransDate(DateTimeUtil.getDateNowByExpression("yyyy-MM-dd"));		ttr.setTransType(tradeTye);//需与业务确认 		ttr.setSubtransNo(null);		PageRecord pageRecord=getDefaultPageRecord();		ttr.setPageRecord(pageRecord);				return ttr;	}	@Override	public String toAdd(@ModelAttribute("e") SecureProduct e, ModelMap model) throws Exception {		String pid = RequestHolder.getRequest().getParameter("pid");				return page_toAdd;	}		/**	 * 	* @param	* @Description: TODO(这里用一句话描述这个方法的作用) 	* @author sunshuo	* @date 2016年10月21日 上午10:44:56 	* @return String    返回类型 	* @throws	 */	@RequestMapping(value = "toOrder")	public String toOrder(@ModelAttribute("e")SecureProduct e,ModelMap model) throws Exception {		//RequestHolder.getRequest().setAttribute("catalogs", systemManager.getCatalogs());//		getSession().setAttribute("insertOrUpdateMsg", "");		String id = RequestHolder.getRequest().getParameter("id");		e = getService().selectById(id);		SecureProductDetail subProduct=new SecureProductDetail();		subProduct.setpId(id);		List<SecureProductDetail> secureProductDetail=getService().selectSecureProductDetail(subProduct);		if(e==null || StringUtils.isBlank(e.getId())){			throw new NullPointerException("根据产品ID查询不到指定的产品！");		}		e.setSecureProductDetailList(secureProductDetail);		model.addAttribute("e", e);		return page_toOrders;			}	//列表页面点击 编辑商品	@Override	public String toEdit(@ModelAttribute("e")SecureProduct e, ModelMap model) throws Exception {//		getSession().setAttribute("insertOrUpdateMsg", "");		return toEdit0(e, model);	}	/**	 * 添加或编辑商品后程序回转编辑	 * @return	 * @throws Exception	 */	@RequestMapping(value = "toEdit2")	public String toEdit2(SecureProduct e, ModelMap model) throws Exception {		return toEdit0(e, model);	}	/**	 * 添加或编辑商品后程序回转编辑	 * @return	 * @throws Exception	 */	@RequestMapping(value = "toEditProduct")	public String toEditProduct(SecureProduct e, ModelMap model) throws Exception {		return toEdit0(e, model);	}		/**	 * 根据商品ID，加载商品全部信息	 */	private String toEdit0(SecureProduct e, ModelMap model) throws Exception {        //RequestHolder.getRequest().setAttribute("catalogs", systemManager.getCatalogs());		if(StringUtils.isBlank(e.getId())){			throw new NullPointerException("产品ID不能为空！");		}				e = getService().selectById(e.getId());		SecureProductDetail subProduct=new SecureProductDetail();		subProduct.setpId(e.getId());		List<SecureProductDetail> secureProductDetail=getService().selectSecureProductDetail(subProduct);		e.setSecureProductDetailList(secureProductDetail);		if(e.getSecureProductDetailList()!=null && e.getSecureProductDetailList().size() > 0){						//如果有子产品，则添加3个到集合的最后，以方便添加数据			for(int i=0;i<3;i++){				e.getSecureProductDetailList().add(new SecureProductDetail());			}					}		if(e==null || StringUtils.isBlank(e.getId())){			throw new NullPointerException("根据产品ID查询不到指定的产品！");		}		model.addAttribute("e", e);		return page_toEdit;	}	//分页查询商品	@Override	public String selectList(HttpServletRequest request,@ModelAttribute("e") SecureProduct e) throws Exception {		try {			String role = LoginUserHolder.getUserRole().getRole_name();			if(!role.equals("超级管理员")){				User user = LoginUserHolder.getLoginUser();				e.setUid(user.getId());			}			super.selectList(request, e);			return page_toListAdmin;				} catch (Exception ex) {			ex.printStackTrace();			throw ex;		}	}		@Override	protected void setParamWhenInitQuery(SecureProduct e) {		super.setParamWhenInitQuery(e);		String selectOutOfStockProduct = RequestHolder.getRequest().getParameter("selectOutOfStockProduct");		if(StringUtils.isNotBlank(selectOutOfStockProduct)){			//后台--首页 需要查询缺货商品			e.setSelectOutOfStockProduct(Boolean.valueOf(selectOutOfStockProduct));		}	}	/**	 *  添加产品	 */	@RequestMapping(value = "insertSecureProduct", method = RequestMethod.POST)	public String insertSecureProduct(HttpServletRequest request, SecureProduct e, ModelMap model, RedirectAttributes flushAttrs) throws Exception {		logger.error(">>>insert secureProduct...");				e.setStatus(1);		e.setDeleteFlag(0);		e.setCreateAccount(getAccount());		int pid=getService().insertSecureProduct(e);		insertSubProduct(e,pid);		e.setCreateAccount(getAccount());		model.addAttribute("e", e);		flushAttrs.addFlashAttribute("message", "新增成功！");		return "redirect:back";	}	/**	 *  添加子产品	 */	public void insertSubProduct(SecureProduct e,int pid) throws Exception {		List<SecureProductDetail> secureProductDetaillist=e.getSecureProductDetailList();		if(secureProductDetaillist!=null && secureProductDetaillist.size()>0){			for(int i=0;i<secureProductDetaillist.size();i++){				logger.error("=======保存子产品" + secureProductDetaillist.get(i));				SecureProductDetail subProduct = secureProductDetaillist.get(i);				if(subProduct.getSubName()!=null&&subProduct.getSubName().length() >0){					subProduct.setpId(String.valueOf(pid));					subProduct.setStatus(1);					subProduct.setDeleteFlag(0);					subProduct.setCreateAccount(getAccount());					int id=getService().insertSecureProductDetail(subProduct);				}							}		}else{			logger.error("=======保存失败");		}	}	/**	 * 更新产品	 */	public String updateSubProduct(HttpServletRequest request, SecureProductDetail e) throws Exception {		e.setUpdateAccount(getAccount());		getService().updateSecureProductDetail(e);		return null;	}		//获取后台管理人员的账号	private String getAccount(){		User user = LoginUserHolder.getLoginUser();		if(user==null){			throw new NullPointerException("登陆超时！");		}		return user.getId();	}		/**	 * 更新产品	 */	@Override	public String update(HttpServletRequest request, SecureProduct e, RedirectAttributes flushAttrs) throws Exception {		e.setUpdateAccount(getAccount());		//insertSubProduct(e,Integer.parseInt(e.getId()));		getService().update(e);		addMessage(flushAttrs, "修改成功！");		return "redirect:toEdit2?id="+ e.getId();	}		/**	 * 	* @param	* @Description: 公共的批量删除数据的方法，子类可以通过重写此方法实现个性化的需求 	* @author sunshuo	* @date 2016年10月21日 下午4:52:36 	* @return String    返回类型 	* @throws	 */	@Override	        public String deletes(HttpServletRequest request, String[] ids, SecureProduct e, RedirectAttributes flushAttrs) throws Exception {	    			String deleteFlag = RequestHolder.getRequest().getParameter("deleteFlag");		User user = LoginUserHolder.getLoginUser();		//==0是只删除子产品		if(deleteFlag.equals("0")){				e.setUpdateAccount(user.getId());			getService().deleteSubProduct(ids, e);					}else{			getService().deletes(ids,SecureProduct.Delete_flag_y,user.getId());	        		}			addMessage(flushAttrs, "删除成功！");        //return "redirect:selectList";		return "redirect:selectList";    }	/**	 * 	* @param	* @Description: 商品上架 	* @author sunshuo	* @date 2016年10月21日 下午4:52:36 	* @return String    返回类型 	* @throws	 */	@RequestMapping(value = "updateUpProduct", method = RequestMethod.POST)	public String updateUpProduct(@ModelAttribute("e")SecureProduct e) throws Exception{		if(StringUtils.isBlank(e.getId())){			throw new NullPointerException();		}				User user = LoginUserHolder.getLoginUser();		secureProductService.updateProductStatus(new String[]{e.getId()},SecureProduct.Product_status_y,user.getUsername());//		getSession().setAttribute("insertOrUpdateMsg", "上架成功！");//		getResponse().sendRedirect(getEditUrl(e.getId()));		return "redirect:toEdit2?id="+e.getId();	}		/**	 * 	* @param	* @Description: 商品下架 	* @author sunshuo	* @date 2016年10月21日 下午4:52:36 	* @return String    返回类型 	* @throws	 */	@RequestMapping(value = "updateDownProduct", method = RequestMethod.POST)	public String updateDownProduct(@ModelAttribute("e")SecureProduct e) throws Exception{		if(StringUtils.isBlank(e.getId())){			throw new NullPointerException();		}		User user = LoginUserHolder.getLoginUser();		secureProductService.updateProductStatus(new String[]{e.getId()},SecureProduct.Product_status_n,user.getUsername());//		getSession().setAttribute("insertOrUpdateMsg", "下架成功！");//		getResponse().sendRedirect(getEditUrl(e.getId()));		return "redirect:toEdit2?id="+e.getId();	}		/**	 * 	* @param	* @Description: 商品上架 	* @author sunshuo	* @date 2016年10月21日 下午4:52:36 	* @return String    返回类型 	* @throws	 */	@RequestMapping(value = "updateUp", method = RequestMethod.POST)	public String updateUp(@ModelAttribute("e")SecureProduct e,String[] ids) throws Exception{				updateStatus(ids,SecureProduct.Product_status_y);		return "redirect:selectList";	}	/**	 * 	* @param	* @Description: 商品下架 	* @author sunshuo	* @date 2016年10月21日 下午4:52:36 	* @return String    返回类型 	* @throws	 */	@RequestMapping(value = "updateDown", method = RequestMethod.POST)	public String updateDown(@ModelAttribute("e")SecureProduct e,String[] ids) throws Exception{		updateStatus(ids, SecureProduct.Product_status_n);		return selectList(RequestHolder.getRequest(), new SecureProduct());	}	/**	 * 	* @param	* @Description: 商品下架 /上架	* @author sunshuo	* @date 2016年10月21日 下午4:52:36 	* @return String    返回类型 	* @throws	 */	private void updateStatus(String[] ids, int status){		User user = LoginUserHolder.getLoginUser();		secureProductService.updateProductStatus(ids,status,user.getUsername());	}		/**	 * @description 用户编辑页面查询该用户已绑定的所有产品	 * @author lin	 * @return	 * @throws Exception	 */	@RequestMapping("userProductList")	public String userProductList(HttpServletRequest request, ModelMap model , @ModelAttribute("e") SecureProduct e) throws Exception {		// 用户编号		String uid = request.getParameter("uid");		// 用户名存入session 供显示用		if (request.getParameter("uname") != null) {			RequestHolder.getSession().setAttribute("userName", request.getParameter("uname"));			RequestHolder.getSession().setAttribute("uid", uid);		}				super.selectList(request, e);		return showBindProduct;	}	/**	 * 查询所有商品 标记已绑定的	 * 	 * @param request	 * @param ids	 *     已绑定的产品编号	 * @return	 * @throws Exception	 */	@RequestMapping("getAllProduct")	public String getAllProduct(HttpServletRequest request, @ModelAttribute("e") SecureProduct e)			throws Exception {		try {			HttpSession sess = RequestHolder.getSession();			String uid = e.getUid();			e.setUid("");			//e.setStatus(1); //查询所有已上架的产品			super.selectList(request, e);			e.setUid(uid);			userProduct u = new userProduct();			u.setUser_id(uid);			//查询该用户的所有产品信息			List<userProduct> idsList = getService().selectIDListFromUserProduct(u);			String ids = "";			if(idsList.size()>0){				 Iterator<userProduct> up = idsList.iterator(); 				 while(up.hasNext()){					 userProduct userproduct  = up.next();					 ids += "," + userproduct.getProduct_id();				 }				 ids = ids.substring(1,ids.length());			}			//所有产品id串返回前台供勾选			sess.setAttribute("repeatIds", ids);			return showAllProduct;		} catch (Exception ex) {			ex.printStackTrace();			throw ex;		}	}	/**	* @param postData 1005报文	* @Description: TODO(远程调用接口) 	* @author lyx	* @date 2016年10月21日 下午2:07:55 	* @return String    返回类型 	* @throws	 */	public String getResonseInfo(String postData){		String responseMsg ="";//接受信息		HttpClient httpClient = new HttpClient();		httpClient.getParams().setContentCharset("utf-8");		String url = "http://localhost:8080/jshop"; //接口ｕｒｌ		PostMethod pm = new PostMethod(url);		pm.addParameter("responseXml", postData);		pm.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"utf-8");		try{		httpClient.executeMethod(pm);		responseMsg = pm.getResponseBodyAsString().trim();		}catch(Exception e){			e.printStackTrace();		}		return responseMsg;			}	/**	 * @Description: TODO(给用户绑定商品) 	 * @author lyx	 * @date 2016年9月21日 下午4:10:01 	 * @return String 返回类型 @throws	 */	@RequestMapping("bindUserProduct")	public String bindUserProduct(HttpServletRequest request, @ModelAttribute("e") SecureProduct e ) {		String userId = "";		String ids = request.getParameter("ids");		String uid ="";		userId = RequestHolder.getSession().getAttribute("uid").toString();//获取当前用户名		if(RequestHolder.getSession().getAttribute("userName")!=null){			 uid = RequestHolder.getSession().getAttribute("uid").toString();		}		int secureProductIDS = secureProductService.bindUserProduct(ids,uid);		logger.info("共插入了"+(secureProductIDS+1)+"条记录");					//查询该用户编号下的所有产品		try {			e.setUid(userId);			userProductList(request, null, e) ;		} catch (Exception e1) {			e1.printStackTrace();		}		return "redirect:userProductList?uid="+uid;	}}