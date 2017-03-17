package net.jeeshop.services.manage.NvhlBaseVO.dao.impl;import java.util.List;import javax.annotation.Resource;import org.apache.commons.lang.StringUtils;import org.slf4j.LoggerFactory;import org.springframework.stereotype.Repository;import ch.qos.logback.classic.Logger;import net.jeeshop.core.dao.BaseDao;import net.jeeshop.core.dao.page.PagerModel;import net.jeeshop.core.system.bean.Role;import net.jeeshop.services.manage.NvhlApplicantVO.bean.NvhlApplicantVO;import net.jeeshop.services.manage.NvhlBaseVO.bean.NvhCollectCommonVO;import net.jeeshop.services.manage.NvhlBaseVO.bean.NvhlBaseVO;import net.jeeshop.services.manage.NvhlBaseVO.dao.NvhlBaseDao;import net.jeeshop.services.manage.NvhlBaseVO.dao.NvhlCollectCommDao;import net.jeeshop.services.manage.NvhlInsuredVO.bean.NvhlInsuredVO;import net.jeeshop.services.manage.secureProduct.bean.SecureProduct;import net.jeeshop.web.action.manage.report.ReportInfo;import net.jeeshop.web.util.LoginUserHolder;/** * 保险订单管理 * @author lin * */@Repository("NvhlCollectCommDaoManage")public class NvhlCollectCommDaoImpl implements NvhlCollectCommDao {	 @Resource		private BaseDao dao;		public void setDao(BaseDao dao) {			this.dao = dao;		}		@Override		public int insert(NvhCollectCommonVO e) {			// TODO Auto-generated method stub			return dao.insert("manage.NvhlCollectComm.insertNvhlCollectComm", e);		}		@Override		public int delete(NvhCollectCommonVO e) {			// TODO Auto-generated method stub			return 0;		}		@Override		public int update(NvhCollectCommonVO e) {			// TODO Auto-generated method stub			return 0;		}		@Override		public NvhCollectCommonVO selectOne(NvhCollectCommonVO e) {			// TODO Auto-generated method stub			return null;		}		@Override		public PagerModel selectPageList(NvhCollectCommonVO e) {			// TODO Auto-generated method stub			return null;		}		@Override		public List<NvhCollectCommonVO> selectList(NvhCollectCommonVO e) {			// TODO Auto-generated method stub			return null;		}		@Override		public int deleteById(int id) {			// TODO Auto-generated method stub			return 0;		}		@Override		public NvhCollectCommonVO selectById(String id) {			// TODO Auto-generated method stub			return null;		}		@Override		public NvhCollectCommonVO selectByCappNo(String args, Object e) {			// TODO Auto-generated method stub			return (NvhCollectCommonVO) dao.selectOne("manage.NvhlCollectComm.selectByCappNo", e);		}	   }