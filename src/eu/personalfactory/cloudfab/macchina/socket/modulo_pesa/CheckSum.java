package eu.personalfactory.cloudfab.macchina.socket.modulo_pesa;

public class CheckSum {

	public String risHexString;

	public CheckSum(int[] arrayInt) {

		String[] arrayStringa = new String[100];
		String risultatoBinario;
		double risultatoDecimale;

		for (int j = 0; j < arrayInt.length; j++) {
			try {
				arrayStringa[j] = convDecBin(arrayInt[j]); 
			} catch (Exception e) {
			}
		}

		risultatoBinario = CalcolaXOR(arrayStringa, arrayInt.length);
		risultatoDecimale = ConvertiDecimale(risultatoBinario, 8); 
		int risultatoDecimaleInt = (int) risultatoDecimale;
		risHexString = Integer.toHexString(risultatoDecimaleInt);
	}

	private static double ConvertiDecimale(String stringaInserita, int numero) {

		int stringa_convertita;
		double var_appoggio;
		double codice_decimale = 0;

		for (int i = 0; i < numero; i++) {
			String frammento = stringaInserita.substring((numero - 1) - i,
					numero - i);
			if (frammento.equals("0"))
				stringa_convertita = 0;
			else
				stringa_convertita = 1;
			double potenza = Math.pow(2.0, i);
			var_appoggio = stringa_convertita * potenza;
			codice_decimale += var_appoggio;
		}

		return codice_decimale;

	}

	/*
	 * L'operatore XOR, detto anche EX-OR, OR esclusivo o somma modulo 2,
	 * restituisce 1 se e solo se la somma degli operandi uguali ad 1 è dispari,
	 * mentre restituisce 0 in tutti gli altri casi.
	 */
	private static String CalcolaXOR(String[] arrayStringa, int lungArr) {

		int[] arrayIntC = new int[100];
		int[] arrayIntR = new int[100];
		String risultato = "";
		String s;
		for (int w = 0; w < 8; w++) {
			for (int j = 0; j < lungArr; j++) {
				Character temp = arrayStringa[j].charAt(w);
				s = temp.toString();
				arrayIntC[w] += Integer.parseInt(s);
			} 
			if ((arrayIntC[w] % 2) != 0) {
				arrayIntR[w] = 1;
			} else
				arrayIntR[w] = 0;
		}

		for (int w = 0; w < 8; w++) {
			risultato += arrayIntR[w];
		} 
		return risultato;

	}

	private static String convDecBin(int dev) {
		String resto = "";
		String rString = "";

		do {
			resto += dev % 2;
			dev /= 2;
		} while (dev != 0);

		for (int j = 0; j < (8 - resto.length()); j++) {
			rString += "0";
		}

		for (int i = resto.length(); i > 0; i--) {
			rString += resto.charAt(i - 1);
		}

		return (rString);

	}

}
