package com.simosan.kplapi.kplwriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class SimKplCheckArgsTest {
	
	private static final Logger log = LoggerFactory.getLogger(SimKplCheckArgsTest.class);
		
	@Test
	public void checkUsageの引数チェック正常系() throws Exception{
		String args1[] = new String[3]; //プロキシ指定なし（EndpointURIなし）
		String args2[] = new String[6]; //プロキシ指定あり
		String args3[] = new String[4]; //プロキシ指定なし（EndpointURIあり）
		
		//プロキシ指定なし(引数３）
		args1[0] = "testStream";
		args1[1] = "ap-northeast-1";
		args1[2] = "localstack";
		log.info("=================================================");
		log.info("checkUsageの引数チェック正常系-プロキシ指定なし（引数３）");
		assertThat(SimKplCheckArgs.checkUsage(args1), is(0));
		
		//プロキシ指定なし（endpointuriあり）(引数４）
		args3[0] = "testStream";
		args3[1] = "ap-northeast-1";
		args3[2] = "localstack";
		args3[3] = "http://localhost:4566";
		log.info("=================================================");
		log.info("checkUsageの引数チェック正常系-プロキシ指定なし（endpointuriあり）（引数4）");
		assertThat(SimKplCheckArgs.checkUsage(args3), is(0));
		
		//プロキシ指定あり(引数6）ホスト名パターン
		args2[0] = "testStream";
		args2[1] = "ap-northeast-1";
		args2[2] = "localstack";
		args2[3] = "dummy";
		args2[4] = "localhost";
		args2[5] = "8080";
		log.info("=================================================");
		log.info("checkUsageの引数チェック正常系-プロキシ指定あり（引数6）ホスト名パターン");
		assertThat(SimKplCheckArgs.checkUsage(args2), is(0));
		
		//プロキシ指定あり(引数6）IPアドレスパターン
		args2[0] = "testStream";
		args2[1] = "ap-northeast-1";
		args2[2] = "localstack";
		args2[3] = "dummy";
		args2[4] = "192.168.255.254";
		args2[5] = "8080";
		log.info("=================================================");
		log.info("checkUsageの引数チェック正常系-プロキシ指定あり（引数6）IPアドレスパターン");
		assertThat(SimKplCheckArgs.checkUsage(args2), is(0));
		
		//プロキシ指定あり(引数6）IPアドレスパターン && Endpointuri指定
		args2[0] = "testStream";
		args2[1] = "ap-northeast-1";
		args2[2] = "localstack";
		args2[3] = "http://localhost:4556";
		args2[4] = "192.168.255.254";
		args2[5] = "8080";
		log.info("=================================================");
		log.info("checkUsageの引数チェック正常系-プロキシ指定あり（引数6）IPアドレスパターン&&Endpointuri指定");
		assertThat(SimKplCheckArgs.checkUsage(args2), is(0));
	}
	
	@Test
	public void checkUsageの引数チェック異常系() throws Exception{
		String args1[] = new String[2];
		String args2[] = new String[7];
		String args3[] = new String[6];
		String args4[] = new String[6];
		
		//プロキシ指定なし(誤り：引数2）
		args1[0] = "testStream";
		args1[1] = "ap-northeast-1";
		log.info("=================================================");
		log.info("checkUsageの引数チェック異常系-プロキシ指定なし(誤り：引数2）");
		assertThat(SimKplCheckArgs.checkUsage(args1), is(255));
		
		//プロキシ指定あり(誤り：引数7）
		args2[0] = "testStream";
		args2[1] = "ap-northeast-1";
		args2[2] = "localstack";
		args2[3] = "dummy";
		args2[4] = "localhost";
		args2[5] = "8080";
		args2[6] = "hogehoge";
		log.info("=================================================");
		log.info("checkUsageの引数チェック異常系-プロキシ指定あり(誤り：引数7）");
		assertThat(SimKplCheckArgs.checkUsage(args2), is(255));
		
		//プロキシ指定あり(IPアドレスが変）
		args3[0] = "testStream";
		args3[1] = "ap-northeast-1";
		args3[2] = "localstack";
		args3[3] = "dummy";
		args3[4] = "192.168.";
		args3[5] = "8080";
		log.info("=================================================");
		log.info("checkUsageの引数チェック異常系-プロキシ指定あり(IPアドレスが変）");
		assertThat(SimKplCheckArgs.checkUsage(args3), is(255));
		
		//プロキシ指定あり(ホスト名が変）→本来的には本体のコードにホスト名規約を盛り込んだロジックを反映し、テストすべきである。
		args4[0] = "testStream";
		args4[1] = "ap-northeast-1";
		args4[2] = "localstack";
		args4[3] = "dummy";
		args4[4] = "@localhost";
		args4[5] = "8080";
		log.info("=================================================");
		log.info("checkUsageの引数チェック異常系-プロキシ指定あり(ホスト名が変）");
		assertThat(SimKplCheckArgs.checkUsage(args4), is(255));
		
		//プロキシ指定あり(EndpointURIが変）
		args4[0] = "testStream";
		args4[1] = "ap-northeast-1";
		args4[2] = "localstack";
		args4[3] = "hogehoge";
		args4[4] = "@localhost";
		args4[5] = "8080";
		log.info("=================================================");
		log.info("checkUsageの引数チェック異常系-プロキシ指定あり(EndpointURIが変）");
		assertThat(SimKplCheckArgs.checkUsage(args4), is(255));
		
	}
}
