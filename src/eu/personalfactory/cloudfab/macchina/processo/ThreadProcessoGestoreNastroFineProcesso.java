/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_OttieniStatoIngresso;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_NASTRO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_FINECORSA_NASTRO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
//import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TEMPO_LAMPEGGIAMENTO_NASTRO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_PULSANTE_STOP; 
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PULIZIA_LIMITE_POS53;

/**
 *
 * @author Francesco Di Gaudio
 */
public class ThreadProcessoGestoreNastroFineProcesso extends Thread {

	int tempo_attivazione_nastro; 
	 
	public ThreadProcessoGestoreNastroFineProcesso(int tempo_attivazione_nastro) {
		super();
		this.tempo_attivazione_nastro = tempo_attivazione_nastro;
	}



	@Override
	public void run() {

		boolean interrompi_processo_nastro = false;

		boolean nastro_attivato= false;
		int counter = 0;
		//int counter_lampeggiamento = 0;
		int divisioni_tempo = 500;
		int tempo_vuoto = tempo_attivazione_nastro / divisioni_tempo;
		//int tempo_lampeggiamento = TEMPO_LAMPEGGIAMENTO_NASTRO / divisioni_tempo;

		//System.out.println("tempo_attivazione_nastro" + tempo_attivazione_nastro);
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// ATTIVAZIONE
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// NASTRO
		//GestoreIO_ModificaOut(USCITA_LOGICA_NASTRO + OUTPUT_SEP_CHAR + USCITA_LOGICA_SEGNALE_LUMINOSO_GIALLO,
		//		OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_TRUE_CHAR);
		
		GestoreIO_ModificaOut(USCITA_LOGICA_NASTRO,
				OUTPUT_TRUE_CHAR);
		
		nastro_attivato = true;
		

		while (!interrompi_processo_nastro) {
			 
			if (GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_FINECORSA_NASTRO)) {

				/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// DISATTIVAZIONE
				/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// NASTRO
				if (nastro_attivato) {
				GestoreIO_ModificaOut(USCITA_LOGICA_NASTRO, OUTPUT_FALSE_CHAR);
				
				nastro_attivato = false; 
				
				if (tempo_attivazione_nastro != PULIZIA_LIMITE_POS53) {
					
					interrompi_processo_nastro = true;
					
					
					}
				}
				counter = 0;

			} else {

				/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// ATTIVAZIONE
				/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// NASTRO
				if (!nastro_attivato) {
					
					nastro_attivato= true; 
					
				GestoreIO_ModificaOut(USCITA_LOGICA_NASTRO, OUTPUT_TRUE_CHAR);
				}
				
				counter++;
				interrompi_processo_nastro = counter > tempo_vuoto;
				  
			}

			if (GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_PULSANTE_STOP)) {

				interrompi_processo_nastro = true;
			}
			
			try {
				ThreadProcessoGestoreNastroFineProcesso.sleep(divisioni_tempo);
			} catch (InterruptedException e) {
			}
			
//			counter_lampeggiamento++;
//			
//			if (counter_lampeggiamento>tempo_lampeggiamento) {
//				/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// DISATTIVAZIONE
//				/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// LAMPEGGIANTE
//				GestoreIO_ModificaOut(USCITA_LOGICA_SEGNALE_LUMINOSO_GIALLO, OUTPUT_FALSE_CHAR);
//			}

		}

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// DISATTIVAZIONE
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// NASTRO
		//GestoreIO_ModificaOut(USCITA_LOGICA_NASTRO + OUTPUT_SEP_CHAR + USCITA_LOGICA_SEGNALE_LUMINOSO_GIALLO,
		//		OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR);
		GestoreIO_ModificaOut(USCITA_LOGICA_NASTRO,
				OUTPUT_FALSE_CHAR);

	}

}
