package com.github.rubenqba.inegi.service.impl;

import com.github.rubenqba.inegi.domain.MxLocality;
import com.github.rubenqba.inegi.domain.MxRegion;
import com.github.rubenqba.inegi.domain.MxState;
import com.github.rubenqba.inegi.service.InegiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.rubenqba.inegi.domain.LocaltyScope.RURAL;
import static com.github.rubenqba.inegi.domain.LocaltyScope.URBANO;
import static org.assertj.core.api.Assertions.assertThat;

class InegiServiceImplTest {

    private final InegiService service = new InegiServiceImpl();

    @BeforeEach
    void setUp() {
        assertThat(service).isNotNull();
    }

    @Test
    void getMexicanStates() {
        final var mxStates = service.getMxStates();
        assertThat(mxStates).isNotEmpty();
        assertThat(mxStates).extracting(MxState::getId).doesNotContainNull();
        assertThat(mxStates).extracting(MxState::getName).doesNotContainNull();
        assertThat(mxStates).extracting(MxState::getAbbreviation).doesNotContainNull();
    }

    @Test
    void getMexicanMunicipalities() {
        MxState nuevoLeonState = new MxState("19", "Nuevo Leon", "NL");

        final var regions = service.getMxRegions(nuevoLeonState);
        assertThat(regions).isNotEmpty();
        assertThat(regions).extracting(MxRegion::getId).doesNotContainNull();
        assertThat(regions).extracting(MxRegion::getState).doesNotContainNull().allMatch(s -> nuevoLeonState.getId().contentEquals(s));
        assertThat(regions).extracting(MxRegion::getName).doesNotContainNull();
        assertThat(regions).extracting(MxRegion::getFirstCity).doesNotContainNull();
    }

    @Test
    void getMexicanLocalties() {
        MxRegion monterrey = new MxRegion("039", "19", "Monterrey", "0001");

        final var regions = service.getMxLocalities(monterrey);
        assertThat(regions).isNotEmpty();
        assertThat(regions).extracting(MxLocality::getId).doesNotContainNull();
        assertThat(regions).extracting(MxLocality::getState).doesNotContainNull().allMatch(s -> monterrey.getState().contentEquals(s));
        assertThat(regions).extracting(MxLocality::getRegion).doesNotContainNull().allMatch(s -> monterrey.getId().contentEquals(s));
        assertThat(regions).extracting(MxLocality::getName).doesNotContainNull();
        assertThat(regions).extracting(MxLocality::getLatitude).doesNotContainNull();
        assertThat(regions).extracting(MxLocality::getLongitude).doesNotContainNull();
        assertThat(regions).extracting(MxLocality::getAltitude).doesNotContainNull();
        assertThat(regions).extracting(MxLocality::getScope).doesNotContainNull().contains(URBANO, RURAL);
    }
}