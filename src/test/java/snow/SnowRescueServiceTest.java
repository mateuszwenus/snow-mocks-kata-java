package snow;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import snow.dependencies.MunicipalServices;
import snow.dependencies.PressService;
import snow.dependencies.WeatherForecastService;

@RunWith(MockitoJUnitRunner.class)
public class SnowRescueServiceTest {

	@Mock
	private WeatherForecastService weatherForecastService;
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

	@Test
	public void should_throw_exception_when_MunicipalServices_is_null() {
		try {
			// when
			new SnowRescueService(weatherForecastService, null, pressService);
			fail("should throw NullPointerException");
		} catch (NullPointerException expected) {
			// then
		}
	}

	@Test
	public void should_throw_exception_when_PressService_is_null() {
		try {
			// when
			new SnowRescueService(weatherForecastService, municipalServices, null);
			fail("should throw NullPointerException");
		} catch (NullPointerException expected) {
			// then
		}
	}

	@Test
	public void should_send_sander_when_temperature_is_low() {
		// given
		SnowRescueService snowRescueService = new SnowRescueService(weatherForecastService, municipalServices, pressService);
		when(weatherForecastService.getAverageTemperatureInCelsius()).thenReturn(-1);
		// when
		snowRescueService.checkForecastAndRescue();
		// then
		verify(municipalServices).sendSander();
	}
}
