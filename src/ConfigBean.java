import java.util.HashSet;
import java.util.Set;



public class ConfigBean {
	private String db_addr1;
	private String db_addr2;
	private int sleep_time;
	private String init_seed;
	private String path;
	private Set<String> filter_distinct;
	private Set<String> filter_include;
	private Set<String> down_include;

	public ConfigBean()
	{
		filter_distinct = new HashSet<String>();
		filter_include = new HashSet<String>();
		down_include = new HashSet<String>();
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDb_addr1() {
		return db_addr1;
	}
	public void setDb_addr1(String db_addr1) {
		this.db_addr1 = db_addr1;
	}
	public String getDb_addr2() {
		return db_addr2;
	}
	public void setDb_addr2(String db_addr2) {
		this.db_addr2 = db_addr2;
	}
	public int getSleep_time() {
		return sleep_time;
	}
	public void setSleep_time(int sleep_time) {
		this.sleep_time = sleep_time;
	}
	public String getInit_seed() {
		return init_seed;
	}
	public void setInit_seed(String init_seed) {
		this.init_seed = init_seed;
	}
	public void filter_distinct_put(String url)
	{
		filter_distinct.add(url);
	}
	public void filter_include_put(String url)
	{
		filter_include.add(url);
	}
	public void down_include_put(String url)
	{
		down_include.add(url);
	}
	
	public Set<String> getFilter_distinct() {
		return filter_distinct;
	}

	public Set<String> getFilter_include() {
		return filter_include;
	}

	public Set<String> getDown_include() {
		return down_include;
	}
	/*public void dump_all()
	{
		System.out.println("打印所有信息");
		System.out.println(db_addr1+", "+db_addr2+", "+sleep_time+", "+ init_seed);
		
		for(String temp : filter_distinct)
		{
			System.out.println("filter_distinct:"+temp);
		}
		
		for(String temp : filter_include)
		{
			System.out.println("filter_include:"+temp);
		}
		
		for(String temp : down_include)
		{
			System.out.println("down_include:"+temp);
		}
	}*/
}
