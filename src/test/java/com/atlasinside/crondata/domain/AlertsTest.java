package com.atlasinside.crondata.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.atlasinside.crondata.web.rest.TestUtil;

public class AlertsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Alerts.class);
        Alerts alerts1 = new Alerts();
        alerts1.setId(1L);
        Alerts alerts2 = new Alerts();
        alerts2.setId(alerts1.getId());
        assertThat(alerts1).isEqualTo(alerts2);
        alerts2.setId(2L);
        assertThat(alerts1).isNotEqualTo(alerts2);
        alerts1.setId(null);
        assertThat(alerts1).isNotEqualTo(alerts2);
    }
}
