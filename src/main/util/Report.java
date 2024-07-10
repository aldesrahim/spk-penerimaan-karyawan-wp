package main.util;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 */
public class Report {
    public static void showReport(String name) throws Exception {
        showReport(name, null);
    }
    
    public static void showReport(String name, Map<String, Object> additionalParams) throws Exception {
        try {
            File file = new File("src/resources/report/" + name + ".jrxml");
            String sourcePath = file.getAbsolutePath();
            
            Database db = Database.getInstance();
            db.connect();
            
            JasperDesign jd = JRXmlLoader.load(sourcePath);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            
            Map<String, Object> params = new HashMap<>();
            params.put(JRParameter.REPORT_LOCALE, new Locale("id", "ID"));
            params.put("REPORT_SUBTITLE", "SPK Penerimaan Karyawan");
            params.put("TTD_TITLE", "HRD");
            params.put("TTD_NAME", "Fariyandi");
            
            if (additionalParams != null) {
                additionalParams.forEach((key, value) -> {
                    params.put(key, value);
                });
            }
            
            JasperPrint jp = JasperFillManager.fillReport(
                    jr,
                    params,
                    db.getConnection()
            );
            
            if (jp.getPages().isEmpty()) {
                throw new Exception("Laporan kosong");
            }
            
            JasperViewer.viewReport(jp, false);
        } catch (Exception e) {
            throw e;
        }
    }
}
