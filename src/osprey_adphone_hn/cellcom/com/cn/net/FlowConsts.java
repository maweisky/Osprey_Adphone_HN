package osprey_adphone_hn.cellcom.com.cn.net;

public class FlowConsts {
	public static String key = "aesaesaesaesaesa";
	public static final String SERVICE_IP = "http://183.6.172.138:8081/yywapp/";
	// public static final String
	// SERVICE_IP="http://219.137.26.249:8087/ospreynew";
	public static final String STATUE_1 = "1";

	// 获取系统参数
	public static String YYW_GETSYSDATA = SERVICE_IP + "/yyw_getsysdata.flow";
	// 查询广告图片
	public static String YYW_GETGG = SERVICE_IP + "/yyw_getgg_new.flow";
	// 登录
	public static String YYW_LOGIN = SERVICE_IP + "/yyw_login.flow";
	// 获取验证码
	public static String YYW_SENDVERIFSMS = SERVICE_IP
			+ "/yyw_sendverifysms.flow";
	// 注册
	public static String YYW_REGISTER = SERVICE_IP + "/yyw_register.flow";
	// 修改密码
	public static String YYW_MODIFYPWD = SERVICE_IP + "/yyw_updatepwd.flow";
	// 意见反馈
	public static String YYW_SUGGEST = SERVICE_IP + "/yyw_suggest.flow";
	// 拨打电话
	public static String YYW_CALLPHONE = SERVICE_IP + "/yyw_callphone.flow";
	// 获取通话记录
	public static String YYW_GETCALL_LOGLIST = SERVICE_IP + "/yyw_getcall_loglist.flow";	
//	// 拨打电话
//	public static String YYW_CALLPHONEGG = SERVICE_IP
//			+ "/yyw_getcallphonegg.flow";
	// 获取用户信息
	public static String YYW_USERINFO = SERVICE_IP + "/yyw_userinfo.flow";
	
	// 获取看一看广告列表 yyw_getlookgglist
	public static String YYW_GETLOOKGGLIST = SERVICE_IP
			+ "/yyw_getlookgglist.flow";

	// 获取看一看广告列表 yyw_getlookgglist_new(新版)
	public static String YYW_GETLOOKGGLIST_NEW = SERVICE_IP
			+ "/yyw_getlookgglist_new.flow";
	// 获取看一看类型接口 yyw_getkyktype
	public static String YYW_GETKYKTYPE = SERVICE_IP + "/yyw_getkyktype_new.flow";
	// 获取看一看广告媒体信息yyw_getlookgginfo
	public static String YYW_GETLOOKGGINFO = SERVICE_IP
			+ "/yyw_getlookgginfo.flow";
	// 领取看一看奖品yyw_getlookmoney
	public static String YYW_GETLOOKMONEY = SERVICE_IP
			+ "/yyw_getlookmoney.flow";
	// 扫一扫获取奖品yyw_getsao
	public static String YYW_GETSAO = SERVICE_IP + "/yyw_getsao.flow";
	// 获取摇一摇广告列表 yyw_getyaogglist
	public static String YYW_GETYAOGGLIST = SERVICE_IP
			+ "/yyw_getyaogglist.flow";
	// 摇一摇获取奖品yyw_getyao  (旧摇一摇获奖流程)
	public static String YYW_GETYAO = SERVICE_IP + "/yyw_getyao.flow";
	// 摇一摇获取奖品yyw_getyao_new (新摇一摇获奖流程)
	public static String YYW_GETYAO_NEW = SERVICE_IP + "/yyw_getyao_new.flow";
	// 摇一摇获取奖品yyw_getyao_new2 (最新摇一摇获奖流程)
	public static String YYW_GETYAO_NEW2 = SERVICE_IP + "/yyw_getyao_new2.flow";
	// 转一转奖品列表 yyw_getzhuanlist
	public static String YYW_GETZHUANLIST = SERVICE_IP + "/yyw_getzhuanlist.flow";
	// 转一转奖品列表 yyw_zyz_getlist(20150428新接口)
	public static String YYW_ZYZ_GETLIST = SERVICE_IP + "/yyw_zyz_getlist.flow";
	// 转一转抽奖 yyw_zhuanchoujiang
	public static String YYW_ZHUANCHOUJIANG = SERVICE_IP  + "/yyw_zhuanchoujiang.flow";
	// 转一转抽奖 yyw_zyz_getwin(20150428新接口)
	public static String YYW_ZYZ_GETWIN = SERVICE_IP  + "/yyw_zyz_getwin.flow";

