package com.simosan.kplapi.kplwriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import com.simosan.kplapi.model.SimMessages;


/**
 * メッセージランダム出力クラス
 *
 */
public class SimMessageGenerator {

	// 絵文字のリスト
	private static final List<SimMessageVal> SIM_MSGS = new ArrayList<SimMessageVal>();
    static {
    	SIM_MSGS.add(new SimMessageVal("よりめ", "( ◑ٹ◐)　"));
    	SIM_MSGS.add(new SimMessageVal("はなれめ", "(⊙◞౪◟⊙)"));
    	SIM_MSGS.add(new SimMessageVal("しょぼん", "(´･ω･`)"));
    	SIM_MSGS.add(new SimMessageVal("きもなき1", "༼;´༎ຶ ༎ຶ༽"));
    	SIM_MSGS.add(new SimMessageVal("きもなき2", "༼;´༎ຶ ۝ ༎ຶ ༽"));
    	SIM_MSGS.add(new SimMessageVal("きもなき3", "v(;´༎ຶД༎ຶ`)v"));
    	SIM_MSGS.add(new SimMessageVal("きも顔1", "(´◉◞౪◟◉)"));
    	SIM_MSGS.add(new SimMessageVal("きも顔2", "(◉◞౪◟◉｀)"));
    	SIM_MSGS.add(new SimMessageVal("うまー", "( ﾟДﾟ)"));
    	SIM_MSGS.add(new SimMessageVal("うぇーい", "(☝ ՞ਊ ՞)☝ｳｨｰ"));
    }

    private final Random random = new Random();
    private AtomicLong id = new AtomicLong(0);
    
    /**
     * メッセージ取得
     * 
     * @return　SimMessagesオブジェクト
     */
    public SimMessages getRandomMessage() {
    	// 絵文字をランダムにチョイス
    	SimMessageVal simmsgVal = SIM_MSGS.get(random.nextInt(SIM_MSGS.size()));
    	// idを１個づつインクリメントしつつ、メッセージをオブジェクトで返却する。
    	// Debug
    	System.out.printf("%s - %s %n",simmsgVal.vulgarLang, simmsgVal.meaning);
    	
        return new SimMessages(simmsgVal.vulgarLang, simmsgVal.meaning, id.getAndIncrement());
    }

    /**
     * メッセージ保持プライベートクラス
     *
     */
    private static class SimMessageVal {
    	private final String vulgarLang;
        private final String meaning;
        
        SimMessageVal(String vulgarLang, String meaning) {
            this.vulgarLang = vulgarLang;
            this.meaning = meaning;
        }
    }
}
