package snow;

import snow.dependencies.MunicipalServices;
import snow.dependencies.PressService;
import snow.dependencies.SnowplowMalfunctioningException;
import snow.dependencies.WeatherForecastService;

public class SnowRescueService {

	public static final int LOW_TEMPERATURE = -1;
	public static final int CRITICAL_TEMPERATURE = -11;
	public static final int HIGH_SNOW_FALL = 4;
	public static final int VERY_HIGH_SNOW_FALL = 6;
	public static final int CRITICAL_SNOW_FALL = 11;
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
		int averageTemperatureInCelsius = weatherForecastService.getAverageTemperatureInCelsius();
		int snowFallHeightInMM = weatherForecastService.getSnowFallHeightInMM();
		if (isCriticalWeatherConditions(averageTemperatureInCelsius, snowFallHeightInMM)) {
			handleCriticalWeatherConditions();
		} else {
			handleNoncriticalWeatherConditions(averageTemperatureInCelsius, snowFallHeightInMM);
		}
	}

	private void handleCriticalWeatherConditions() {
		sendSnowplows(3);
		municipalServices.sendSander();
	}

	private void handleNoncriticalWeatherConditions(int averageTemperatureInCelsius, int snowFallHeightInMM) {
		if (isLowTemperature(averageTemperatureInCelsius)) {
			municipalServices.sendSander();
		}
		if (isVeryHighSnowFall(snowFallHeightInMM)) {
			sendSnowplows(2);
		} else if (isHighSnowFall(snowFallHeightInMM)) {
			sendSnowplows(1);
		}
	}

	private boolean isCriticalWeatherConditions(int averageTemperatureInCelsius, int snowFallHeightInMM) {
		return averageTemperatureInCelsius <= CRITICAL_TEMPERATURE && snowFallHeightInMM >= CRITICAL_SNOW_FALL;
	}

	private boolean isLowTemperature(int averageTemperatureInCelsius) {
		return averageTemperatureInCelsius <= LOW_TEMPERATURE;
	}

	private boolean isVeryHighSnowFall(int snowFallHeightInMM) {
		return snowFallHeightInMM >= VERY_HIGH_SNOW_FALL;
	}

	private boolean isHighSnowFall(int snowFallHeightInMM) {
		return snowFallHeightInMM >= HIGH_SNOW_FALL;
	}

	private void sendSnowplows(int numberOfSnowplowsToSend) {
		int attempt = 0;
		int numberOfSuccessfullSnowplows = 0;
		while (numberOfSuccessfullSnowplows < numberOfSnowplowsToSend && attempt < MAX_SNOWPLOW_ATTEMPTS) {
			try {
				attempt++;
				municipalServices.sendSnowplow();
				numberOfSuccessfullSnowplows++;
			} catch (SnowplowMalfunctioningException e) {
			}
		}
	}

}
