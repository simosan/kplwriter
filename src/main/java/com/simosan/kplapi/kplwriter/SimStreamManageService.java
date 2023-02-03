package com.simosan.kplapi.kplwriter;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.ProxyConfiguration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.DescribeStreamRequest;
import software.amazon.awssdk.services.kinesis.model.DescribeStreamResponse;

/**
 * Kinesisストリーム書き込みサービスクラス
 *
 */
public class SimStreamManageService {

	private static final Logger log = LoggerFactory.getLogger(SimStreamManageService.class);
	private static final Integer rtnzero = 0;
	private static final Integer rtnerr = 255;
	private final KinesisAsyncClient kinesisClient;
	private final ProxyConfiguration proxy;
	private final SdkAsyncHttpClient httpclient;
	private final String streamName;

	/**
	 * プロキシなしパターン,KinesisEndpoinURI指定
	 * 
	 * @param regionName  AWSリージョン名
	 * @param streamName  kinesisストリーム名
	 * @param profile     AWSプロファイル
	 * @param endpointuri EndpointURI
	 */
	public SimStreamManageService(final String regionName, final String streamName, final String profile,
			final String endpointuri) {
		Region region = Region.of(regionName);
		this.kinesisClient = KinesisAsyncClient.builder().region(region).endpointOverride(URI.create(endpointuri))
				.credentialsProvider(ProfileCredentialsProvider.create(profile)).build();
		this.streamName = streamName;
		this.proxy = null;
		this.httpclient = null;
	}

	/**
	 * プロキシなしパターン,KinesisEndpoinURIなし
	 * 
	 * @param regionName AWSリージョン名
	 * @param streamName kinesisストリーム名
	 * @param profile    AWSプロファイル
	 */
	public SimStreamManageService(final String regionName, final String streamName, final String profile) {
		Region region = Region.of(regionName);
		this.kinesisClient = KinesisAsyncClient.builder().region(region)
				.credentialsProvider(ProfileCredentialsProvider.create(profile)).build();
		this.streamName = streamName;
		this.proxy = null;
		this.httpclient = null;
	}

	/**
	 * プロキシありパターン
	 * 
	 * @param regionName  AWSリージョン名
	 * @param streamName  kinesisストリーム名
	 * @param profile     AWSプロファイル
	 * @param endpointuri EndpointURI
	 * @param proxyaddr   プロキシアドレス
	 * @param proxyport   プロキシポート
	 */
	public SimStreamManageService(final String regionName, final String streamName, final String profile,
			final String endpointuri, final String proxyaddr, String proxyport) {

		Region region = Region.of(regionName);
		this.proxy = ProxyConfiguration.builder().host(proxyaddr).port(Integer.parseInt(proxyport)).build();
		this.httpclient = NettyNioAsyncHttpClient.builder().proxyConfiguration(this.proxy).build();
		this.kinesisClient = KinesisAsyncClient.builder().region(region)
				.credentialsProvider(ProfileCredentialsProvider.create(profile)).httpClient(httpclient).build();
		this.streamName = streamName;
	}

	/**
	 * 対象のKinesisストリームがACTIVEになっているかをチェックする
	 * 
	 * @return　0 or 255
	 */
	public Integer checkStreamStatus() {

		try {
			DescribeStreamRequest describeStreamRequest = DescribeStreamRequest.builder().streamName(streamName)
					.build();
			DescribeStreamResponse describeStreamResponse = kinesisClient.describeStream(describeStreamRequest).get();
			if (!describeStreamResponse.streamDescription().streamStatus().toString().equals("ACTIVE")) {
				log.error("Stream " + streamName + " がACTIVEではありません。");
				return rtnerr;
			}
		} catch (Exception e) {
			log.error("SimCheckStreamStatus.checkStreamStatusで例外発生。 " + streamName, e);
			return rtnerr;
		}
		return rtnzero;
	}

	/**
	 * 対象のKinesisストリームクライアントオブジェクトを返却する（データPUTのために）
	 * 
	 * @return kinesisクライアントオブジェクト
	 */
	public KinesisAsyncClient getKinesisClientObject() {
		return this.kinesisClient;
	}
}
