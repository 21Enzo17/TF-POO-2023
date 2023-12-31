package ar.edu.unju.fi.tp9.service;

import java.io.ByteArrayOutputStream;

import org.springframework.core.io.InputStreamSource;

import ar.edu.unju.fi.tp9.exception.ManagerException;

public interface IEmailService {
	public void send(String emisor, String para, String tema, String cuerpo, InputStreamSource imagen, ByteArrayOutputStream comprobante) throws ManagerException;
}
