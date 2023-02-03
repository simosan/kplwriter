package com.simosan.kplapi.kplwriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class SimStreamManageServiceTest {
	
	private static final Logger log = LoggerFactory.getLogger(SimKplCheckArgsTest.class);
	
	// 事前にKinesisStreamを作成する
	@BeforeClass
	public static void setUp() {
		String cmd = "/opt/homebrew/bin/aws kinesis create-stream --stream-name simkpltest --shard-count 1 --profile localstack --endpoint-url http://192.168.11.31:4566";
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
		String cmd = "/opt/homebrew/bin/aws kinesis delete-stream --enforce-consumer-deletion --stream-name simkpltest --profile localstack --endpoint-url http://192.168.11.31:4566";
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
	
	/*@Test
	public void checkStreamStatusの正常系_Proxyなしパターン() throws Exception {
		String regionnm = "ap-northeast-1";
		String streamnm = "simkpltest";
		String profile = "simkai";
		SimStreamManageService ssms = new SimStreamManageService(regionnm, streamnm, profile);
		log.info("=================================================");
		log.info("checkStreamStatusの正常系_Proxyなしパターン");
		assertThat(ssms.checkStreamStatus(), is(0));
	}
	
	@Test
	public void checkStreamStatusの正常系_Proxyありホスト名パターン() throws Exception {
		String regionnm = "ap-northeast-1";
		String streamnm = "simkpltest";
		String profile = "localstack";
		String proxyhost = "localhost";
		String port = "58080";
		SimStreamManageService ssms = new SimStreamManageService(regionnm, streamnm, profile, proxyhost, port);
		log.info("=================================================");
		log.info("checkStreamStatusの正常系_Proxyありホスト名パターン");
		assertThat(ssms.checkStreamStatus(), is(0));
	}
	
	@Test
	public void checkStreamStatusの正常系_ProxyありIPパターン() throws Exception {
		String regionnm = "ap-northeast-1";
		String streamnm = "simkpltest";
		String profile = "localstack";
		String proxyhost = "192.168.11.90";
		String port = "58080";
		SimStreamManageService ssms = new SimStreamManageService(regionnm, streamnm, profile, proxyhost, port);
		log.info("=================================================");
		log.info("checkStreamStatusの正常系_ProxyありIPパターン");
		assertThat(ssms.checkStreamStatus(), is(0));
	}*/
	
	@Test
	public void getKinesisClientObjectの正常系_ProxyなしEndpointURIありパターン() throws Exception {
		String regionnm = "ap-northeast-1";
		String streamnm = "simkpltest";
		String profile = "localstack";
		String enduri = "http://simubu:4566";
		SimStreamManageService ssms = new SimStreamManageService(regionnm, streamnm, profile, enduri);
		log.info("=================================================");
		log.info("getKinesisClientObjectの正常系_ProxyなしEndpointURIありパターン");
		assertThat(ssms.getKinesisClientObject(),is(instanceOf(KinesisAsyncClient.class)));
	}
	
}
