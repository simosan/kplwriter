package com.simosan.kplapi.kplwriter;

import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.simosan.kplapi.model.SimMessages;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;

/**
 * 　Kinesis Streamメッセージ書き込みクラス
 *
 */
public class SimKplSender {
	
	private final static Logger log = LoggerFactory.getLogger(SimKplSender.class);
	private final KinesisAsyncClient kplclient;
	private final String streamname;
	private final String partkey;
	private final static Integer rtnzero = 0;
	private final static Integer rtnerr = 255;
	
	/**
	 * @param kplclient　kinesisクライアント
	 * @param streamname kinesisストリーム名
	 */
	SimKplSender(KinesisAsyncClient kplclient, String streamname){
		this.kplclient = kplclient;
		this.streamname = streamname;
		this.partkey = "test-kplwriter-0";
	}
	
	/**
	 * @param msg　kinesisへの出力メッセージ
	 * @return　0 or 255
	 */
	public Integer sendSimMessages(SimMessages msg) {
		byte[] bytes = msg.toJsonAsBytes();
		if (bytes == null) {
			log.error("SimKplSender.sendSimMessages : jsonデータが空です。");
			return rtnerr;
		}
		// このコードで使うストリームは1シャードのため固定値
		PutRecordRequest request = PutRecordRequest.builder()
                .partitionKey(partkey)
                .streamName(streamname)
                .data(SdkBytes.fromByteArray(bytes))
                .build();
		try {
			kplclient.putRecord(request).get();
        } catch (InterruptedException e) {
            log.warn("割り込み中断のため処理停止",e);
            return rtnerr;
        } catch (ExecutionException e) {
        	//処理は中断しない。
            log.error("Kinesisへのデータ送信中に例外が発生しました。次のサイクルで再試行します。", e);
        }
                
		return rtnzero;
	}
}
