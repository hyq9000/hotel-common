
import java.sql.ResultSet;

/**
 * 数据操作工作类,提供了一系列执行SQL的实现；
 * 创建时间:2012-03-04
 * @author 李四海
 */
public class DBUtil {
	/**
	 * 执行所有的更新SQL的操作；
	 * @param sql  合法的增，删，改的更新SQL
	 * @param objects 参数数组
	 */
	public void update(String sql,Object...objects){
		
	}
	
	/**
	 * 执行合法SQL的查询操作
	 * @param sql 合法的查询SQL
	 * @param objects 参数数组
	 * @return 如果返回失败则为null;
	 */
	public ResultSet query(String sql, Object...objects){
		return null;
	}
}
