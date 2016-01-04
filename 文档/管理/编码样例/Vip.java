package t55.template;
/**
 * 数据库表TEST_VIP的实体类；
 * 创建时间：2012-01-05
 * @author 周小雄
 */
public class Vip {
	
	private String id,//主键ID
	name,//姓名	
	address;//住址
	int age;//年龄

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
