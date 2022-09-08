package a04.tugasakhirfarmasi.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name = "serviceResponse", namespace = "http://www.yale.edu/tp/cas")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceResponse {
    @XmlElement(name = "authenticationFailure", namespace = "http://www.yale.edu/tp/cas")
    private String authenticationFailure;

    @XmlElement(name = "authenticationSuccess", namespace = "http://www.yale.edu/tp/cas")
    private AuthenticationSuccess authenticationSuccess;
}
