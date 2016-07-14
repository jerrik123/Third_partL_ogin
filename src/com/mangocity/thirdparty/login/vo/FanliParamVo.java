package com.mangocity.thirdparty.login.vo;

/**
 * 返利联合登录参数VO
 * @author panshilin
 *
 */
public class FanliParamVo {
	/**
	 * 返利网标识
	 */
	private String channelid;
	/**
	 * 返利网的会员ID
	 */
	private String u_id;
	/**
	 * 目标url
	 */
	private String url;
	/**
	 * 合法性校验码
	 */
	private String code;
	/**
	 * 是否同步用户信息标识，true为接收，false为不接受以下参数
	 */
	private String syncname;
	/**
	 * 联合登陆的用户名，一般是用户在返利网的用户ID加“@51fanli”,
	 * 即如果会员id是238719，那本字段内容为“238719@51fanli”
	 */
	private String username;
	/**
	 * 用户安全码，由返利网生成
	 */
	private String usersafekey;
	/**
	 * UNIX时间戳(1970年1月1日0时到当前时间的秒数)
	 */
	private String action_time;
	/**
	 * 用户email
	 */
	private String email;
	/**
	 * 是否同步储存用户在返利网的默认收货地址标识，
	 * true为接收，false为不接受以下参数
	 */
	private String syncaddress;
	/**
	 * 收货人姓名
	 */
	private String name;
	/**
	 * 收货人地址-省份
	 */
	private String province;
	/**
	 * 收货人地址-城市
	 */
	private String city;
	/**
	 * 收货人地址-区
	 */
	private String area;
	/**
	 * 收货人地址-地址
	 */
	private String address;
	/**
	 * 收货人地址-邮编
	 */
	private String zip;
	/**
	 * 收货人固定电话
	 */
	private String phone;
	/**
	 * 收货人手机号
	 */
	private String mobile;
	/**
	 * 返利网用户初始密码<芒果网暂不使用>
	 */
	private String pwd;
	/**
	 * 邮箱标识(芒果内部使用，标识使用哪个作为使用的邮箱)
	 * 如果是false的话，直接用返利过来的邮箱注册;如果是true的话，则用返利用户名+.com邮箱注册
	 */
	private String emailFlag;
	
	
	public String getChannelid() {
		return channelid;
	}
	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}
	public String getU_id() {
		return u_id;
	}
	public void setU_id(String u_id) {
		this.u_id = u_id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSyncname() {
		return syncname;
	}
	public void setSyncname(String syncname) {
		this.syncname = syncname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsersafekey() {
		return usersafekey;
	}
	public void setUsersafekey(String usersafekey) {
		this.usersafekey = usersafekey;
	}
	public String getAction_time() {
		return action_time;
	}
	public void setAction_time(String action_time) {
		this.action_time = action_time;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSyncaddress() {
		return syncaddress;
	}
	public void setSyncaddress(String syncaddress) {
		this.syncaddress = syncaddress;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getEmailFlag() {
		return emailFlag;
	}
	public void setEmailFlag(String emailFlag) {
		this.emailFlag = emailFlag;
	}
}
