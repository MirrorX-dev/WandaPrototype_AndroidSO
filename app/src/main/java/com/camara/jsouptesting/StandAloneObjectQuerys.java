package com.camara.jsouptesting;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.O)
public interface StandAloneObjectQuerys {
	public ArrayList<Integer> identificadores = new ArrayList<Integer>();
	LocalDate date = LocalDate.now(ZoneId.of("Europe/Madrid"));

	/**
	 * Este metodo permite identificar el partido m�s cercano a la fecha actual.
	 * @return Devuelve el identificador del partido m�s cercano. Ej: dc847028-f23b-3e8c-afa4-240910332928
	 */
	@RequiresApi(api = Build.VERSION_CODES.O)
	public static Object ObtenerDatosPartido_MasCercano() {
		String aux_id=null;
		try {
			if (Partido.partidos.size() != 0) {
				for (Partido p : Partido.partidos) {
					try {
						if (p.estadiopartido.equals("Wanda Metropolitano")) {
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
							Date date1 = dateFormat.parse(new DameFecha().dameDateAmericana(p.getFechapartido()));
							Date date2 = dateFormat.parse(String.valueOf(date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth()));

							if (date1.after(date2) || date1.equals(date2)) {
								System.out.println("El partido m�s cercano es: " + p);
								aux_id = p.getId_partido();
								break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		} catch (Exception error) {
			error.printStackTrace();
		}
		return aux_id;
	}


	/**
	 * Este metodo permite identificar el partido m�s cercano a la fecha actual.
	 * @return Devuelve el objeto Partido del array Partidos m�s cercano por su identificador.
	 */
	public static int ObtenerDatosPartido_MasCercano2() {
		int aux_id = -1;
		try {
			if (Partido.partidos.size() != 0) {
				for (int i = 0; i < Partido.partidos.size(); i++) {
					try {
						if (Partido.partidos.get(i).estadiopartido.equals("Wanda Metropolitano")) {
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
							Date date1 = dateFormat.parse(new DameFecha().dameDateAmericana(Partido.partidos.get(i).getFechapartido()));
							Date date2 = dateFormat.parse(String.valueOf(date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth()));

							if (date1.after(date2) || date1.equals(date2)) {
								System.out.println("El partido m�s cercano es: " + Partido.partidos.get(i));
								aux_id = i;
								break;
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception error) {
			error.printStackTrace();
		}
		return aux_id;
	}


	/**
	 * Este metodo permite identificar los partidos m�s cercanos a la fecha actual.
	 * @return Devuelve un listado de identificadores de Objetos Partidos < M�s cercanos a la fecha actual.
	 */
	public static Object ObtenerDatosPartidos_MasCercanos() {
		identificadores.clear();
		try {
			if (Partido.partidos.size() != 0) {
				for (int i = 0; i < Partido.partidos.size(); i++) {
					try {
						if (Partido.partidos.get(i).estadiopartido.equals("Wanda Metropolitano")) {
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
							Date date1 = dateFormat.parse(new DameFecha().dameDateAmericana(Partido.partidos.get(i).getFechapartido()));
							Date date2 = dateFormat.parse(String.valueOf(date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth()));

							if (date1.after(date2) || date1.equals(date2)) {
								identificadores.add(i);
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception error) {
			error.printStackTrace();
		}
		return identificadores;
	}

	/**
	 * Este metodo permite identificar los partidos m�s cercanos a la fecha actual Máximo 3 partidos.
	 * @return Devuelve un listado de identificadores de Objetos Partidos < M�s cercanos a la fecha actual.
	 */
	public static Object ObtenerDatosPartidos_MasCercanos2() {
		identificadores.clear();
		try {
			if (Partido.partidos.size()!=0) {
				for (int i = 0; i < Partido.partidos.size(); i++) {
					try {
						if (Partido.partidos.get(i).estadiopartido.equals("Wanda Metropolitano")) {
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
							Date date1 = dateFormat.parse(new DameFecha().dameDateAmericana(Partido.partidos.get(i).getFechapartido()));
							Date date2 = dateFormat.parse(String.valueOf(date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth()));

							if ((date1.after(date2) || date1.equals(date2)) && identificadores.size() != 3) {
								identificadores.add(i);
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception error) {
			error.printStackTrace();
		} finally {
		}
		return identificadores;
	}

	public static boolean LimpiarListaIdentificadores() {
		identificadores.clear();
		return true;
	}



}
