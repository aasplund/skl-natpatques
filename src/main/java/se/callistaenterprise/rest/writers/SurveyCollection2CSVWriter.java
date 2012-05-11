package se.callistaenterprise.rest.writers;

import static se.callistaenterprise.Utils.NEW_LINE;
import static se.callistaenterprise.Utils.QUOT;
import static se.callistaenterprise.Utils.tryFormatDate;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import se.callistaenterprise.domain.Indicator;
import se.callistaenterprise.domain.Survey;

@Provider
@Produces("application/csv")
public class SurveyCollection2CSVWriter implements MessageBodyWriter<Collection<Survey>> {

    private static final char SEPARATOR = ',';


    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        // Ensure that we're handling only Collection<HsaObject> objects.
        boolean isWritable;
        if (Collection.class.isAssignableFrom(type) && genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type[] actualTypeArgs = (parameterizedType.getActualTypeArguments());
            isWritable = (actualTypeArgs.length == 1 && actualTypeArgs[0].equals(Survey.class));
        } else {
            isWritable = false;
        }
        return isWritable;
    }

    @Override
    public long getSize(Collection<Survey> t, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Collection<Survey> survies, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream out) throws IOException,
            WebApplicationException {

        PrintStream ps = new PrintStream(out, true, "UTF-8");
        
        ps.append(QUOT).append("hsaId").append(QUOT).append(SEPARATOR)
            .append(QUOT).append("startDateQuestionnaire").append(QUOT).append(SEPARATOR)
            .append(QUOT).append("endDateQuestionnaire").append(QUOT).append(SEPARATOR);

        boolean first = true;
        for (Survey survey : survies) {
            List<Indicator> indicators = survey.getIndicators();
            if (first) {
                first = false;
                for (int i = 0; i < indicators.size(); i++) {
                    ps.append(QUOT).append(indicators.get(i).getName()).append(QUOT);
                    if(i < indicators.size()-1) {
                        ps.append(SEPARATOR);
                    } else {
                        ps.append(NEW_LINE);
                    }
                }
            }
            ps.append(QUOT).append(survey.getHsaId()).append(QUOT).append(SEPARATOR).append(QUOT)
                    .append(tryFormatDate(survey.getStartDateQuestionnaire(), "yyyy-MM-dd")).append(QUOT)
                    .append(SEPARATOR).append(tryFormatDate(survey.getEndDateQuestionnaire(), "yyyy-MM-dd"))
                    .append(QUOT).append(SEPARATOR);

            for (int i = 0; i < indicators.size(); i++) {
                Indicator indicator = indicators.get(i);
                if(indicator != null && indicator.getValue() != null) {
                    ps.append(QUOT).append(indicator.getValue().toString()).append(QUOT);
                } else {
                    ps.append(QUOT).append("").append(QUOT);
                }
                if (i < indicators.size() - 1) {
                    ps.append(SEPARATOR);
                } else {
                    ps.append(NEW_LINE);
                }
            }
        }
        ps.close();
    }
    
    
}
