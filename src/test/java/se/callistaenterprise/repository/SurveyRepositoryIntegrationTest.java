package se.callistaenterprise.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.callistaenterprise.domain.Survey;
import se.callistaenterprise.domain.Survey.SurveyBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SurveyRepositoryIntegrationTest {
    @Autowired
    private MongoTemplate template;

    @Before
    public void setUp() {
        template.dropCollection(Survey.class);
        for (int i = 1; i <= 10; i++) {
            Survey survey = new SurveyBuilder(i+"")
            .setStartDateQuestionnaire(new Date())
            .setEndDateQuestionnaire(new Date())
            .addIndicator("a", 90)
            .addIndicator("b", 91)
            .addIndicator("c", 92)
            .addIndicator("d", 93)
            .addIndicator("e", 94)
            .addIndicator("f", 95)
            .addIndicator("g", 96)
                .build();
            template.save(survey);
        }
    }

    @Test
    public void shouldFindCareUnitByHsaId() {
        Survey acctualCareUnit = template.findById("1", Survey.class);
        assertThat(acctualCareUnit, is(notNullValue()));
        assertThat(acctualCareUnit.getHsaId(), is("1"));
        assert(true);
    }

}
