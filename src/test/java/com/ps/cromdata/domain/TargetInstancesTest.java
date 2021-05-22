package com.ps.cromdata.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ps.cromdata.web.rest.TestUtil;

public class TargetInstancesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TargetInstances.class);
        TargetInstances targetInstances1 = new TargetInstances();
        targetInstances1.setId(1L);
        TargetInstances targetInstances2 = new TargetInstances();
        targetInstances2.setId(targetInstances1.getId());
        assertThat(targetInstances1).isEqualTo(targetInstances2);
        targetInstances2.setId(2L);
        assertThat(targetInstances1).isNotEqualTo(targetInstances2);
        targetInstances1.setId(null);
        assertThat(targetInstances1).isNotEqualTo(targetInstances2);
    }
}
