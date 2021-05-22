package com.ps.cromdata.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ps.cromdata.web.rest.TestUtil;

public class CronConfigTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CronConfig.class);
        CronConfig cronConfig1 = new CronConfig();
        cronConfig1.setId(1L);
        CronConfig cronConfig2 = new CronConfig();
        cronConfig2.setId(cronConfig1.getId());
        assertThat(cronConfig1).isEqualTo(cronConfig2);
        cronConfig2.setId(2L);
        assertThat(cronConfig1).isNotEqualTo(cronConfig2);
        cronConfig1.setId(null);
        assertThat(cronConfig1).isNotEqualTo(cronConfig2);
    }
}
