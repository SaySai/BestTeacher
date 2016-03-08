package utils;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
/*
 * 获得时间的工具类,先从网络获取时间,如果获取有问题,则取得系统时间
 */
public class TimeUtils {
	static SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");

	//获得当前时间
	public static String getCurrentTime() {
		String time = "";
		URL url;
		try {
			url = new URL("http://www.bjtime.cn");
			URLConnection uc = url.openConnection();// 生成连接对象
			uc.connect(); // 发出连接
			long ld = uc.getDate(); // 取得网站日期时间
			Date date = new Date(ld); // 转换为标准时间对象
			time = mDateFormat.format(date);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			time = mDateFormat.format(curDate);
		}// 取得资源对象

		return time;
	}
	
	
	//获得当前时间
		public static long getCurrentTimeLong() {
			long time = 0;
			URL url;
			try {
				url = new URL("http://www.bjtime.cn");
				URLConnection uc = url.openConnection();// 生成连接对象
				uc.connect(); // 发出连接
				time = uc.getDate(); // 取得网站日期时间
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
				time =curDate.getTime();
			}// 取得资源对象

			return time;
		}
	
}
