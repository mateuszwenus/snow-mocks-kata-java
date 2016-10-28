package snow;

import snow.dependencies.MunicipalServices;
import snow.dependencies.PressService;
import snow.dependencies.WeatherForecastService;

public class SnowRescueService {

	public SnowRescueService(WeatherForecastService weatherForecastService, MunicipalServices municipalServices, PressService pressService) {
		if (weatherForecastService == null) {
			throw new NullPointerException("weatherForecastService must not be null");
		}
		if (municipalServices == null) {
			throw new NullPointerException("municipalServices must not be null");
		}
	}

	public void checkForecastAndRescue() {
	}

}
