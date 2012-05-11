package se.callistaenterprise.rest.writers;

import static org.apache.commons.beanutils.BeanUtils.getProperty;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static se.callistaenterprise.Utils.NEW_LINE;
import static se.callistaenterprise.Utils.QUOT;
import static se.callistaenterprise.Utils.getLast;

import java.io.PrintStream;
import java.util.List;

public class Bean2CSVWriter {
    
    private List<String> properties;
    private static final char SEPARATOR = ',';

    public Bean2CSVWriter(List<String> properties) {
        this.properties = properties;
    }
    
    public void writeHeader(PrintStream out) {
        for (int i = 0; i < properties.size()-1; i++) {
            out.append(properties.get(i)).append(SEPARATOR);
        }
        out.append(getLast(properties)).append(NEW_LINE);

    }
    
    public void writeData(PrintStream out, Object bean) {
        try {
            for (int i = 0; i < properties.size() - 1; i++) {
                out.append(QUOT)
                .append(defaultIfBlank(getProperty(bean, properties.get(i)),EMPTY))
                .append(QUOT).append(SEPARATOR);
            }
            out.append(QUOT)
            .append(getProperty(bean, getLast(properties)).toString())
            .append(QUOT).append(NEW_LINE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
