package com.wandaprototype.android.dataformat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.TimeZone;

public class DameFecha {

	@SuppressWarnings("unused")
	public String dameDateUtilAqui(java.util.Date fechaAlli) {
		String cadena = fechaAlli.toString();
		StringTokenizer st = new StringTokenizer(cadena, " ");
		String uno = st.nextToken();
		String dos = st.nextToken();
		String tres = st.nextToken();
		String cuatro = st.nextToken();
		String cinco = st.nextToken();
		String seis = st.nextToken();

		String aqui = tres + " " + dos + " " + cuatro + " " + seis;

		return aqui;
	}

	public String dameTimestampAqui(Timestamp fechaAlli) {
		String cadena = fechaAlli.toString();
		StringTokenizer st = new StringTokenizer(cadena, " ");
		String uno = st.nextToken();
		String dos = st.nextToken();
		st = new StringTokenizer(uno, "-");
		String tres = st.nextToken();
		String cuatro = st.nextToken();
		String cinco = st.nextToken();

		String aqui = cinco + "/" + cuatro + "/" + tres + " " + dos;

		return aqui;
	}

	public Timestamp dameTimestampAlli(String fechaTAqui) {
		StringTokenizer st = new StringTokenizer(fechaTAqui, " ");
		
		
		String uno = st.nextToken();
		st = new StringTokenizer(uno, "/");
		String dos = st.nextToken();
		st = new StringTokenizer(dos, "/");
		String tres = st.nextToken();
		st = new StringTokenizer("", "");
		
		

		String alli = tres + "-" + dos + "-" + uno+" 00";
		Timestamp fechaAlli = Timestamp.valueOf(alli);

		return fechaAlli;
	}

	public Date dameDateAlli(String fechaAqui) {
		StringTokenizer st = new StringTokenizer(fechaAqui, "/");
		String uno = st.nextToken();
		String dos = st.nextToken();
		String tres = st.nextToken();

		String alli = tres + "-" + dos + "-" + uno;
		Date fechaAlli = Date.valueOf(alli);

		return fechaAlli;
	}

	public String dameDateAqui(Date fechaAlli) {
		String cadena = fechaAlli.toString();
		StringTokenizer st = new StringTokenizer(cadena, "-");
		String uno = st.nextToken();
		String dos = st.nextToken();
		String tres = st.nextToken();

		String aqui = tres + "/" + dos + "/" + uno;

		return aqui;
	}
	
	public String dameDateAmericana(String fecha) {
		String cadena = fecha.toString();
		StringTokenizer st = new StringTokenizer(cadena, "/");
		String dia = st.nextToken();
		String mes = st.nextToken();
		String anho = st.nextToken();

		String aqui = anho + "-" + mes + "-" + dia;
		return aqui;
	}

	public static String iNeedLocalDateAndTime() {
		/*
		*	"yyyy.MM.dd G 'at' HH:mm:ss z" ---- 2001.07.04 AD at 12:08:56 PDT
		*	"hh 'o''clock' a, zzzz" ----------- 12 o'clock PM, Pacific Daylight Time
		*	"EEE, d MMM yyyy HH:mm:ss Z"------- Wed, 4 Jul 2001 12:08:56 -0700
		*	"yyyy-MM-dd'T'HH:mm:ss.SSSZ"------- 2001-07-04T12:08:56.235-0700
		*	"yyMMddHHmmssZ"-------------------- 010704120856-0700
		*	"K:mm a, z" ----------------------- 0:08 PM, PDT
		*	"h:mm a" -------------------------- 12:08 PM
		*	"EEE, MMM d, ''yy" ---------------- Wed, Jul 4, '01
		*/
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Madrid"));
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = df.format(now.getInstance().getTime());
		return date;
	}

}
