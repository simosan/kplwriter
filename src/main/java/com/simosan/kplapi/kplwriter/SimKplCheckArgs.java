package com.simosan.kplapi.kplwriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * コマンドライン引数チェッククラス
 *
 */
public class SimKplCheckArgs {

	private static final Logger log = LoggerFactory.getLogger(SimKplCheckArgs.class);
	private static Integer rtnzero = 0;
	private static Integer rtnerr = 255;

	private static String ipregxstr = "10\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])"
			+ "\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])"
			+ "\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])"
			+ "|172\\.(1[6-9]|2[0-9]|3[0-1])\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])"
			+ "\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])"
			+ "|192\\.168\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])"
			+ "\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])";

	/**
	 * 引数チェック
	 * 
	 * @param args コマンドライン引数
	 * @return 0 or 255
	 */
	public static Integer checkUsage(String[] args) {
		// 引数数チェック(3 or 4 or 6)
		if ((args.length != 6) && (args.length != 4) && (args.length != 3)) {
			log.error("引数の数に誤りがあります（3 or 4 or 6です）");
			log.error("Usage: " + SimKplWriterMain.class.getSimpleName()
					+ " <stream name> <region> <profile> [<endpoint-uri> [<proxy hostname/IPAddress> <proxy port> ]]");
			return rtnerr;
		}

		// PROXY設定なし（EndpontURIあり）
		if (args.length == 4) {
			if (!args[3].matches("http.*"))
				return rtnerr;
		}

		// PROXY設定ありパターン
		if (args.length == 6) {
			// 第4引数のendpointuriは"dummy" or "http.*"かをチェック
			if (!args[3].equals("dummy") && !args[3].matches("http.*"))
				return rtnerr;
			// IPアドレスorHostnameかをチェック
			if (!chkIpaddressOrHost(args[4]))
				return rtnerr;
		}

		return rtnzero;
	}

	/**
	 * 引数5のIPアドレスorホスト名チェック
	 * 
	 * @param ipaddr IPアドレス or ホスト名
	 * @return true or false
	 */
	private static boolean chkIpaddressOrHost(final String ipaddr) {
		String regexip = "^[1-9][0-9].+";
		String regexhost = "^[a-zA-Z].+";
		if (ipaddr.matches(regexip)) {
			Pattern p = Pattern.compile(ipregxstr);
			Matcher m = p.matcher(ipaddr);
			if (!m.find()) {
				log.error("引数5のIPアドレスの指定に誤りがあります。");
				return false;
			}
		} else if (!ipaddr.matches(regexhost)) {
			log.error("引数5のホスト名の指定がおかしいです。");
			return false;
		}
		return true;
	}

}
