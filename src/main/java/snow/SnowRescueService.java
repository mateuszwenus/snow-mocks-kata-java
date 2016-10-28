package snow;

import snow.dependencies.MunicipalServices;
import snow.dependencies.PressService;
import snow.dependencies.WeatherForecastService;

public class SnowRescueService {

	public SnowRescueService(WeatherForecastService weatherForecastService, MunicipalServices municipalServices,
			PressService pressService) {
		checkNotNull(weatherForecastService, "weatherForecastService");
		checkNotNull(municipalServices, "municipalServices");
		checkNotNull(pressService, "pressService");
	}

	private void checkNotNull(Object variableToCheck, String variableName) {
		if (variableToCheck == null) {
			throw new NullPointerException(variableName + " must not be null");
		}
	}

	public void checkForecastAndRescue() {
	}

}
