package net.jeeshop.services.manage.NvhlApplicantVO.dao.impl;import java.util.Date;import java.util.List;import javax.annotation.Resource;import org.apache.commons.lang.StringUtils;import org.springframework.stereotype.Repository;import net.jeeshop.core.dao.BaseDao;import net.jeeshop.core.dao.page.PagerModel;import net.jeeshop.core.util.DateTimeUtil;import net.jeeshop.core.util.UUIDGeneratorUtil;import net.jeeshop.services.manage.NvhlApplicantVO.bean.NvhlApplicantVO;import net.jeeshop.services.manage.NvhlApplicantVO.dao.NvhlApplicantDao;import net.jeeshop.services.manage.NvhlBaseVO.bean.NvhlBaseVO;import net.jeeshop.services.manage.NvhlBaseVO.dao.NvhlBaseDao;import net.jeeshop.services.manage.NvhlInsuredVO.bean.NvhlInsuredVO;import net.jeeshop.services.manage.secureProduct.bean.SecureProduct;import net.jeeshop.web.util.LoginUserHolder;/** * 保险订单管理 * @author lin * */@Repository("NvhlApplicantDaoManage")public class NvhlApplicantDaoImpl implements NvhlApplicantDao {    @Resource	private BaseDao dao;	public void setDao(BaseDao dao) {		this.dao = dao;	}	@Override	public int insert(NvhlApplicantVO e) {		// TODO Auto-generated method stub		return 0;	}	@Override	public int delete(NvhlApplicantVO e) {		// TODO Auto-generated method stub		return 0;	}	@Override	public int update(NvhlApplicantVO e) {		if(!StringUtils.isBlank(e.getId())){			e.setAppNme(e.getAppNmePlay());			e.setCertfCls(e.getCertfClsPlay());			e.setCertfCde(e.getCertfCdePlay());			e.setClntAddr(e.getClntAddrPlay());			e.setCountry(e.getCountryPlay());			e.setEmail(e.getEmailPlay());			e.setMobile(e.getMobilePlay());			e.setUpdateAccount(LoginUserHolder.getLoginUser().getId());						return dao.update("manage.nvhlApplicantVO.update", e);		}			else{			return 0;		}	}	/**	 * 根据投保人代码查具体的投保人信息	 */	@Override	public NvhlApplicantVO selectOne(NvhlApplicantVO e) {		return (NvhlApplicantVO) dao.selectOne("manage.nvhlApplicantVO.selectOne", e);	}		@Override	public PagerModel selectPageList(NvhlApplicantVO e) {		//获取当前用户权限 		//订单记录 查看 createAccount是当前登陆人的记录 如果是查看个人				String range = LoginUserHolder.getUserRole().getSelect_range();		if(!StringUtils.isBlank(range)){			if(range.indexOf("个人")>-1){				e.setCreateAccount(LoginUserHolder.getLoginUser().getId());			}			return dao.selectPageList("manage.nvhlApplicantVO.selectPageList", "manage.nvhlApplicantVO.selectPageCount", e);		}		else{			return null;		}			}	@Override	public List<NvhlApplicantVO> selectList(NvhlApplicantVO e) {				return dao.selectList("manage.nvhlApplicantVO.selectPageList",e);	}	@Override	public int deleteById(int id) {		// TODO Auto-generated method stub		return 0;	}	@Override	public NvhlApplicantVO selectById(String id) {				return (NvhlApplicantVO) dao.selectOne("manage.NvhlApplicantVO.selectById", id);	}}