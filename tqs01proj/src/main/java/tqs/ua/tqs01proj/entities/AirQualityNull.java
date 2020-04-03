package tqs.ua.tqs01proj.entities;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.Id;

// TODO: maybe remover reason,
// cause "Note, that when we set reason, Spring calls HttpServletResponse.sendError(). Therefore, it will send an HTML error page to the client, which makes it a bad fit for REST endpoints."
@ResponseStatus(value = HttpStatus.NOT_FOUND) //reason = "non existing city")
public  class AirQualityNull extends RuntimeException{


}