	// 财产操作记录查询接口 yyw_getcaichan
	public static String YYW_GETCAICHAN = SERVICE_IP + "/yyw_getcaichan.flow";
	// 兑换中心接口 yyw_duihuan
	public static String YYW_DUIHUAN = SERVICE_IP + "/yyw_duihuan.flow";
	// 商品类别获取接口 yyw_shop_getvarietylist
	public static String YYW_SHOP_GETVARIETYLIST = SERVICE_IP
			+ "/yyw_shop_getvarietylist.flow";
	// 商品列表接口yyw_shangpin_list
	public static String YYW_SHANGPIN_LIST = SERVICE_IP
			+ "/yyw_shangpin_list.flow";
	// 商品详情接口 yyw_shangpin_detail
	public static String YYW_SHANGPIN_DETAIL = SERVICE_IP
			+ "/yyw_shangpin_detail.flow";
	// 加入购物车接口 yyw_shopping_add
	public static String YYW_SHANGPIN_ADD = SERVICE_IP
			+ "/yyw_shopping_add.flow";
	// 查看购物车接口 yyw_shangpin_look
	public static String YYW_SHANGPIN_LOOK = SERVICE_IP
			+ "/yyw_shangpin_look.flow";
	// 删除购物车接口 yyw_shopping_del
	public static String YYW_SHANGPIN_DEL = SERVICE_IP
			+ "/yyw_shopping_del.flow";
	//删除购物车已完成记录 yyw_shopping_del2
	public static String YYW_SHANGPIN_DEL2 = SERVICE_IP
			+ "/yyw_shopping_del2.flow";
	// 更新购物车商品数量接口 yyw_shopping_update
	public static String YYW_SHANGPIN_UPDATE = SERVICE_IP
			+ "/yyw_shopping_update.flow";
	// 兑换商品接口 yyw_shopping_order
	public static String YYW_SHANGPIN_ORDER = SERVICE_IP
			+ "/yyw_shopping_order.flow";

	// 修改个人资料
	public static String YYW_USERINFO_UPDATE = SERVICE_IP
			+ "/yyw_userinfo_update.flow";
	// 家生活获取设备列表接口
	public static String YYW_GETDEVICELIST = SERVICE_IP
			+ "/yyw_getdevicelist.flow";
	// 家生活查询省份、城市、区域、小区、单元、房号接口
	public static String YYW_GETHOUSE = SERVICE_IP
			+ "/yyw_gethouse.flow";
	//获取全部省份列表
	public static String YYW_GETAREALIST=SERVICE_IP
			+ "yyw_getarealist.flow";
	//家生活绑定、修改设备接口
	public static String YYW_SETDEVICE = SERVICE_IP
			+ "/yyw_setdevice.flow";
	//家生活删除设备接口
	public static String YYW_DELDEVICE = SERVICE_IP
			+ "/yyw_deldevice.flow";

	//家生活获取物管信息接口 yyw_getwginfo
	public static String YYW_GETWGINFO = SERVICE_IP
			+ "/yyw_getwginfo.flow";

	//注册2cu成功回调接口
	public static String YYW_REG2CU = SERVICE_IP
			+ "/yyw_reg2cu.flow";

	//获取兑换率规则接口 yyw_getrate
	public static String YYW_GETRATE = SERVICE_IP
			+ "/yyw_getrate.flow";
	
	// 获取特价商品列表 yyw_shangpin_discount
	public static String YYW_SHANGPIN_DISCOUNT = SERVICE_IP
			+ "/yyw_shangpin_discount.flow";

