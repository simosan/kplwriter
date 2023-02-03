package com.simosan.kplapi.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * メッセージモデルクラス
 *
 */
public class SimMessages {

	
    private final static ObjectMapper JSON = new ObjectMapper();
    static {
        JSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private final String vulgarMsg;
    private final String meanIng;
    private final long id;

    /**
     * @param msg	出力メッセージ
     * @param imi 	メッセージの意味
     * @param id	メッセージ番号
     */
    public SimMessages(String msg, String imi, long id) {
        this.vulgarMsg = msg;
        this.meanIng = imi;
        this.id = id;
    }
    
    /**
     * メッセージID、メッセージ結合
     * 
     * @return　IDとメッセージと意味を結合した文字列
     */
    public String getIdAndVulgarMsg() {
        return id + "-" + vulgarMsg + "-" + meanIng;
    }

    /**
     * @return バイト型のメッセージ
     */
    public byte[] toJsonAsBytes() {
        try {
            return JSON.writeValueAsBytes(this);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("ID %d: %s %s", id, vulgarMsg, meanIng);
    }
	
}
