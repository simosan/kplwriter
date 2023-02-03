package com.simosan.kplapi.kplwriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sim
 */
class SimKplWriterMain {

	private static final Logger log = LoggerFactory.getLogger(SimKplWriterMain.class);

	/**
	 *  メインメソッド
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {

		String streamname = null;
		String regionname = null;
		String profile = null;
		String endpointuri = null;
		String hostname = null;
		String port = null;

		// 引数チェック
		Integer rtncode = SimKplCheckArgs.checkUsage(args);
		if (0 != rtncode) {
			log.error("SimKplCheckArgs.checkUsageでエラー発生。");
			System.exit(1);
		}

		SimStreamManageService ssms = null;

		// Stream作成（プロキシなし、EndpointURIなし）
		if (args.length == 3) {
			// 引数
			streamname = args[0];
			regionname = args[1];
			profile = args[2];
			ssms = new SimStreamManageService(regionname, streamname, profile);
			// Stream作成（プロキシなし、EndpointURIあり）
		} else if (args.length == 4) {
			// 引数
			streamname = args[0];
			regionname = args[1];
			profile = args[2];
			endpointuri = args[3];
			ssms = new SimStreamManageService(regionname, streamname, profile, endpointuri);
			// Stream作成（プロキシあり）
		} else if (args.length == 6) {
			// 引数
			streamname = args[0];
			regionname = args[1];
			profile = args[2];
			endpointuri = args[3];
			hostname = args[4];
			port = args[5];
			ssms = new SimStreamManageService(regionname, streamname, profile, endpointuri, hostname, port);
		}

		Integer rtn = ssms.checkStreamStatus();
		if (rtn != 0) {
			log.error("SimStreamManageService.checkStreamStatusでエラー発生。");
			System.exit(1);
		}
		// SimMessage用インスタンスの作成
		SimMessageGenerator smg = new SimMessageGenerator();
		// KPL転送インスタンスの作成
		SimKplSender sks = new SimKplSender(ssms.getKinesisClientObject(),streamname);
		while (true) {
			sks.sendSimMessages(smg.getRandomMessage());
			log.info("SimKplWriterMainを処理しています");
			Thread.sleep(3000);
		}
	}
}
