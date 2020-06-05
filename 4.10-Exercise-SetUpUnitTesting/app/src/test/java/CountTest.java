import com.example.android.clickcounter.ClickCounter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CountTest {

    private ClickCounter clickCounter;

    @Before
    public void setUpClickCounter() {
        clickCounter = new ClickCounter();
    }

    @Test
    public void verifyInitialCount() {
        Assert.assertEquals(clickCounter.getCount(), 0);
    }

    @Test
    public void verifyIncrement() {
        int previousValue = clickCounter.getCount();
        clickCounter.increment();
        int postValue = clickCounter.getCount();
        Assert.assertEquals(previousValue + 1, postValue);
    }
}