	public static String YYW_HOMETIME=SERVICE_IP+"/yyw_hometime.flow";
	//获取冠名广告
	public static String YYW_GETAD_NAEMED=SERVICE_IP+"/yyw_getad_named.flow";
	//扫一扫领奖（新版 150429）
	public static String YYW_SAO_GETWIN=SERVICE_IP+"/yyw_sao_getwin.flow";
	//获取订单号
    public static String YYW_ZFB_CREATEORDER=SERVICE_IP+"/yyw_zfb_createorder.flow";
    //抢红包列表
    public static String YYW_GETREDPACKEDLIST=SERVICE_IP+"/yyw_getredpackedlist.flow";
    // 预约抢红包
    public static String YYW_MAKEROB=SERVICE_IP+"/yyw_makerob.flow";
    // 抢红包
    public static String YYW_ROBREDPACKED=SERVICE_IP+"/yyw_robredpacked.flow";
    
	public static void refleshIp(String urlString) {
		// 获取系统参数
		YYW_GETSYSDATA = urlString + "/yyw_getsysdata.flow";
		// 查询广告图片
		YYW_GETGG = urlString + "/yyw_getgg_new.flow";
		// 新登录
		YYW_LOGIN = urlString + "/yyw_login.flow";
		// 获取验证码
		YYW_SENDVERIFSMS = urlString + "/yyw_sendverifysms.flow";
		// 注册
		YYW_REGISTER = urlString + "/yyw_register.flow";
		// 修改密码
		YYW_MODIFYPWD = urlString + "/yyw_updatepwd.flow";
		// 意见反馈
		YYW_SUGGEST = urlString + "/yyw_suggest.flow";
		// 拨打电话
		YYW_CALLPHONE = urlString + "/yyw_callphone.flow";
		// 获取通话记录
		YYW_GETCALL_LOGLIST = urlString + "/yyw_getcall_loglist.flow";
		// 获取看一看广告列表 yyw_getlookgglist
		YYW_GETLOOKGGLIST = urlString + "/yyw_getlookgglist.flow";
		// 获取看一看广告列表 yyw_getlookgglist_new(新版)
		YYW_GETLOOKGGLIST_NEW = urlString + "/yyw_getlookgglist_new.flow";
		// 获取看一看类型接口 yyw_getkyktype
		YYW_GETKYKTYPE = urlString + "/yyw_getkyktype_new.flow";
		// 获取看一看广告媒体信息yyw_getlookgginfo
		YYW_GETLOOKGGINFO = urlString + "/yyw_getlookgginfo.flow";
		// 领取看一看奖品yyw_getlookmoney
		YYW_GETLOOKMONEY = urlString + "/yyw_getlookmoney.flow";
		// 扫一扫获取奖品yyw_getsao
		YYW_GETSAO = urlString + "/yyw_getsao.flow";
		// 获取摇一摇广告列表 yyw_getyaogglist
		YYW_GETYAOGGLIST = urlString + "/yyw_getyaogglist.flow";
		// 摇一摇获取奖品yyw_getyao
		YYW_GETYAO = urlString + "/yyw_getyao.flow";
		// 摇一摇获取奖品yyw_getyao_new
		YYW_GETYAO_NEW = urlString + "/yyw_getyao_new.flow";
		// 摇一摇获取奖品yyw_getyao_new2 (最新摇一摇获奖流程)
		YYW_GETYAO_NEW2 = urlString + "/yyw_getyao_new2.flow";
		// 转一转奖品列表 yyw_getzhuanlist
		YYW_GETZHUANLIST = urlString + "/yyw_getzhuanlist.flow";
		// 转一转奖品列表 yyw_zyz_getlist(20150428新接口)
		YYW_ZYZ_GETLIST = urlString + "/yyw_zyz_getlist.flow";
		// 转一转抽奖 yyw_zhuanchoujiang
		YYW_ZHUANCHOUJIANG = urlString + "/yyw_zhuanchoujiang.flow";
		// 转一转抽奖 yyw_zyz_getwin(20150428新接口)
		YYW_ZYZ_GETWIN = urlString  + "/yyw_zyz_getwin.flow";
		// 财产操作记录查询接口 yyw_getcaichan
		YYW_GETCAICHAN = urlString + "/yyw_getcaichan.flow";
		// 兑换中心接口 yyw_duihuan
		YYW_DUIHUAN = urlString + "/yyw_duihuan.flow";
		// 商品类别获取接口 yyw_shop_getvarietylist
		YYW_SHOP_GETVARIETYLIST = urlString + "/yyw_shop_getvarietylist.flow";
		// 商品列表接口yyw_shangpin_list
		YYW_SHANGPIN_LIST = urlString + "/yyw_shangpin_list.flow";
		// 商品详情接口 yyw_shangpin_detail
		YYW_SHANGPIN_DETAIL = urlString + "/yyw_shangpin_detail.flow";
		// 加入购物车接口 yyw_shopping_add
		YYW_SHANGPIN_ADD = urlString + "/yyw_shopping_add.flow";
		// 查看购物车接口 yyw_shangpin_look
		YYW_SHANGPIN_LOOK = urlString + "/yyw_shangpin_look.flow";
		// 删除购物车接口 yyw_shopping_del
		YYW_SHANGPIN_DEL = urlString + "/yyw_shopping_del.flow";
		//删除购物车已完成记录 yyw_shopping_del2
		YYW_SHANGPIN_DEL2 = urlString + "/yyw_shopping_del2.flow";
		// 更新购物车商品数量接口 yyw_shopping_update
		YYW_SHANGPIN_UPDATE = urlString + "/yyw_shopping_update.flow";
		// 兑换商品接口 yyw_shopping_order
		YYW_SHANGPIN_ORDER = urlString + "/yyw_shopping_order.flow";
		// 家生活查询省份、城市、区域、小区、单元、房号接口
		YYW_GETHOUSE = urlString
				+ "/yyw_gethouse.flow";
		// 拨打电话
//		YYW_CALLPHONEGG = urlString + "/yyw_getcallphonegg.flow";
		// 获取用户信息
		YYW_USERINFO = urlString + "/yyw_userinfo.flow";
		// 修改个人资料
		YYW_USERINFO_UPDATE = urlString + "/yyw_userinfo_update.flow";
		// 家生活获取设备列表接口
		YYW_GETDEVICELIST = urlString + "/yyw_getdevicelist.flow";
		//获取全部省份列表
		YYW_GETAREALIST=urlString
				+ "yyw_getarealist.flow";
		//家生活绑定、修改设备接口
		YYW_SETDEVICE = urlString + "/yyw_setdevice.flow";
		//家生活删除设备接口
		YYW_DELDEVICE = urlString	+ "/yyw_deldevice.flow";
		//家生活获取物管信息接口 yyw_getwginfo
		YYW_GETWGINFO = urlString	+ "/yyw_getwginfo.flow";
		//注册2cu成功回调接口
		YYW_REG2CU = urlString+ "/yyw_reg2cu.flow";
		//获取兑换率规则接口 yyw_getrate
		YYW_GETRATE = urlString+ "/yyw_getrate.flow";
		YYW_HOMETIME=urlString+"/yyw_hometime.flow";
		// 获取特价商品列表 yyw_shangpin_discount
		YYW_SHANGPIN_DISCOUNT = urlString + "/yyw_shangpin_discount.flow";
		//获取冠名广告
		YYW_GETAD_NAEMED=urlString+"/yyw_getad_named.flow";
		//扫一扫领奖（新版 150429）
		YYW_SAO_GETWIN=urlString+"/yyw_sao_getwin.flow";
		//获取订单号
	    YYW_ZFB_CREATEORDER=urlString+"/yyw_zfb_createorder.flow";
	    //抢红包列表
	    YYW_GETREDPACKEDLIST=urlString+"/yyw_getredpackedlist.flow";
	    // 预约抢红包
	    YYW_MAKEROB=urlString+"/yyw_makerob.flow";
	    // 抢红包
	    YYW_ROBREDPACKED=urlString+"/yyw_robredpacked.flow";
	}
}