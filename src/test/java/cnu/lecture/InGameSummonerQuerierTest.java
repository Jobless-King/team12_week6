package cnu.lecture;

import org.junit.Before;
import org.junit.Test;import org.junit.internal.ExactComparisonCriteria;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cnu.lecture.InGameInfo.Observer;
import cnu.lecture.InGameInfo.Participant;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

import org.apache.http.client.HttpClient;

/**
 * Created by tchi on 2016. 4. 25..
 */
public class InGameSummonerQuerierTest {
	private InGameSummonerQuerier querier;

    @Before
    public void setup() {
        final String apiKey = "8242f154-342d-4b86-9642-dfa78cdb9d9c";
        GameParticipantListener dontCareListener = mock(GameParticipantListener.class);

        querier = new InGameSummonerQuerier(apiKey, dontCareListener);
    }

    @Test
    public void shouldQuerierIdentifyGameKeyWhenSpecificSummonerNameIsGiven() throws Exception {
        final String summonerName;
        InGameSummonerQuerier spy = spy(querier);
        GIVEN: {
            summonerName = "akane24";
            InGameInfo gameInfo = mock(InGameInfo.class);
            Observer observer = mock(Observer.class);
            when(gameInfo.getObservers()).thenReturn(observer);
            Participant[] participants = new Participant[1];
            participants[0] = mock(Participant.class);
            when(gameInfo.getParticipants()).thenReturn(participants);
            when(gameInfo.getObservers().getEncryptionKey()).thenReturn("4/bl4DC8HBir8w7bGHq6hvuHluBd+3xM");
            when(spy.getGameInfoFromHttp(anyString())).thenReturn(gameInfo);
        }

        final String actualGameKey;
        WHEN: {
            actualGameKey = spy.queryGameKey(summonerName);
        }

        final String expectedGameKey = "4/bl4DC8HBir8w7bGHq6hvuHluBd+3xM";
        THEN: {
            assertThat(actualGameKey, is(expectedGameKey));
            verify(spy, times(1)).getGameInfoFromHttp(anyString());
            verify(spy, times(1)).getSummonerIdFromHttp(anyString());
            verify(spy, times(1)).printInGmaePlayer(any());
        }
    }
    
    @Test
    public void shouldQuerierReportMoreThan5Summoners() {
    	final InGameInfo gameInfo;
    	GIVEN: {
	    	gameInfo = mock(InGameInfo.class);
	        Participant[] participants = new Participant[4];
	        for(int i=0; i<participants.length; ++i)
	        	participants[i] = mock(Participant.class);
	        when(gameInfo.getParticipants()).thenReturn(participants);
        }
    	
    	final boolean actualValue;
    	WHEN: {
        	actualValue = querier.isMax(gameInfo);
    	}
    	
    	final boolean expectedValue = true;
    	THEN: {
    		assertThat(expectedValue, is(actualValue));
    	}
    }
}
