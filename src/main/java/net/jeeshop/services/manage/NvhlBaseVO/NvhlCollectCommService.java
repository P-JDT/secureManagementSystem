package net.jeeshop.services.manage.NvhlBaseVO;import java.util.List;import javax.servlet.http.HttpServletRequest;import net.jeeshop.core.Services;import net.jeeshop.services.manage.NvhlApplicantVO.bean.NvhlApplicantVO;import net.jeeshop.services.manage.NvhlBaseVO.bean.NvhCollectCommonVO;import net.jeeshop.services.manage.NvhlBaseVO.bean.NvhlBaseVO;import net.jeeshop.services.manage.NvhlInsuredVO.bean.NvhlInsuredVO;import net.jeeshop.web.action.manage.report.ReportInfo;/** *  * @author Yang * */public interface NvhlCollectCommService extends Services<NvhCollectCommonVO> {	/**	 * 根据保单标号查寻相关数据	 * @param args	 * @param e	 * @return	 */	public NvhCollectCommonVO selectByCappNo(String args,Object e);}