package com.atlasinside.crondata.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.atlasinside.crondata.web.rest.TestUtil;

public class TargetsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Targets.class);
        Targets targets1 = new Targets();
        targets1.setId(1L);
        Targets targets2 = new Targets();
        targets2.setId(targets1.getId());
        assertThat(targets1).isEqualTo(targets2);
        targets2.setId(2L);
        assertThat(targets1).isNotEqualTo(targets2);
        targets1.setId(null);
        assertThat(targets1).isNotEqualTo(targets2);
    }
}
