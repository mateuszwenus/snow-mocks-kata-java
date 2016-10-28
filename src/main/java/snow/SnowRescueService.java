package snow;

import snow.dependencies.MunicipalServices;
import snow.dependencies.PressService;
import snow.dependencies.SnowplowMalfunctioningException;
import snow.dependencies.WeatherForecastService;

public class SnowRescueService {

	public static final int LOW_TEMPERATURE = -1;
	public static final int HIGH_SNOW_FALL = 4;

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
		if (weatherForecastService.getSnowFallHeightInMM() >= HIGH_SNOW_FALL) {
			boolean snowplowSuccessfull = false;
			while (!snowplowSuccessfull) {
				try {
					municipalServices.sendSnowplow();
					snowplowSuccessfull = true;
				} catch (SnowplowMalfunctioningException e) {
				}
			}
		}
	}

}
