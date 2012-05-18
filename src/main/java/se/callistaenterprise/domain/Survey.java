package se.callistaenterprise.domain;

import static org.apache.commons.lang3.StringUtils.remove;
import static se.callistaenterprise.Utils.tryParseDate;
import static se.callistaenterprise.Utils.tryParseInt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@XmlRootElement
public final class Survey {
    @XmlElement private String hsaId;
    @XmlElement private Date startDateQuestionnaire;
    @XmlElement private Date endDateQuestionnaire;
    @JsonIgnore @Indexed private int year;
    @JsonIgnore @Indexed private String county;
    
    @XmlElementWrapper @XmlElement(name="indicator") private List<Indicator> indicators;

    public static class SurveyBuilder implements EntityBuilder<Survey> {

        private Survey survey = new Survey();

        public SurveyBuilder() {
            survey.indicators = new ArrayList<Indicator>();
        }

        public SurveyBuilder(String hsaId) {
            this();
            survey.hsaId = hsaId;
        }

        public SurveyBuilder(Survey other) {
            survey.hsaId = other.hsaId;
        }

        public SurveyBuilder setStartDateQuestionnaire(Date startDateQuestionnaire) {
            if (startDateQuestionnaire != null) {
                survey.startDateQuestionnaire = new Date(startDateQuestionnaire.getTime());
            }
            return this;
        }

        public SurveyBuilder setEndDateQuestionnaire(Date endDateQuestionnaire) {
            if (endDateQuestionnaire != null) {
                survey.endDateQuestionnaire = new Date(endDateQuestionnaire.getTime());
            }
            return this;
        }

        public SurveyBuilder setHsaId(String hsaId) {
            survey.hsaId = hsaId;
            return this;
        }

        public SurveyBuilder setIndicators(List<Indicator> indicators) {
            survey.indicators = indicators;
            return this;
        }

        public SurveyBuilder addIndicator(String name, Integer value) {
            survey.indicators.add(new Indicator(name, value));
            return this;
        }

        public SurveyBuilder setYear(int year) {
            survey.year = year;
            return this;
        }

        public SurveyBuilder setCounty(String county) {
            survey.county = county;
            return this;
        }

        @Override
        public Survey build() {
            return survey;
        }
    }

    private Survey() {
    }

    public String getHsaId() {
        return hsaId;
    }

    public List<Indicator> getIndicators() {
        return indicators;
    }

    public Date getEndDateQuestionnaire() {
        return endDateQuestionnaire;
    }

    public Date getStartDateQuestionnaire() {
        return startDateQuestionnaire;
    }
    
    public int getYear() {
        return year;
    }
    
    public String getCounty() {
        return county;
    }

    public String getHsaObjectUrl() {
        try {
            return "http://54.247.175.65:8080/orgmaster-hsa/v1/hsaObjects/" + URLEncoder.encode(hsaId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "http://54.247.175.65:8080/orgmaster-hsa/v1/hsaObjects/" + hsaId;
        }
    }

    public static Collection<Survey> parse(InputStream is) throws IOException {

        List<Survey> surveys = new ArrayList<Survey>();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        Scanner lineScanner = new Scanner(br).useDelimiter("\"\"");
        while (lineScanner.hasNext()) {
            String line = lineScanner.next();
            Scanner elementScanner = new Scanner(line).useDelimiter(";");
            while (elementScanner.hasNext()) {
                SurveyBuilder sb = new SurveyBuilder();
                sb.setHsaId(remove(elementScanner.next(), '\"'))
                        .setCounty("Stockholm")
                        .addIndicator("Bemötande", tryParseInt(remove(elementScanner.next(), '\"')))
                        .addIndicator("Delaktighet", tryParseInt(remove(elementScanner.next(), '\"')))
                        .addIndicator("Förtroende", tryParseInt(remove(elementScanner.next(), '\"')))
                        .addIndicator("Helhetsintryck", tryParseInt(remove(elementScanner.next(), '\"')))
                        .addIndicator("Information", tryParseInt(remove(elementScanner.next(), '\"')))
                        .addIndicator("Rekommendera", tryParseInt(remove(elementScanner.next(), '\"')))
                        .addIndicator("Tillgänglighet", tryParseInt(remove(elementScanner.next(), '\"')))
                        .addIndicator("Upplevd nytta", tryParseInt(remove(elementScanner.next(), '\"')))
                        .setStartDateQuestionnaire(tryParseDate(remove(elementScanner.next(), '\"')));
                
               Date d = tryParseDate(remove(elementScanner.next(), '\"'));
               int year = tryParseInt(new SimpleDateFormat("yyyy").format(d));
               sb.setEndDateQuestionnaire(d).setYear(year);
               
               surveys.add(sb.build());
            }
        }
        return surveys;
    }

}
