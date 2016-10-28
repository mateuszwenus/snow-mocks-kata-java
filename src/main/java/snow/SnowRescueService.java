package snow;

import snow.dependencies.MunicipalServices;
import snow.dependencies.PressService;
import snow.dependencies.WeatherForecastService;

public class SnowRescueService {

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
		if (weatherForecastService.getAverageTemperatureInCelsius() < 0) {
			municipalServices.sendSander();
		}
	}

}
