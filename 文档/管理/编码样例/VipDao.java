
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 提供对于会员数据一系列操作的DAO实现；
 * 创建时间：2012-03-09
 * @author 张三丰
 */
public class VipDao {
	private DBUtil dbutil=new DBUtil();//数据库操作工具类
	
	/**
	 * 实现新注册一个会员数据到数据库表；
	 * @param vip 除主键(id)外,该对象其他各属性值都要求已初始化的；
	 * @exception 如果抛出了异常,则说明操作不成功；
	 */
	public void registVip(Vip vip) throws Exception{
		String sql="insert into vip values(?,?,?,?)";
		//准备数据
		Object[] params={vip.getId(),
				vip.getAge(),
				vip.getName(),
				vip.getAddress()};
		//执行SQL更新操作
		dbutil.update(sql, params);
	}
	
	/**
	 * 实现取得所有会员实例的集合；
	 * @return 返回存在的所有会员实例的List集合；如果没有则返回null;
	 * @exception 如果抛出了异常,则说明操作不成功；
	 */
	public List<Vip> getAllVip() throws Exception{
		String sql="select * from vip";	
		ResultSet rs=dbutil.query(sql);
		//将ResultSet封装到List集合中再返回；
		List<Vip> list=new ArrayList<Vip>();
		//......
		return list;
	}
}
