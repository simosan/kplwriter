package com.simosan.kplapi.kplwriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class SimMessageGeneratorTest {

	private static final Logger log = LoggerFactory.getLogger(SimMessageGeneratorTest.class);
	
	@Test
	public void SimMessageGenerator正常系() throws Exception{
		
		SimMessageGenerator smg = new SimMessageGenerator();
		log.info("=================================================");
		log.info("SimMessageGenerator正常系");
		assertThat(smg.getRandomMessage().toString(), notNullValue());
		log.info(smg.getRandomMessage().toString());
	}
}
