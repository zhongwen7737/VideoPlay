package com.videoplay.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class StringUtil {
	String str = "^[1-9][0-9]{5}$";

	/**
	 * 判断邮编
	 * 
	 * @param paramString
	 * @return
	 */
	public static boolean isZipNO(String zipString) {
		String str = "^[1-9][0-9]{5}$";
		return Pattern.compile(str).matcher(zipString).matches();
	}

	/**
	 * 判断字符串是否是數字
	 * 
	 * @author lvliuyan
	 */

	private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	private final static Pattern phone = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断是否是手机号
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhoneNumber(String phoneNumber) {
		boolean isValid = false;
		String expression = "^1[3|5|7|8][0-9]{9}$";
		CharSequence inputStr = phoneNumber;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * 判断字符串是否包含中文
	 * 
	 * @author lvliuyan
	 */
	public static final boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}

	private static final boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}


	public static int strToInt(String number) {
		int n = 0;
		try {
			n = Integer.valueOf(number);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return n;
	}

	public static long strToLong(String number) {
		long n = 0;
		try {
			n = Long.valueOf(number);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return n;
	}

	public static BigDecimal strToBigDecimal(String number) {
		BigDecimal n = null;
		try {
			n = new BigDecimal(number);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return n;
	}

	public static double strToDouble(String number) {
		double n = 0l;
		try {
			n = Double.valueOf(number);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return n;
	}

	/**
	 * 根据毫秒值返回字符串.e.g."0天0小时0分钟0秒"
	 * 
	 * @param millisSecond
	 * @return
	 */
	public static String millisToString(long millisSecond) {
		int s = 1000;
		int m = 60 * s;
		int h = 60 * m;
		int d = 24 * h;
		StringBuffer sb = new StringBuffer();
		if (millisSecond / d > 0) {
			sb.append(millisSecond / d);
			sb.append("天");
		}
		sb.append(millisSecond % d / h);
		sb.append("小时");
		sb.append(millisSecond % d % h / m);
		sb.append("分钟");
		sb.append(millisSecond % d % h % m / s);
		sb.append("秒");
		return sb + "";
	}

	/***
	 * 将输入金额num转换为汉字大写格式
	 * 
	 * @param num
	 *            输入金额（小于10000000）
	 * @return 金额的大写格式
	 */
	public static String transferPriceToHanzi(String number) {
		BigDecimal num;
		if (TextUtils.isEmpty(number.trim())) {
			return "零";
		} else if (number.startsWith("-")) {
			return "输入金额不能为负数";
		} else {
			num = new BigDecimal(number.trim());
		}
		String title = "人民币:";
		String[] upChinese = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", };
		String[] upChinese2 = { "分", "角", "圆", "拾", "佰", "仟", "萬", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆" };
		StringBuffer result = new StringBuffer();
		int count = 0;
		int zeroflag = 0;
		boolean mantissa = false;
		if (num.compareTo(BigDecimal.ZERO) < 0) {
			// 输入值小于零
			return "输入金额不能为负数";
		}
		if (num.compareTo(BigDecimal.ZERO) == 0) {
			// 输入值等于零
			return "零";
		}
		if (String.valueOf(num).length() > 12) { // 输入值过大转为科学计数法本方法无法转换
			return "您输入的金额过大";
		}
		BigDecimal temp = num.multiply(BigDecimal.TEN.pow(2));
		BigDecimal[] divideAndRemainder = temp.divideAndRemainder(BigDecimal.TEN.pow(2));
		if (divideAndRemainder[1].compareTo(BigDecimal.ZERO) == 0) {
			// 金额为整时
			if (temp.compareTo(BigDecimal.ZERO) == 0)
				return "您输入的金额过小";
			// 输入额为e:0.0012小于分计量单位时
			result.insert(0, "整");
			temp = temp.divide(BigDecimal.TEN.pow(2));
			count = 2;
			mantissa = true;
		}
		while (temp.compareTo(BigDecimal.ZERO) > 0) {
			BigDecimal[] divideAndRemainder2 = temp.divideAndRemainder(BigDecimal.TEN);
			BigDecimal t = divideAndRemainder2[1];
			// 取得最后一位
			if (t.compareTo(BigDecimal.ZERO) != 0) {
				// 最后一位不为零时
				if (zeroflag >= 1) {
					// 对该位前的单个或多个零位进行处理
					if (((!mantissa) && count == 1)) {
						// 不是整数金额且分为为零
					} else if (count > 2 && count - zeroflag < 2) {
						// 输入金额为400.04小数点前后都有零

						result.insert(1, "零");
					} else if (count > 6 && count - zeroflag < 6 && count < 10) {
						// 万位后为零且万位为零
						if (count - zeroflag == 2) {
							// 输入值如400000
							result.insert(0, "萬");
						} else {
							result.insert(0, "萬零");
							// 输入值如400101
						}
					} else if (count > 10 && count - zeroflag < 10) {
						if (count - zeroflag == 2) {
							result.insert(0, "亿");
						} else {
							result.insert(0, "亿零");
						}
					} else if (((count - zeroflag) == 2)) {
						// 个位为零
					} else if (count > 6 && count - zeroflag == 6 && count < 10) { // 以万位开始出现零如4001000
						result.insert(0, "萬");
					} else if (count == 11 && zeroflag == 1) {
						result.insert(0, "亿");
					} else {
						result.insert(0, "零");
					}
				}
				result.insert(0, upChinese[t.intValue()] + upChinese2[count]);
				zeroflag = 0;
			} else {
				if (count == 2) {
					result.insert(0, upChinese2[count]);
					// 个位为零补上"圆"字
				}
				zeroflag++;
			}
			BigDecimal[] divideAndRemainder3 = temp.divideAndRemainder(BigDecimal.TEN);
			temp = divideAndRemainder3[0];
			System.out.println("count=" + count + "---zero=" + zeroflag + "----" + result + "");
			count++;
			if (count > 14)
				break;
		}
		return title + result + "";
	}

	// /**
	// * 判断字符串是否为手机号
	// *
	// * @author 吕柳燕
	// * @param phoneNumber
	// * 判断的字符串
	// * */
	public static boolean isPhoneNumberValid(String phoneNumber) {

		boolean isValid = false;
		/*
		 * 可接受的电话格式有：
		 */
		String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
		/*
		 * 可接受的电话格式有：
		 */
		String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
		CharSequence inputStr = phoneNumber;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);

		Pattern pattern2 = Pattern.compile(expression2);
		Matcher matcher2 = pattern2.matcher(inputStr);
		if (matcher.matches() || matcher2.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * 判断字符串是否为邮箱
	 * 
	 * @author 吕柳燕
	 * @param email
	 *            判断的字符串
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	public static String removeAllSpace(String str) {
		String tmpstr = str.replace(" ", "");
		tmpstr = tmpstr.replace("+86", "");
		tmpstr = tmpstr.replace("-", "");
		return tmpstr;
	}

	/**
	 * 判断当前是否有网络
	 * 
	 * @author lvliuyan
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 根据手机分辨率从 px(像素) 单位 转成 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 根据手机分辨率从 dp 单位 转成 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 是否是身份证号
	 */
	public static boolean isCard(String s_aStr) {
		String str = "\\d{17}[0-9a-zA-Z]|\\d{14}[0-9a-zA-Z]";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(s_aStr);
		return m.matches();
	}

	public static final String[] province = { "北京", "天津", "河北", "山西", "内蒙古", "辽宁", "吉林", "黑龙江", "上海", "江苏", "浙江", "安徽",
			"福建", "江西", "山东", "河南", "湖北", "湖南", "广东", "广西", "海南", "重庆", "四川", "贵州", "云南", "西藏", "陕西", "甘肃", "青海", "宁夏",
			"新疆", "香港", "澳门", "台湾" };
	public static final String[][] city_all = {
			{ "海淀", "朝阳", "东城", "西城", "崇文", "宣武", "丰台", "石景山", "房山", "门头沟", "通州", "顺义", "昌平", "密云", "怀柔", "延庆", "平谷",
					"大兴" },
			{ "和平", "西青", "北辰", "南开", "河东", "河西", "河北", "津南", "红桥", "东丽", "宝坻", "蓟县", "武清", "宁河", "静海", "滨海新区" },
			{ "石家庄", "唐山", "秦皇岛", "邯郸", "邢台", "保定", "张家口", "承德", "沧州", "廊坊", "衡水" },
			{ "太原", "大同", "阳泉", "长治", "晋城", "朔州", "晋中", "运城", "忻州", "临汾", "吕梁" },
			{ "呼和浩特", "包头", "乌海", "赤峰", "通辽", "鄂尔多斯", "呼伦贝尔", "巴彦淖尔", "乌兰察布", "兴安盟", "锡林郭勒盟", "阿拉善盟" },
			{ "沈阳", "大连", "鞍山", "抚顺", "本溪", "丹东", "锦州", "营口", "阜新", "辽阳", "盘锦", "铁岭", "朝阳", "葫芦岛" },
			{ "长春", "吉林", "四平", "辽源", "通化", "白山", "松原", "白城", "延边" },
			{ "哈尔滨", "齐齐哈尔", "鸡西", "鹤岗", "双鸭山", "大庆", "伊春", "佳木斯", "七台河", "牡丹江", "黑河", "绥化", "大兴安岭" },
			{ "浦东", "嘉定", "宝山", "闵行", "青浦", "松江", "普陀", "徐汇", "杨浦", "虹口", "闸北", "奉贤", "金山", "黄浦", "静安", "卢湾", "崇明",
					"长宁", "其他", "昆山", "吴江", "嘉兴" },
			{ "南京", "无锡", "徐州", "常州", "苏州", "南通", "连云港", "淮安", "盐城", "扬州", "镇江", "泰州", "宿迁" },
			{ "杭州", "宁波", "温州", "嘉兴", "湖州", "绍兴", "金华", "衢州", "舟山", "台州", "丽水" },
			{ "合肥", "芜湖", "蚌埠", "淮南", "马鞍山", "淮北", "铜陵", "安庆", "黄山", "滁州", "阜阳", "宿州", "巢湖", "六安", "亳州", "池州", "宣城" },
			{ "福州", "厦门", "莆田", "三明", "泉州", "漳州", "南平", "龙岩", "宁德" },
			{ "南昌", "景德镇", "萍乡", "九江", "新余", "鹰潭", "赣州", "吉安", "宜春", "抚州", "上饶" },
			{ "济南", "青岛", "淄博", "枣庄", "东营", "烟台", "潍坊", "济宁", "泰安", "威海", "日照", "莱芜", "临沂", "德州", "聊城", "滨州", "荷泽" },
			{ "郑州", "开封", "洛阳", "平顶山", "安阳", "鹤壁", "新乡", "焦作", "濮阳", "", "许昌", "漯河", "三门峡", "南阳", "商丘", "信阳", "周口",
					"驻马店" },
			{ "武汉", "黄石", "十堰", "宜昌", "襄樊", "鄂州", "荆门", "孝感", "荆州", "黄冈", "咸宁", "随州", "恩施", "神农架" },
			{ "长沙", "株洲", "湘潭", "衡阳", "邵阳", "岳阳", "常德", "张家界", "益阳", "郴州", "永州", "怀化", "娄底", "湘西" },
			{ "广州", "韶关", "深圳", "珠海", "汕头", "佛山", "江门", "湛江", "茂名", "肇庆", "惠州", "梅州", "汕尾", "河源", "阳江", "清远", "东莞",
					"中山", "潮州", "揭阳", "云浮" },
			{ "南宁", "柳州", "桂林", "梧州", "北海", "防城港", "钦州", "贵港", "玉林", "百色", "贺州", "河池", "来宾", "崇左" }, { "海口", "三亚" },
			{ "南岸", "渝北", "大渡口", "北碚", "沙坪坝", "巴南", "江北", "九龙坡", "渝中", "北部新区", "高新区" },
			{ "成都", "自贡", "攀枝花", "泸州", "德阳", "绵阳", "广元", "遂宁", "内江", "乐山", "南充", "眉山", "宜宾", "广安", "达州雅安", "巴中", "资阳",
					"阿坝", "甘孜", "凉山" },
			{ "贵阳", "六盘水", "遵义", "安顺", "铜仁", "黔西南", "毕节", "黔东南", "黔南" },
			{ "昆明", "曲靖", "玉溪", "保山", "昭通", "丽江", "思茅", "临沧", "楚雄", "红河", "文山", "西双版纳", "大理", "德宏", "怒江", "迪庆" },
			{ "拉萨", "昌都", "山南日喀则", "那曲", "阿里", "林芝" }, { "西安", "铜川", "宝鸡", "咸阳", "渭南", "延安", "汉中", "榆林", "安康", "商洛" },
			{ "兰州", "嘉峪关", "金昌", "白银", "天水", "武威张掖", "平凉", "酒泉", "庆阳", "定西", "陇南", "临夏", "甘南" },
			{ "西宁", "海东", "海北", "黄南", "海南", "果洛", "玉树", "海西" }, { "银川", "石嘴山", "吴忠", "固原", "中卫" },
			{ "乌鲁木齐", "克拉玛依", "吐鲁番", "哈密", "昌吉", "博尔塔拉", "巴音郭楞", "阿克苏", "克孜勒苏", "喀什", "和田", "伊犁", "塔城", "阿勒泰", "石河子",
					"阿拉尔", "图木舒克", "五家渠" },
			{ "香港" }, { "澳门" }, { "台湾" } };

	public static String[] citysString = new String[] { "北京", "天津", "上海", "重庆", "石家庄", "唐山", "秦皇岛", "邯郸", "邢台", "保定",
			"张家口", "承德", "沧州", "廊坊", "衡水", "太原", "大同", "阳泉", "长治", "晋城", "朔州", "晋中", "运城", "忻州", "临汾", "吕梁", "台北", "高雄",
			"基隆", "台中", "台南", "新竹", "嘉义", "沈阳", "大连", "鞍山", "抚顺", "本溪", "丹东", "锦州", "营口", "阜新", "辽阳", "盘锦", "铁岭", "朝阳",
			"葫芦岛", "长春", "吉林", "四平", "辽源", "通化", "白山", "松原", "白城", "延边朝鲜族自治州", "哈尔滨", "齐齐哈尔", "鹤岗", "双鸭山", "鸡西", "大庆",
			"伊春", "牡丹江", "佳木斯", "七台河", "黑河", "绥化", "大兴安岭", "南京", "无锡", "徐州", "常州", "苏州", "南通", "连云港", "淮安", "盐城", "扬州",
			"镇江", "泰州", "宿迁", "杭州", "宁波", "温州", "嘉兴", "湖州", "绍兴", "金华", "衢州", "舟山", "台州", "丽水", "合肥", "芜湖", "蚌埠", "淮南",
			"马鞍山", "淮北", "铜陵", "安庆", "黄山", "滁州", "阜阳", "宿州", "巢湖", "六安", "亳州", "池州", "宣城", "福州", "厦门", "莆田", "三明", "泉州",
			"漳州", "南平", "龙岩", "宁德", "南昌", "景德镇", "萍乡", "九江", "新余", "鹰潭", "赣州", "吉安", "宜春", "抚州", "上饶", "济南", "青岛", "淄博",
			"枣庄", "东营", "烟台", "潍坊", "济宁", "泰安", "威海", "日照", "莱芜", "临沂", "德州", "聊城", "滨州", "菏泽", "郑州", "开封", "洛阳", "平顶山",
			"安阳", "鹤壁", "新乡", "焦作", "濮阳", "许昌", "漯河", "三门峡", "南阳", "商丘", "信阳", "周口", "驻马店", "济源", "武汉", "黄石", "十堰",
			"荆州", "宜昌", "襄樊", "鄂州", "荆门", "孝感", "黄冈", "咸宁", "随州", "仙桃", "天门", "潜江", "神农架", "恩施", "长沙", "株洲", "湘潭", "衡阳",
			"邵阳", "岳阳", "常德", "张家界", "益阳", "郴州", "永州", "怀化", "娄底", "湘西", "广州", "深圳", "珠海", "汕头", "韶关", "佛山", "江门", "湛江",
			"茂名", "肇庆", "惠州", "梅州", "汕尾", "河源", "阳江", "清远", "东莞", "中山", "潮州", "揭阳", "云浮", "兰州", "金昌", "白银", "天水", "嘉峪关",
			"武威", "张掖", "平凉", "酒泉", "庆阳", "定西", "陇南", "成都", "自贡", "攀枝花", "泸州", "德阳", "绵阳", "广元", "遂宁", "内江", "乐山", "南充",
			"眉山", "宜宾", "广安", "达州", "雅安", "巴中", "资阳", "阿坝", "甘孜", "凉山", "济南", "青岛", "淄博", "枣庄", "东营", "烟台", "潍坊", "济宁",
			"泰安", "威海", "日照", "莱芜", "临沂", "德州", "聊城", "滨州", "菏泽", "贵阳", "六盘水", "遵义", "安顺", "铜仁", "毕节", "黔西南布依族苗族自治州",
			"黔东南苗族侗族自治州", "黔南布依族苗族自治州", "海口", "三亚", "五指山", "琼海", "儋州", "文昌", "万宁", "东方", "昆明", "靖市", "玉溪", "保山", "昭通",
			"丽江", "思茅", "临沧", "文山壮族苗族自治州", "红河哈尼族彝族自治州", "西双版纳傣族自治州", "楚雄彝族自治州", "大理白族自治州", "德宏傣族景颇族自治州", "怒江傈傈族自治州",
			"迪庆藏族自治州", "西宁", "海东", "海北藏族自治州", "黄南藏族自治州", "海南藏族自治州", "果洛藏族自治州", "玉树藏族自治州", "海西蒙古族藏族自治州", "西安", "铜川",
			"宝鸡", "咸阳", "渭南", "延安", "汉中", "榆林", "安康", "商洛", "南宁", "柳州", "桂林", "梧州", "北海", "防城港", "钦州", "贵港", "玉林", "百色",
			"贺州", "河池", "来宾", "崇左", "拉萨", "那曲", "昌都", "山南", "日喀则", "阿里", "林芝", "银川", "石嘴山", "吴忠", "固原", "中卫", "乌鲁木齐",
			"克拉玛依市", "石河子", "阿拉尔", "图木舒克", "五家渠", "吐鲁番", "阿克苏", "喀什", "哈密", "和田", "阿图什", "库尔勒", "昌吉", "阜康", "米泉", "博乐",
			"伊宁", "奎屯", "塔城", "乌苏", "阿勒泰", "呼和浩特", "包头", "乌海", "赤峰", "通辽", "鄂尔多斯", "呼伦贝尔", "巴彦淖尔", "乌兰察布", "澳门", "香港" };

	public static void ToastTool(Context context, String content) {
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}

	public static float getDensity(Context context) {
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		return dm.density;
	}

	public static int getDensityWdith(Context context) {
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		return dm.widthPixels;
	}

	public static int getDensityHeight(Context context) {
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		return dm.heightPixels;
	}

	public static void dismiss(PopupWindow p) {
		if (p != null && p.isShowing()) {
			p.dismiss();
		}
	}



	public static String getAppSecret(Context context) {
		ApplicationInfo applicationInfo = null;
		String appSecret = null;
		try {
			applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
					PackageManager.GET_META_DATA);
			appSecret = applicationInfo.metaData.getString("app_secret");
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return appSecret;
	}

	/**
	 * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 */
	public static boolean isEmpty(CharSequence input) {
		if (input == null || "".equals(input) || "null".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是不是一个合法的电子邮件地址
	 */
	public static boolean isEmail(CharSequence email) {
		if (isEmpty(email))
			return false;
		return emailer.matcher(email).matches();
	}

	/**
	 * 判断是不是一个合法的手机号码
	 */
	public static boolean isPhone(CharSequence phoneNum) {
		if (isEmpty(phoneNum))
			return false;
		return phone.matcher(phoneNum).matches();
	}

	/**
	 * 字符串转整数
	 * 
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
		}
		return defValue;
	}

	/**
	 * 对象转整
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if (obj == null)
			return 0;
		return toInt(obj + "", 0);
	}

	/**
	 * String转long
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static long toLong(String obj) {
		try {
			return Long.parseLong(obj);
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * String转double
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static double toDouble(String obj) {
		try {
			return Double.parseDouble(obj);
		} catch (Exception e) {
		}
		return 0D;
	}

	/**
	 * 字符串转布尔
	 * 
	 * @param b
	 * @return 转换异常返回 false
	 */
	public static boolean toBool(String b) {
		try {
			return Boolean.parseBoolean(b);
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 判断一个字符串是不是数字
	 */
	public static boolean isNumber(CharSequence str) {
		try {
			Integer.parseInt(str + "");
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * byte[]数组转换为16进制的字符串。
	 * 
	 * @param data
	 *            要转换的字节数组。
	 * @return 转换后的结果。
	 */
	public static final String byteArrayToHexString(byte[] data) {
		StringBuilder sb = new StringBuilder(data.length * 2);
		for (byte b : data) {
			int v = b & 0xff;
			if (v < 16) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(v));
		}
		return sb + "".toUpperCase(Locale.getDefault());
	}

	/**
	 * 16进制表示的字符串转换为字节数组。
	 * 
	 * @param s
	 *            16进制表示的字符串
	 * @return byte[] 字节数组
	 */
	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] d = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
			d[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return d;
	}

	public static void dissPw(PopupWindow pw) {
		if (pw != null && pw.isShowing()) {
			pw.dismiss();
		}
	}

	public static void setDrawbleLeft(Context context, TextView view, int rsd) {
		Drawable drawable = context.getResources().getDrawable(rsd);
		// / 这一步必须要做,否则不会显示.
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		view.setCompoundDrawables(drawable, null, null, null);
	}

	public static void setDrawbleRight(Context context, TextView view, int rsd) {
		Drawable drawable = context.getResources().getDrawable(rsd);
		// / 这一步必须要做,否则不会显示.
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		view.setCompoundDrawables(null, null, drawable, null);
	}

	public static void setDrawbleTop(Context context, TextView view, int rsd) {
		Drawable drawable = context.getResources().getDrawable(rsd);
		// / 这一步必须要做,否则不会显示.
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		view.setCompoundDrawables(null, drawable, null, null);
	}

	public static String removeBiFuhao(String str) {
		String tmpstr = str.replace(" ", "");
		tmpstr = tmpstr.replace("￥", "");
		tmpstr = tmpstr.replace("$", "");
		tmpstr = tmpstr.replace("元", "");
		return tmpstr;
	}

	/**
	 * 把图片资源保存到本地
	 * 
	 * @param context
	 * @param filename
	 *            文件名
	 * @param dra
	 *            本地图片资源id
	 * @return
	 */
	public static String initImagePath(Activity context, String filename, int dra) {
		String imagepath;
		try {
			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
					&& Environment.getExternalStorageDirectory().exists()) {
				imagepath = Environment.getExternalStorageDirectory().getAbsolutePath() + filename;
			} else {
				imagepath = context.getApplication().getFilesDir().getAbsolutePath() + filename;
			}
			File file = new File(imagepath);
			if (!file.exists()) {
				file.createNewFile();
				Bitmap pic = BitmapFactory.decodeResource(context.getResources(), dra);
				FileOutputStream fos = new FileOutputStream(file);
				pic.compress(CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			imagepath = null;
		}
		return imagepath;
	}


	public static String getString(Object o) {
		String str = o + "";
		str = str.replace("null", "");
		return str;
	}

	/**
	 * 实现文本复制功能
	 * 
	 * @param content
	 *            要复制的内容
	 */
	@SuppressLint("NewApi")
	public static void copy(String content, Context context) {
		// 得到剪贴板管理器
		ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setText(content.trim());
	}

	// 隐藏软键盘
	public static void hideSoftKeyboard(Activity context) {
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (context.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (context.getCurrentFocus() != null){
				inputMethodManager.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(),
						0);
			}
		}
	}

	// 隐藏软键盘
	public static void hideSoftKeyboard(Context context,EditText text) {
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(text.getWindowToken(),0);
	}

	public static void showSoftKeyboard(Context context,EditText text){
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.showSoftInput(text,0);
	}

//	/**
//	 * 发短息，直接发
//	 *
//	 * @param phoneNumber
//	 * @param message
//	 */
//
//	public static void sendSMS1(String phoneNumber, String message) {
//		// 获取短信管理器
//		android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
//		// 拆分短信内容（手机短信长度限制）
//		List<String> divideContents = smsManager.divideMessage(message);
//		for (String text : divideContents) {
//			smsManager.sendTextMessage(phoneNumber, null, text, null, null);//
//		}
//	}

	/**
	 * 编辑内容，发短息
	 * 
	 * @param phoneNumber
	 * @param message
	 */
	public static void sendSMS2(String phoneNumber, String message, Context context) {
		// 获取短信管理器
		Uri uri = Uri.parse("smsto:" + phoneNumber);
		Intent it = new Intent(Intent.ACTION_SENDTO, uri);
		it.putExtra("sms_body", message);
		context.startActivity(it);
	}

	public static void sendSMS(String phoneNumber, String message, Context context){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.putExtra("address", phoneNumber);
		intent.putExtra("sms_body", message);
		intent.setType("vnd.android-dir/mms-sms");
		context.startActivity(intent);
	}

	/**
	 * sha256加密2
	 * 
	 * @param phoneNumber
	 * @param message
	 */
	public static String hmac_sha256(String value, String key) {
		try {
			// Get an hmac_sha256 key from the raw key bytes
			byte[] keyBytes = key.getBytes();
			SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA256");

			// Get an hmac_sha256 Mac instance and initialize with the signing
			// key
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(signingKey);

			// Compute the hmac on input data bytes
			byte[] rawHmac = mac.doFinal(value.getBytes());

			// Convert raw bytes to Hex
			String hexBytes = byte2hex(rawHmac);
			return hexBytes;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String byte2hex(final byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0xFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs;
	}

	public static String getStrToStr(String[] strs) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strs.length; i++) {
			sb.append(strs[i] + ",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb + "";
	}

	public static String getPicUrl(String logo) {
		return null;
	}

	
	public static String getXiaoShu(String d) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
		String s = df.format(strToDouble(d));
		return s;
	}
	public static String getZhengshu(String d) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("0");
		String s = df.format(strToDouble(d));
		return s;
	}

}
