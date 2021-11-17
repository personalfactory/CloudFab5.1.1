/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.stampante.CitronixCIJ_Client;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.Prodotto;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AdattaInfoPesoTeorico;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaValoreParametriRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ApprossimaValoreVelInverter;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConteggiaConfezioniEffettuate;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.LetturaDettagliComponenti;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaValoreParProdottoMacchinaPerIdParametroPerIdProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIndiceTabellaRipristinoPerIdParRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaTuttiValoriParametroConfezionePerIdCategoriaPerNumeroSacchetti;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.VerificaLunghezzaStringa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEFAULT_CORRETTIVO_TEMPI_PRODOTTO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEFAULT_CORRETTIVO_VEL_PRODOTTO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_CHAR_SEPARATOR; 
/**
 *
 * @author francescodigaudio
 *
 * Lettura dei Parametri Utilizzati per la produzione
 *
 */
public class ThreadProcessoLetturaParametriProcesso extends Thread {

    private final Processo processo;
    private int numeroVelScarico;
    private int numPuntiRilievoScarico;
    private String[][] parametriConfezioni;

    //COSTRUTTORE
    public ThreadProcessoLetturaParametriProcesso(Processo processo) {

        //Dichiarazione Varibale Processo
        this.processo = processo;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Inizio Lettura Parametri Processo");

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// LETTURA PARAMETRI PRODOTTO
        //Lettura Parametri Prodotto
        processo.prodotto = new Prodotto(Integer.parseInt(TrovaSingoloValoreParametroRipristino(1)));

        //Memorizzazione Log Processo
        ProcessoLogger.logger.log(Level.INFO,
                "Parametri Carico{0}Id del prodotto ={1}{2}Codice del prodotto ={3}{4}Nome del prodotto ={5}{6}Numero di miscele ={7}{8}"
                + "Numero di sacchi ={9}{10}Peso singolo sacco ={11}{12}Variazione sul cemento (Grigio o Bianco) ={13}{14}"
                + "Variazione sul contenitore (Secchio o Sacco) ={15}{16}Variazione sulla chimica (Confezionata o Sfusa) ={17}{18} Codice Chimica (Confezionata o Sfusa) ={10}",
                new Object[]{
                    LOG_CHAR_SEPARATOR,
                    processo.prodotto.getId(),
                    LOG_CHAR_SEPARATOR,
                    processo.prodotto.getCodiceProdotto(),
                    LOG_CHAR_SEPARATOR,
                    processo.prodotto.getNome(),
                    LOG_CHAR_SEPARATOR,
                    TrovaSingoloValoreParametroRipristino(8),
                    LOG_CHAR_SEPARATOR,
                    TrovaSingoloValoreParametroRipristino(9),
                    LOG_CHAR_SEPARATOR,
                    TrovaSingoloValoreParametroRipristino(10),
                    LOG_CHAR_SEPARATOR,
                    TrovaSingoloValoreParametroRipristino(12),
                    LOG_CHAR_SEPARATOR,
                    TrovaSingoloValoreParametroRipristino(13),
                    LOG_CHAR_SEPARATOR,
                    TrovaSingoloValoreParametroRipristino(14),
                    LOG_CHAR_SEPARATOR,
                    processo.prodotto.getCodiceChimica()});

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// LETTURA CORRETTIVI
        //Lettura parametri correttivi impostati manualmente sulla macchina - PRESTAZIONI
        letturaCorrettivi();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// LETTURA INFORMAZIONI MATERIE PRIME
        //Lettura del Numero di Materie Prime 
        processo.numeroMateriePrime = Integer.parseInt(TrovaSingoloValoreParametroRipristino(15));

        //Lettura Dettagli 
        processo.componenti = LetturaDettagliComponenti();

        /////////////////////////////////////////
        // Aggiornamento Contatore Confezioni  //
        /////////////////////////////////////////
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        String date = DATE_FORMAT.format(new Date());
        int res = ConteggiaConfezioniEffettuate(processo.prodotto.getCodiceProdotto(), date);

        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(441))) {

            //Aggiorna Valore Contatore Indice Sacco
            AggiornaValoreParametriRipristino(processo,
                    TrovaIndiceTabellaRipristinoPerIdParRipristino(47),
                    47,
                    Integer.toString(res),
                    ParametriSingolaMacchina.parametri.get(15));

        }

        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(454))) {
            //Configurazione Stampante
            new ThreadConfiguraStampante(processo, res).start();
        }

        String formula_teorica = "";
        for (int i = 0; i < processo.componenti.size(); i++) {

            //Memorizzazione Log Processo
            ProcessoLogger.logger.log(Level.INFO,
                    "iComponente {0}{1}Nome Componente = {2}{3}Id Componente = {4}{5}Presa Componente = {6}{7}"
                    + "Quantita Componente = {8}{9}Correttivo Vel Componente = {10}{11}Volo Componente = {12}"
                    + "{13}Quantita Intervento Inverter = {14}{15}Curva Vel= {16}{17} "
                    + "Limiti Cambio Vel= {18}{19} TolleranzaEccesso = {20}{21} Tolleranza Diffetto = {22}",
                    new Object[]{
                        i,
                        LOG_CHAR_SEPARATOR,
                        processo.componenti.get(i).getNome(),
                        LOG_CHAR_SEPARATOR,
                        processo.componenti.get(i).getId(),
                        LOG_CHAR_SEPARATOR,
                        processo.componenti.get(i).getPresa(),
                        LOG_CHAR_SEPARATOR,
                        processo.componenti.get(i).getQuantità(),
                        LOG_CHAR_SEPARATOR,
                        processo.componenti.get(i).getCorrettivoVelocità(),
                        LOG_CHAR_SEPARATOR,
                        processo.componenti.get(i).getVolo(),
                        LOG_CHAR_SEPARATOR,
                        processo.componenti.get(i).getQuantitàSecondaVelocitàInverter(),
                        LOG_CHAR_SEPARATOR,
                        processo.componenti.get(i).getVelocità(),
                        LOG_CHAR_SEPARATOR,
                        processo.componenti.get(i).getLimitiCambioVelocità(),
                        LOG_CHAR_SEPARATOR,
                        processo.componenti.get(i).getTolleranzaEccesso(),
                        LOG_CHAR_SEPARATOR,
                        processo.componenti.get(i).getTolleranzaDifetto()});

            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(364))) {
                ////////////////////////////////////////////////////////
                /// REGISTRAZIONE FORMULA TEORICA - INFO AGGIUNTIVE  ///
                ////////////////////////////////////////////////////////

                formula_teorica += processo.componenti.get(i).getId() + ParametriGlobali.parametri.get(22) + AdattaInfoPesoTeorico(processo.componenti.get(i).getQuantità());

                if (i < (processo.numeroMateriePrime - 1)) {
                    formula_teorica += ParametriGlobali.parametri.get(21);
                }

            }

        }

        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(364))) {
            ////////////////////////////////////////////////////////
            /// REGISTRAZIONE FORMULA TEORICA - INFO AGGIUNTIVE  ///
            ////////////////////////////////////////////////////////

            //Indice Codice Sacchetto Inserito
            AggiornaValoreParametriRipristino(processo,
                    TrovaIndiceTabellaRipristinoPerIdParRipristino(80),
                    80,
                    formula_teorica,
                    ParametriSingolaMacchina.parametri.get(15));

        }

        // Configurazione microdosatori se presenti e avvio controlli iniziali
        new ThreadProcessoGestoreMicrodosaturaConfigurazione(processo).start();
  
        //Dichiarazione Array List
        processo.aliqScarico = new ArrayList<>();

        //Lettura numero punti di rilievo Pesa Confezioni  
        numPuntiRilievoScarico = Integer.parseInt(ParametriSingolaMacchina.parametri.get(48));

        //Lettura aliquote Pesa Confezioni 
        for (int i = 0; i < numPuntiRilievoScarico; i++) {
            processo.aliqScarico.add(Integer.parseInt(ParametriSingolaMacchina.parametri.get(50 + i)));

        }

        //Lettura Parametri Pesa Confezioni
        parametriConfezioni = TrovaTuttiValoriParametroConfezionePerIdCategoriaPerNumeroSacchetti(processo.prodotto.getIdCategoria(),
                Integer.parseInt(TrovaSingoloValoreParametroRipristino(9)));

        numeroVelScarico = Integer.parseInt(ParametriSingolaMacchina.parametri.get(49));

        processo.numConfezioniPerMiscela = Integer.parseInt(TrovaSingoloValoreParametroRipristino(9));

        //Dichiarazione ArrayParametri
        processo.velocitaMiscelatoreFaseScarico = new int[processo.numConfezioniPerMiscela][numeroVelScarico];
        processo.limiti = new int[processo.numConfezioniPerMiscela][Integer.parseInt(ParametriGlobali.parametri.get(60))];
        processo.tempiApertura = new int[processo.numConfezioniPerMiscela][Integer.parseInt(ParametriGlobali.parametri.get(60))];
        processo.tempiChiusura = new int[processo.numConfezioniPerMiscela][Integer.parseInt(ParametriGlobali.parametri.get(60))];

        processo.parSecchioVelocita = new double[processo.numConfezioniPerMiscela];
        processo.parSecchioCorrettivoTempi = new double[processo.numConfezioniPerMiscela];
        processo.pesoDaRaggiungereConfezione = new double[processo.numConfezioniPerMiscela];
        processo.materialeInizioPesaturaFine = new double[processo.numConfezioniPerMiscela];
        processo.tempoSvuotamentoTubo = new int[processo.numConfezioniPerMiscela];
        processo.materialeMediamenteNelTubo = new int[processo.numConfezioniPerMiscela];
        processo.materialeDosaturaSoloVibro = new int[processo.numConfezioniPerMiscela];
        processo.curvaDosaggioCocleaExt = new String[processo.numConfezioniPerMiscela];

        //Lettura indice tipo contenitore : Sacco o Secchio
        processo.confezionamentoInSecchio = Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(13));

        processo.parProdottoCorrettivoVelocita = Integer.parseInt(DEFAULT_CORRETTIVO_VEL_PRODOTTO);
        processo.parProdottoCorrettivoTempi = Integer.parseInt(DEFAULT_CORRETTIVO_TEMPI_PRODOTTO);

        try {
            //Lettura indice Correttivo Velocita Prodotto
            processo.parProdottoCorrettivoVelocita
                    = Integer.parseInt(TrovaValoreParProdottoMacchinaPerIdParametroPerIdProdotto(processo.prodotto.getId(), 1));

            //Lettura indice Correttivo Tempi Prodotto
            processo.parProdottoCorrettivoTempi = Integer.parseInt(TrovaValoreParProdottoMacchinaPerIdParametroPerIdProdotto(processo.prodotto.getId(), 2));

        } catch (NumberFormatException ex) {

            ProcessoLogger.logger.severe("Errore durante la lettura dei correttivi tempi e velocita prodotto");
        }

        ProcessoLogger.logger.log(Level.INFO, "Vibro Valvola Attivo= {0}", processo.prodotto.isVibroAttivo());

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// LETTURA PARAMETRI CONFEZIONAMENTO - SECCHI
        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Lettura Parametri Confezionamento: Parametri Secchi");

        //Lettura Parametri Secchi
        for (int j = 0; j < processo.numConfezioniPerMiscela; j++) {

            //Lettura Quantità Pesatura Solo Vibro                
            processo.materialeDosaturaSoloVibro[j] = Integer.parseInt(parametriConfezioni[42][j]);

            if (Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(82))) {
                //Lettura Curva coclea Estrattrice
                processo.curvaDosaggioCocleaExt[j] = parametriConfezioni[43][j];
            }

            //Lettura Vel Confezionamento in Secchio
            processo.parSecchioVelocita[j] = Integer.parseInt(parametriConfezioni[17][j])
                    * (processo.parPrestazioneVelocitaScarico / 100)
                    * (processo.parProdottoCorrettivoVelocita / 100);

            //Lettura Correttivi Tempi Confezionamento in Secchio
            processo.parSecchioCorrettivoTempi[j] = Integer.parseInt(parametriConfezioni[18][j]);

        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// LETTURA PARAMETRI CONFEZIONAMENTO - PARAMETRI PRODOTTO
        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Lettura Parametri Confezionamento: Parametri Prodotti");

        for (int j = 0; j < processo.numConfezioniPerMiscela; j++) {

            //Lettura del Materiale Mancante per l'Intervento della Pesatura Fine
            processo.materialeInizioPesaturaFine[j] = Integer.parseInt(parametriConfezioni[8][j])
                    * (processo.parPrestazioneMaterialeInizioPesaturaFine / 100);

            processo.tempoSvuotamentoTubo[j] = Integer.parseInt(parametriConfezioni[6][j]);
            processo.materialeMediamenteNelTubo[j] = Integer.parseInt(parametriConfezioni[7][j]) - Integer.parseInt(ParametriSingolaMacchina.parametri.get(513));

            //Calcolo del Peso da Raggiungere
            processo.pesoDaRaggiungereConfezione[j] = Integer.parseInt(TrovaSingoloValoreParametroRipristino(10))
                    - processo.materialeMediamenteNelTubo[j]
                    + Integer.parseInt(ParametriSingolaMacchina.parametri.get(335));

        }

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Lettura ParametriConfezionamento: Variazione Vel Confezioni");

        //Lettura Parametri Variazione Vel Confezioni
        for (int j = 0; j < processo.numConfezioniPerMiscela; j++) {
            for (int i = 0; i < numeroVelScarico; i++) {

                int valoreVelocita = (int) (Double.parseDouble(parametriConfezioni[13 + i][j])
                        * (processo.parPrestazioneVelocitaScarico / 100)
                        * (processo.parProdottoCorrettivoVelocita / 100)
                        * Double.parseDouble(ParametriSingolaMacchina.parametri.get(360)));

                processo.velocitaMiscelatoreFaseScarico[j][i] = ApprossimaValoreVelInverter(valoreVelocita);

            }
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// LETTURA PARAMETRI CONFEZIONAMENTO - TEMPI DI APERTURA E CHIUSURA
        //Lettura Valori Limite per id Sacco
        for (int j = 0; j < processo.numConfezioniPerMiscela; j++) {
            for (int i = 0; i < Integer.parseInt(ParametriGlobali.parametri.get(60)); i++) {
                if (i != Integer.parseInt(ParametriGlobali.parametri.get(60)) - 1) {
                    processo.limiti[j][i] = Integer.parseInt(parametriConfezioni[9 + i][j]);
                } else {
                    processo.limiti[j][i] = Integer.parseInt(parametriConfezioni[8][j]);
                }
            }
        }

        //Lettura Tempi di Apertura per id Sacco
        for (int j = 0; j < processo.numConfezioniPerMiscela; j++) {
            for (int i = 0; i < Integer.parseInt(ParametriGlobali.parametri.get(60)); i++) {

                if (Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(13))) {

                    processo.tempiApertura[j][i]
                            = (int) (Double.parseDouble(parametriConfezioni[1 + i][j])
                            * (processo.parPrestazioneTempiScarico / 100)
                            * (processo.parProdottoCorrettivoTempi / 100)
                            * (processo.parSecchioCorrettivoTempi[j] / 100)
                            * Double.parseDouble(ParametriSingolaMacchina.parametri.get(237)));

                    processo.tempiChiusura[j][i]
                            = (int) (Double.parseDouble(parametriConfezioni[19 + i][j])
                            * (processo.parPrestazioneTempiScarico / 100)
                            * (processo.parProdottoCorrettivoTempi / 100)
                            * (processo.parSecchioCorrettivoTempi[j] / 100));

                } else {

                    processo.tempiApertura[j][i]
                            = (int) (Double.parseDouble(parametriConfezioni[1 + i][j])
                            * (processo.parPrestazioneTempiScarico / 100)
                            * (processo.parProdottoCorrettivoTempi / 100)
                            * Double.parseDouble(ParametriSingolaMacchina.parametri.get(237)));

                    processo.tempiChiusura[j][i]
                            = (int) (Double.parseDouble(parametriConfezioni[19 + i][j])
                            * (processo.parPrestazioneTempiScarico / 100)
                            * (processo.parProdottoCorrettivoTempi / 100));
                }

            }
        }

        //Memorizzazione Log Processo
        ProcessoLogger.logger.log(Level.INFO, "Parametri Confezionamento{0}"
                + "Numero di Vel definite per lo Scarico = {1}{2}"
                + "Numero di Confezioni per Miscela = {3}{4}"
                + "Indice Correttivo Vel Prestazioni = {5}{6}"
                + "Indice Correttivo Tempi Prestazioni = {7}{8}"
                + "Indice Correttivo Materiale Mancante Inizio Pesatura Fine Prestazioni = {9}{10}"
                + "Indice tipo contenitore : Sacco o Secchio = {11}{12}"
                + "Indice Correttivo Velocita Prodotto = {13}{14}"
                + "Indice Correttivo Tempi Prodotto = {15}{16}"
                + "Indice Tempi Valvola = {17}",
                new Object[]{
                    LOG_CHAR_SEPARATOR,
                    numeroVelScarico,
                    LOG_CHAR_SEPARATOR,
                    processo.numConfezioniPerMiscela,
                    LOG_CHAR_SEPARATOR,
                    processo.parPrestazioneVelocitaScarico,
                    LOG_CHAR_SEPARATOR,
                    processo.parPrestazioneTempiScarico,
                    LOG_CHAR_SEPARATOR,
                    processo.parPrestazioneMaterialeInizioPesaturaFine,
                    LOG_CHAR_SEPARATOR,
                    processo.confezionamentoInSecchio,
                    LOG_CHAR_SEPARATOR,
                    processo.parProdottoCorrettivoVelocita,
                    LOG_CHAR_SEPARATOR,
                    processo.parProdottoCorrettivoTempi,
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(237)});

        //Memorizzazione Log Processo
        ProcessoLogger.logger.log(Level.CONFIG, "Numero Punti di Rilevo Scarico = {0}", numPuntiRilievoScarico);

        for (int i = 0; i < numPuntiRilievoScarico; i++) {

            //Memorizzazione Log Processo
            ProcessoLogger.logger.log(Level.CONFIG, "Aliquota Velocita Scarico Indice {0} = {1}", new Object[]{i, processo.aliqScarico.get(i)});

        }

        //Memorizzazione Log di Processo
        for (int j = 0; j < processo.numConfezioniPerMiscela; j++) {

            String s = "";

            for (int i = 0; i < numeroVelScarico; i++) {
                s += "- Vel " + i + "=" + VerificaLunghezzaStringa(processo.velocitaMiscelatoreFaseScarico[j][i],4) + LOG_CHAR_SEPARATOR;
            }

            for (int i = 0; i < Integer.parseInt(ParametriGlobali.parametri.get(60)); i++) {

                s += "- Limite " + i + " Calcolo Riapertura =" + processo.limiti[j][i] + LOG_CHAR_SEPARATOR
                        + "- Tempo Apertura " + i + " = " + processo.tempiApertura[j][i] + LOG_CHAR_SEPARATOR
                        + "- Tempo Chiusura " + i + " = " + processo.tempiChiusura[j][i] + LOG_CHAR_SEPARATOR;

            }

            //Memorizzazione Log Processo
            ProcessoLogger.logger.log(Level.INFO,
                    "iConfezione = {0}{1} "
                    + "Peso da Raggiungere = {2}{3}"
                    + "- Materiale Inizio Pesatura Fine = {4}{5}"
                    + "- Tempo Svuotamento Tubo  = {6}{7}"
                    + "- Materiale Mediamente Presente nel Tubo  = {8}{9}{10}"
                    + "Parametri Secchi - Vel ={11}{12}"
                    + "Parametri Secchi - Correttivo Tempi = {13}",
                    new Object[]{
                        j,
                        LOG_CHAR_SEPARATOR,
                        processo.pesoDaRaggiungereConfezione[j],
                        LOG_CHAR_SEPARATOR,
                        processo.materialeInizioPesaturaFine[j],
                        LOG_CHAR_SEPARATOR,
                        processo.tempoSvuotamentoTubo[j],
                        LOG_CHAR_SEPARATOR,
                        processo.materialeMediamenteNelTubo[j],
                        LOG_CHAR_SEPARATOR,
                        s,
                        processo.parSecchioVelocita[j],
                        LOG_CHAR_SEPARATOR,
                        processo.parSecchioCorrettivoTempi[j]});
        }

        processo.svuotamento_valvova_da_eseguire = Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(77));

        //Memorizzazione Log Processo
        ProcessoLogger.logger.log(Level.INFO, "Abilitazione Procedura Svuotamento Valvola Prima Miscela ={0}", processo.svuotamento_valvova_da_eseguire);

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Fine Lettura Parametri Processo");
        

    }

    //Lettura parametri correttivi di processo impostati manualmente sulla macchina
    public void letturaCorrettivi() {

        //Lettura indice Correttivo Vel Prestazioni
        processo.parPrestazioneVelocitaScarico
                = Double.parseDouble(
                        ParametriSingolaMacchina.parametri.get(108));

        //Lettura indice Correttivo Tempi Prestazioni
        processo.parPrestazioneTempiScarico
                = Double.parseDouble(
                        ParametriSingolaMacchina.parametri.get(109));

        //Lettura indice Correttivo Materiale Mancante Inizio Pesatura Fine Prestazioni
        processo.parPrestazioneMaterialeInizioPesaturaFine
                = Double.parseDouble(
                        ParametriSingolaMacchina.parametri.get(110));

    }

    private class ThreadConfiguraStampante extends Thread {

        String VALORE_PAR_SINGOLA_MACCHINA_INDIRIZZO_STAMPANTE = ParametriSingolaMacchina.parametri.get(451);
        int VALORE_PAR_SINGOLA_MACCHINA_PORTA_STAMPANTE = Integer.parseInt(ParametriSingolaMacchina.parametri.get(452));
        Processo processo;
        int contatore;

        public ThreadConfiguraStampante(Processo processo, int contatore) {
            this.processo = processo;
            this.contatore = contatore;
        }

        @Override
        public void run() {
            new CitronixCIJ_Client(
                    VALORE_PAR_SINGOLA_MACCHINA_INDIRIZZO_STAMPANTE,
                    VALORE_PAR_SINGOLA_MACCHINA_PORTA_STAMPANTE,
                    EstraiStringaHtml(processo.prodotto.getNome()),
                    ParametriSingolaMacchina.parametri.get(453),
                    Integer.toString(contatore));

        }

    }
}
