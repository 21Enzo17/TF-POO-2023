package ar.edu.unju.fi.tp9.service;


import java.io.ByteArrayOutputStream;


public interface IComprobanteGenerator {
    public ByteArrayOutputStream generarComprobante(String html);
} 
