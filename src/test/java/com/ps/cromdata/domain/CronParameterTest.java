package com.ps.cromdata.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ps.cromdata.web.rest.TestUtil;

public class CronParameterTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CronParameter.class);
        CronParameter cronParameter1 = new CronParameter();
        cronParameter1.setId(1L);
        CronParameter cronParameter2 = new CronParameter();
        cronParameter2.setId(cronParameter1.getId());
        assertThat(cronParameter1).isEqualTo(cronParameter2);
        cronParameter2.setId(2L);
        assertThat(cronParameter1).isNotEqualTo(cronParameter2);
        cronParameter1.setId(null);
        assertThat(cronParameter1).isNotEqualTo(cronParameter2);
    }
}
