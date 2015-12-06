package org.example.yulin;

public class AppConfig {
	public static String YuLingUrl = "http://yuling.bmob.cn/";
	/**
	 * 图灵请求地址 get
	 */
	public static String TuLingUrl = "http://www.tuling123.com/openapi/api";
	public static String TuLingApiKey = "b78c958bc1b9acd521dee9d2a4d5ee6c";
	/**
	 * 讯飞appid
	 */
	public static String XunfeiAppId = "54ad33cf";

	public static String QQAppId = "1103994876";

	/** 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY */
	public static String SinaAppKEY = "213187260";

	/**
	 * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
	 * 
	 * <p>
	 * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响， 但是没有定义将无法使用 SDK 认证登录。
	 * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
	 * </p>
	 */
	public static final String REDIRECT_URL = "http://www.sina.com";

	/**
	 * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
	 * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利 选择赋予应用的功能。
	 * 
	 * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的 使用权限，高级权限需要进行申请。
	 * 
	 * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
	 * 
	 * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
	 * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
	 */
	public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";

	/**
	 * 酷果
	 */
	public static String KuGuoId = "40ef70191a7242f88b243bda4ee05ae2";
	public static String KuGuoChannelId="k-hiapk";//安卓市场
	/**
	 * Bmob
	 */
	public static String BmobAppId = "744cf27293178f7a7f05de03bf0945ee";

	/**
	 * 大众点评
	 */
	public static String DianPingAppKey = "06139676";
	public static String DianPingAppSecret = "e5192d2385ba4edca1f238f699d1b08f";
	// 获取当前在线的全部团购ID列表
	public static String DianPingUrlGetGoods = "http://api.dianping.com/v1/deal/get_all_id_list";
	// 获取支持团购搜索的最新城市列表
	public static String DianPingUrlGetCitys = "http://api.dianping.com/v1/metadata/get_cities_with_deals";
	// 根据多个团购ID批量获取指定团购单的详细信息
	public static String DianPingUrlGet_Batch_Deals_By_Id = "http://api.dianping.com/v1/deal/get_batch_deals_by_id";
	/**
	 * 获取指定城市每日新增团购ID列表
	 * 
	 * @param city
	 *            string 包含团购信息的城市名称，可选范围见相关API返回结果
	 * @param date
	 *            string 查询日期，格式为“YYYY-MM-DD
	 */
	public static String DianPingUrlGet_Daily_Wew_Id_List = "http://api.dianping.com/v1/deal/get_daily_new_id_list";
	
	public static String DianPingUrlGet_Find_Deals="http://api.dianping.com/v1/deal/find_deals";
}
