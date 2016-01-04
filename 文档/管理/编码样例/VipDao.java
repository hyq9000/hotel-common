
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * �ṩ���ڻ�Ա����һϵ�в�����DAOʵ�֣�
 * ����ʱ�䣺2012-03-09
 * @author ������
 */
public class VipDao {
	private DBUtil dbutil=new DBUtil();//���ݿ����������
	
	/**
	 * ʵ����ע��һ����Ա���ݵ����ݿ��
	 * @param vip ������(id)��,�ö�������������ֵ��Ҫ���ѳ�ʼ���ģ�
	 * @exception ����׳����쳣,��˵���������ɹ���
	 */
	public void registVip(Vip vip) throws Exception{
		String sql="insert into vip values(?,?,?,?)";
		//׼������
		Object[] params={vip.getId(),
				vip.getAge(),
				vip.getName(),
				vip.getAddress()};
		//ִ��SQL���²���
		dbutil.update(sql, params);
	}
	
	/**
	 * ʵ��ȡ�����л�Աʵ���ļ��ϣ�
	 * @return ���ش��ڵ����л�Աʵ����List���ϣ����û���򷵻�null;
	 * @exception ����׳����쳣,��˵���������ɹ���
	 */
	public List<Vip> getAllVip() throws Exception{
		String sql="select * from vip";	
		ResultSet rs=dbutil.query(sql);
		//��ResultSet��װ��List�������ٷ��أ�
		List<Vip> list=new ArrayList<Vip>();
		//......
		return list;
	}
}
