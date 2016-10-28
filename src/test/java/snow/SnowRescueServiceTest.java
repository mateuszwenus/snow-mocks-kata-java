package snow;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import snow.dependencies.MunicipalServices;
import snow.dependencies.PressService;

@RunWith(MockitoJUnitRunner.class)
public class SnowRescueServiceTest {

	@Mock
	private MunicipalServices municipalServices;
	@Mock
	private PressService pressService;

	@Test
	public void should_throw_exception_when_WeatherForecastService_is_null() {
		try {
			// when
			new SnowRescueService(null, municipalServices, pressService);
			fail("should throw NullPointerException");
		} catch (NullPointerException expected) {
			// then
		}
	}
}
