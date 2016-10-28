package snow;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import snow.dependencies.MunicipalServices;
import snow.dependencies.PressService;
import snow.dependencies.SnowplowMalfunctioningException;
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
		when(weatherForecastService.getAverageTemperatureInCelsius()).thenReturn(SnowRescueService.LOW_TEMPERATURE);
		// when
		snowRescueService.checkForecastAndRescue();
		// then
		verify(municipalServices).sendSander();
	}

	@Test
	public void should_not_send_sander_when_temperature_is_not_low() {
		// given
		SnowRescueService snowRescueService = new SnowRescueService(weatherForecastService, municipalServices, pressService);
		when(weatherForecastService.getAverageTemperatureInCelsius()).thenReturn(SnowRescueService.LOW_TEMPERATURE + 1);
		// when
		snowRescueService.checkForecastAndRescue();
		// then
		verify(municipalServices, never()).sendSander();
	}

	@Test
	public void should_send_snowplow_when_snow_fall_is_high() {
		// given
		SnowRescueService snowRescueService = new SnowRescueService(weatherForecastService, municipalServices, pressService);
		when(weatherForecastService.getSnowFallHeightInMM()).thenReturn(SnowRescueService.HIGH_SNOW_FALL);
		// when
		snowRescueService.checkForecastAndRescue();
		// then
		verify(municipalServices).sendSnowplow();
	}

	@Test
	public void should_not_send_snowplow_when_snow_fall_is_low() {
		// given
		SnowRescueService snowRescueService = new SnowRescueService(weatherForecastService, municipalServices, pressService);
		when(weatherForecastService.getSnowFallHeightInMM()).thenReturn(SnowRescueService.HIGH_SNOW_FALL - 1);
		// when
		snowRescueService.checkForecastAndRescue();
		// then
		verify(municipalServices, never()).sendSnowplow();
	}

	@Test
	public void should_send_second_snowplow_if_first_malfunctions() {
		// given
		SnowRescueService snowRescueService = new SnowRescueService(weatherForecastService, municipalServices, pressService);
		when(weatherForecastService.getSnowFallHeightInMM()).thenReturn(SnowRescueService.HIGH_SNOW_FALL);
		doThrow(SnowplowMalfunctioningException.class).doNothing().when(municipalServices).sendSnowplow();
		// when
		snowRescueService.checkForecastAndRescue();
		// then
		verify(municipalServices, times(2)).sendSnowplow();
	}

	@Test
	public void should_send_third_snowplow_if_first_and_second_mulfunction() {
		// given
		SnowRescueService snowRescueService = new SnowRescueService(weatherForecastService, municipalServices, pressService);
		when(weatherForecastService.getSnowFallHeightInMM()).thenReturn(SnowRescueService.HIGH_SNOW_FALL);
		doThrow(SnowplowMalfunctioningException.class).doThrow(SnowplowMalfunctioningException.class).doNothing().when(municipalServices).sendSnowplow();
		// when
		snowRescueService.checkForecastAndRescue();
		// then
		verify(municipalServices, times(3)).sendSnowplow();
	}

	@Test
	public void should_not_loop_forever_when_all_snowplows_malfunction() {
		// given
		SnowRescueService snowRescueService = new SnowRescueService(weatherForecastService, municipalServices, pressService);
		when(weatherForecastService.getSnowFallHeightInMM()).thenReturn(SnowRescueService.HIGH_SNOW_FALL);
		doThrow(SnowplowMalfunctioningException.class).when(municipalServices).sendSnowplow();
		// when
		snowRescueService.checkForecastAndRescue();
		// then
		verify(municipalServices, times(SnowRescueService.MAX_SNOWPLOW_ATTEMPTS)).sendSnowplow();
	}

	@Test
	public void should_send_two_snowplow_when_snow_fall_is_very_high() {
		// given
		SnowRescueService snowRescueService = new SnowRescueService(weatherForecastService, municipalServices, pressService);
		when(weatherForecastService.getSnowFallHeightInMM()).thenReturn(6);
		// when
		snowRescueService.checkForecastAndRescue();
		// then
		verify(municipalServices, times(2)).sendSnowplow();
	}
}
