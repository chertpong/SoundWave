package com.kritacademy.soundwave;

/**
 * Created by Chertpong on 7/9/2558.
 */
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class SoundAnalyzerTest {

    SoundAnalyzer analyzer;

    @Before
    public void setUp() throws Exception {
        analyzer = new SoundAnalyzer();
        try {
            analyzer.setMedia("F:/FFOutput/lost-stars.wav");

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void getLength_GivenWav4Min_ShouldReturn4Min(){
        assertThat(analyzer.getLength(), is(240));
    }

//    @Test
//    public void getAmplitude_1s_ShouldReturn10(){
//        assertThat(analyzer.getAmplitude(1),is(notNullValue()));
//    }

    @Test
    public void getAmplitudeVector_x200y100_ShouldReturnVector(){
        assertThat(analyzer.getAmplitudeVector(200,100), is(notNullValue()));
    }
}
