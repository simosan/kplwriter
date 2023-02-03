package com.simosan.kplapi.kplwriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class SimKplSenderTest {
	
	private static final Logger log = LoggerFactory.getLogger(SimKplSenderTest.class);
	
	// 事前にKinesisStreamを作成する
	@BeforeClass
	public static void setUp() {
		String cmd = "/opt/homebrew/bin/aws kinesis create-stream --stream-name simkpltest --shard-count 1 --profile localstack --endpoint-url http://simubu:4566";
		try {
			log.info("Streamを作成します");
			Runtime runtime = Runtime.getRuntime();
			//@SuppressWarnings("deprecation")
			Process p = runtime.exec(cmd);
			p.waitFor();
			p.destroy();
			//ストリームの作成に時間がかかるのでスリープ（awsコマンドでステータスみてもいいけどめんどくさいのでそこまでやらん）
			Thread.sleep(10000);
		} catch (Exception e) {
			log.error("Streamの作成に失敗しました!!!!");
			e.printStackTrace();
		}
	}
	//テスト終わったらKinesisStream消す
	@AfterClass
	public static void tearDown() {
		String cmd = "/opt/homebrew/bin/aws kinesis delete-stream --enforce-consumer-deletion --stream-name simkpltest --profile localstack --endpoint-url http://simubu:4566";
		try {
			log.info("Streamを削除します");
			Runtime runtime = Runtime.getRuntime();
			//@SuppressWarnings("deprecation")
			Process p = runtime.exec(cmd);
			p.waitFor();
			p.destroy();
			Thread.sleep(10000);
		} catch (Exception e) {
			log.error("Streamの削除に失敗しました!!!!");
			e.printStackTrace();
		}
	}
	
	@Test
	public void SimKplSenderTest正常系() throws Exception{
		String regionnm = "ap-northeast-1";
		String streamnm = "simkpltest";
		String profile = "simkai";
        String enduri = "http://simubu:4566";
		SimStreamManageService ssms = new SimStreamManageService(regionnm, streamnm, profile, enduri);
		SimMessageGenerator smg = new SimMessageGenerator();
		SimKplSender sks = new SimKplSender(ssms.getKinesisClientObject(), streamnm);
		log.info("=================================================");
		assertThat(sks.sendSimMessages(smg.getRandomMessage()),is(0));
	}
}
