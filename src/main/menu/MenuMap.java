package main.menu;

import javax.swing.JPanel;

import main.forms.panels.*;

/**
 *
 * @author aldes
 */
public enum MenuMap {
    DASHBOARD,
    MASTER_BOBOT,
    MASTER_KRITERIA,
    MASTER_NORMALISASI,
    MASTER_SUBKRITERIA,
    MASTER_LOWONGAN,
    MASTER_PELAMAR,
    PP_PENILAIAN,
    PP_PERHITUNGAN,
    LAP_LAPORAN;
    
    public JPanel getPanel() {
        return switch(this) {
            case DASHBOARD -> new DashboardPanel();
            case MASTER_BOBOT -> new MasterBobotPanel();
            case MASTER_KRITERIA -> new MasterKriteriaPanel();
            case MASTER_NORMALISASI -> new MasterNormalisasiPanel();
            case MASTER_SUBKRITERIA -> new MasterSubKriteriaPanel();
            case MASTER_LOWONGAN -> new MasterLowonganPanel();
            case MASTER_PELAMAR -> new MasterPelamarPanel();
            case PP_PENILAIAN -> new PenilaianPanel();
            case PP_PERHITUNGAN -> new PerhitunganPanel();
            case LAP_LAPORAN -> new LaporanPanel();
        };
    }
}
