package snow;

import snow.dependencies.MunicipalServices;
import snow.dependencies.PressService;
import snow.dependencies.SnowplowMalfunctioningException;
import snow.dependencies.WeatherForecastService;

public class SnowRescueService {

	public static final int LOW_TEMPERATURE = -1;
	public static final int HIGH_SNOW_FALL = 4;
	public static final int MAX_SNOWPLOW_ATTEMPTS = 10;

	private final WeatherForecastService weatherForecastService;
	private final MunicipalServices municipalServices;

	public SnowRescueService(WeatherForecastService weatherForecastService, MunicipalServices municipalServices,
			PressService pressService) {
		this.weatherForecastService = checkNotNull(weatherForecastService, "weatherForecastService");
		this.municipalServices = checkNotNull(municipalServices, "municipalServices");
		checkNotNull(pressService, "pressService");
	}

	private <T> T checkNotNull(T variableToCheck, String variableName) {
		if (variableToCheck == null) {
			throw new NullPointerException(variableName + " must not be null");
		}
		return variableToCheck;
	}

	public void checkForecastAndRescue() {
		if (weatherForecastService.getAverageTemperatureInCelsius() <= LOW_TEMPERATURE) {
			municipalServices.sendSander();
		}
		if (weatherForecastService.getSnowFallHeightInMM() >= 6) {
			sendSnowplow();
		}
		if (weatherForecastService.getSnowFallHeightInMM() >= HIGH_SNOW_FALL) {
			sendSnowplow();
		}
	}

	private void sendSnowplow() {
		int attempt = 0;
		boolean snowplowSuccessfull = false;
		while (!snowplowSuccessfull && attempt < MAX_SNOWPLOW_ATTEMPTS) {
			try {
				attempt++;
				municipalServices.sendSnowplow();
				snowplowSuccessfull = true;
			} catch (SnowplowMalfunctioningException e) {
			}
		}
	}

}
