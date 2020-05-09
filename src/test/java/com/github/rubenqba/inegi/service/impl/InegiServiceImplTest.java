package com.github.rubenqba.inegi.service.impl;

import com.github.rubenqba.inegi.domain.MxLocality;
import com.github.rubenqba.inegi.domain.MxMunicipal;
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
    void getState() {
        assertThat(service.getMxState("XX"))
                .isNotPresent();

        assertThat(service.getMxState("26"))
                .isPresent()
                .hasValueSatisfying(state -> {
                            assertThat(state).extracting(MxState::getId).isEqualTo("26");
                            assertThat(state).extracting(MxState::getName).isEqualTo("Sonora");
                            assertThat(state).extracting(MxState::getAbbreviation).isEqualTo("Son.");
                        }
                );
    }

    @Test
    void getMexicanMunicipalities() {
        MxState nuevoLeonState = new MxState("19", "Nuevo Leon", "NL");

        final var regions = service.getMxRegions(nuevoLeonState);
        assertThat(regions).isNotEmpty();
        assertThat(regions).extracting(MxMunicipal::getId).doesNotContainNull();
        assertThat(regions).extracting(MxMunicipal::getState).doesNotContainNull().allMatch(s -> nuevoLeonState.getId().contentEquals(s));
        assertThat(regions).extracting(MxMunicipal::getName).doesNotContainNull();
        assertThat(regions).extracting(MxMunicipal::getFirstCity).doesNotContainNull();
    }

    @Test
    void getMunicipal() {
        assertThat(service.getMxMunicipal("14", "ZZZ"))
                .isNotPresent();

        assertThat(service.getMxMunicipal("14", "039"))
                .isPresent()
                .hasValueSatisfying(municipal -> {
                            assertThat(municipal).extracting(MxMunicipal::getId).isEqualTo("039");
                            assertThat(municipal).extracting(MxMunicipal::getState).isEqualTo("14");
                            assertThat(municipal).extracting(MxMunicipal::getName).isEqualTo("Guadalajara");
                            assertThat(municipal).extracting(MxMunicipal::getFirstCity).isEqualTo("0001");
                        }
                );
    }

    @Test
    void getMexicanLocalties() {
        MxMunicipal monterrey = new MxMunicipal("039", "19", "Monterrey", "0001");

        final var regions = service.getMxLocalities(monterrey);
        assertThat(regions).isNotEmpty();
        assertThat(regions).extracting(MxLocality::getId).doesNotContainNull();
        assertThat(regions).extracting(MxLocality::getState).doesNotContainNull().allMatch(s -> monterrey.getState().contentEquals(s));
        assertThat(regions).extracting(MxLocality::getMunicipal).doesNotContainNull().allMatch(s -> monterrey.getId().contentEquals(s));
        assertThat(regions).extracting(MxLocality::getName).doesNotContainNull();
        assertThat(regions).extracting(MxLocality::getLatitude).doesNotContainNull();
        assertThat(regions).extracting(MxLocality::getLongitude).doesNotContainNull();
        assertThat(regions).extracting(MxLocality::getAltitude).doesNotContainNull();
        assertThat(regions).extracting(MxLocality::getScope).doesNotContainNull().contains(URBANO, RURAL);
    }

    @Test
    void getLocality() {
        assertThat(service.getMxLocality("09", "013", "XXXX"))
                .isNotPresent();

        assertThat(service.getMxLocality("09", "013", "0096"))
                .isPresent()
                .hasValueSatisfying(locality -> {
                            assertThat(locality).extracting(MxLocality::getId).isEqualTo("0096");
                            assertThat(locality).extracting(MxLocality::getMunicipal).isEqualTo("013");
                            assertThat(locality).extracting(MxLocality::getState).isEqualTo("09");
                            assertThat(locality).extracting(MxLocality::getName).isEqualTo("Ixotitla");
                            assertThat(locality).extracting(MxLocality::getScope).isEqualTo(RURAL);
                            assertThat(locality).extracting(MxLocality::getLatitude).isEqualTo(19.2372167);
                            assertThat(locality).extracting(MxLocality::getLongitude).isEqualTo(-99.0569950);
                            assertThat(locality).extracting(MxLocality::getAltitude).isEqualTo(2350.0);
                        }
                );
    }
}