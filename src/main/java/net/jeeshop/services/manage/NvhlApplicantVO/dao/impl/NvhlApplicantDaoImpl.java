package net.jeeshop.services.manage.NvhlApplicantVO.dao.impl;import java.util.Date;import java.util.List;import javax.annotation.Resource;import org.springframework.stereotype.Repository;import net.jeeshop.core.dao.BaseDao;import net.jeeshop.core.dao.page.PagerModel;import net.jeeshop.core.util.DateTimeUtil;import net.jeeshop.core.util.UUIDGeneratorUtil;import net.jeeshop.services.manage.NvhlApplicantVO.bean.NvhlApplicantVO;import net.jeeshop.services.manage.NvhlApplicantVO.dao.NvhlApplicantDao;import net.jeeshop.services.manage.NvhlBaseVO.bean.NvhlBaseVO;import net.jeeshop.services.manage.NvhlBaseVO.dao.NvhlBaseDao;import net.jeeshop.services.manage.NvhlInsuredVO.bean.NvhlInsuredVO;import net.jeeshop.web.util.LoginUserHolder;/** * 保险订单管理 * @author lin * */@Repository("NvhlApplicantDaoManage")public class NvhlApplicantDaoImpl implements NvhlApplicantDao {    @Resource	private BaseDao dao;	public void setDao(BaseDao dao) {		this.dao = dao;	}	@Override	public int insert(NvhlApplicantVO e) {		// TODO Auto-generated method stub		return 0;	}	@Override	public int delete(NvhlApplicantVO e) {		// TODO Auto-generated method stub		return 0;	}	@Override	public int update(NvhlApplicantVO e) {		// TODO Auto-generated method stub		return 0;	}	@Override	public NvhlApplicantVO selectOne(NvhlApplicantVO e) {		// TODO Auto-generated method stub		return null;	}	@Override	public PagerModel selectPageList(NvhlApplicantVO e) {		return dao.selectPageList("manage.nvhlApplicantVO.selectPageList", "manage.nvhlApplicantVO.selectPageCount", e);	}	@Override	public List<NvhlApplicantVO> selectList(NvhlApplicantVO e) {		// TODO Auto-generated method stub		return null;	}	@Override	public int deleteById(int id) {		// TODO Auto-generated method stub		return 0;	}	@Override	public NvhlApplicantVO selectById(String id) {		// TODO Auto-generated method stub		return null;	}	}