package com.atlasinside.crondata.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.atlasinside.crondata.web.rest.TestUtil;

public class AppCategoryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppCategory.class);
        AppCategory appCategory1 = new AppCategory();
        appCategory1.setId(1L);
        AppCategory appCategory2 = new AppCategory();
        appCategory2.setId(appCategory1.getId());
        assertThat(appCategory1).isEqualTo(appCategory2);
        appCategory2.setId(2L);
        assertThat(appCategory1).isNotEqualTo(appCategory2);
        appCategory1.setId(null);
        assertThat(appCategory1).isNotEqualTo(appCategory2);
    }
}
