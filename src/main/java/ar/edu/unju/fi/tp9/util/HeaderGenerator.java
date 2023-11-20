package ar.edu.unju.fi.tp9.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class HeaderGenerator {

    /**
     * Metodo encargado de crear los headers para el archivo excel
     * @return HttpHeaders
     */
    public HttpHeaders crearHeadersExcel(){
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "Prestamos.xlsx");
        return headers;
    }

    /**
	 * Metodo encargado de crear los headers del pdf
	 * @param pdfBytes
	 * @return
	 */
	public HttpHeaders crearHeadersPdf(byte[] pdfBytes) {
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_PDF);
	    headers.setContentDispositionFormData("attachment", "Resumen.pdf");
	    headers.setContentLength(pdfBytes.length);
	    return headers;
	}
    
    /**
     * Metodo encargado de crear los headers para el archivo pdf
     * @return HttpHeaders
     */
    public HttpHeaders crearHeadersComprobantePdf(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "comprobante.pdf");
        return headers;
    }
}
