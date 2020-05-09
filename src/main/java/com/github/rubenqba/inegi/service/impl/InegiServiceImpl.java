package com.github.rubenqba.inegi.service.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rubenqba.inegi.domain.LocaltyScope;
import com.github.rubenqba.inegi.domain.MxLocality;
import com.github.rubenqba.inegi.domain.MxRegion;
import com.github.rubenqba.inegi.domain.MxState;
import com.github.rubenqba.inegi.service.InegiService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class InegiServiceImpl implements InegiService {

    private static final String INEGI_GEO_API = "https://gaia.inegi.org.mx/wscatgeo";

    private static final Function<InegiState, MxState> stateDomainMapper = (dto) -> new MxState(dto.getCveAgee(), dto.getNomAbrev(), dto.getNomAbrev());
    private static final Function<InegiMunicipio, MxRegion> regionDomainMapper = (dto) -> new MxRegion(dto.municipaly, dto.state, dto.name, dto.principalCity);
    private static final Function<InegiLocalidad, MxLocality> localtyDomainMapper = (dto) -> new MxLocality(dto.localty, dto.state, dto.municipal, dto.name, LocaltyScope.valueOf(dto.ambito), dto.latitud, dto.longitud, dto.altitud);

    private final ObjectMapper objectMapper;

    public InegiServiceImpl() {
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);

    }

    @Data
    static class MetadatosDto {
        private String fechaActualizacion;
        private String fuenteInfo;
    }

    @Data
    static class InegiState {
        @JsonProperty("cve_agee")
        private String cveAgee;
        @JsonProperty("nom_agee")
        private String nomAgee;
        @JsonProperty("nom_abrev")
        private String nomAbrev;
        private Long pob; // poblacion total?
        @JsonProperty("pob_fem")
        private Long pobFem; // poblacion femenina
        @JsonProperty("pob_mas")
        private Long pobMas; // poblacion masculina
        private Long viv; // cantidad de viviendas?
    }


    @Data
    static class StatesDto {
        @JsonProperty("datos")
        private List<InegiState> datos;
        private MetadatosDto metadatos;
        private Long numReg;
    }

    @Override
    public List<MxState> getMxStates() {
        final var uriComponents = UriComponentsBuilder.fromHttpUrl(INEGI_GEO_API).path("/mgee").build();
        try {
            Request request = new Request.Builder()
                    .url(uriComponents.toUriString())
                    .build();

            final var result = HttpClientHolder.getHttpClient().newCall(request).execute();

            if (result.isSuccessful()) {
                final var dto = objectMapper.readValue(result.body().string(), StatesDto.class);
                if (log.isTraceEnabled()) {
                    log.trace("received {} states from INEGI service, last updated at {} for '{}'", dto.numReg, dto.metadatos.fechaActualizacion, dto.metadatos.fuenteInfo);
                }
                return dto.datos.stream().map(stateDomainMapper).collect(Collectors.toUnmodifiableList());
            }
        } catch (IOException ex) {
            log.error("There was an error downloading the URL '{}' ", uriComponents.toUri());
            if (log.isTraceEnabled()) {
                log.trace(ex.getMessage(), ex.getCause());
            }
        }
        return Collections.emptyList();
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class InegiMunicipio {
        @JsonProperty("cve_agee")
        private String state;
        @JsonProperty("cve_agem")
        private String municipaly;
        @JsonProperty("nom_agem")
        private String name;
        @JsonProperty("cve_cab")
        private String principalCity;
    }

    @Data
    static class RegionDto {
        @JsonProperty("datos")
        private List<InegiMunicipio> datos;
        private MetadatosDto metadatos;
        private Long numReg;
    }

    @Override
    public List<MxRegion> getMxRegions(MxState state) {
        final var uriComponents = UriComponentsBuilder.fromHttpUrl(INEGI_GEO_API)
                .pathSegment("mgem", "{state}")
                .buildAndExpand(state.getId());
        try {
            Request request = new Request.Builder()
                    .url(uriComponents.toUriString())
                    .build();

            final var result = HttpClientHolder.getHttpClient().newCall(request).execute();

            if (result.isSuccessful()) {
                final var dto = objectMapper.readValue(result.body().string(), RegionDto.class);
                if (log.isTraceEnabled()) {
                    log.trace("received {} {} state's regions from INEGI service, last updated at {} for '{}'",
                            state.getName(), dto.numReg, dto.metadatos.fechaActualizacion, dto.metadatos.fuenteInfo);
                }
                return dto.datos.stream().map(regionDomainMapper).collect(Collectors.toUnmodifiableList());
            }
        } catch (IOException ex) {
            log.error("There was an error downloading the URL '{}' ", uriComponents.toUri());
            if (log.isTraceEnabled()) {
                log.trace(ex.getMessage(), ex.getCause());
            }
        }
        return Collections.emptyList();
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class InegiLocalidad {
        @JsonProperty("cve_agee")
        private String state;
        @JsonProperty("cve_agem")
        private String municipal;
        @JsonProperty("cve_loc")
        private String localty;
        @JsonProperty("nom_loc")
        private String name;
        @JsonProperty("ambito")
        private String ambito;
        @JsonProperty("longitud")
        private Double longitud;
        @JsonProperty("latitud")
        private Double latitud;
        @JsonProperty("altitud")
        private Double altitud;
        @JsonProperty("estatus")
        private boolean enabled;
    }

    @Data
    static class LocaltyDto {
        @JsonProperty("datos")
        private List<InegiLocalidad> datos;
        private MetadatosDto metadatos;
        private Long numReg;
    }

    @Override
    public List<MxLocality> getMxLocalities(MxRegion region) {
        final var uriComponents = UriComponentsBuilder.fromHttpUrl(INEGI_GEO_API)
                .pathSegment("localidades", "{state}", "{region}")
                .buildAndExpand(region.getState(), region.getId());
        try {
            Request request = new Request.Builder()
                    .url(uriComponents.toUriString())
                    .build();

            final var result = HttpClientHolder.getHttpClient().newCall(request).execute();

            if (result.isSuccessful()) {
                final var dto = objectMapper.readValue(result.body().string(), LocaltyDto.class);
                if (log.isTraceEnabled()) {
                    log.trace("received {} {} municipal's localties from INEGI service, last updated at {} for '{}'",
                            region.getName(), dto.numReg, dto.metadatos.fechaActualizacion, dto.metadatos.fuenteInfo);
                }
                return dto.datos.stream().filter(InegiLocalidad::isEnabled).map(localtyDomainMapper).collect(Collectors.toUnmodifiableList());
            }
        } catch (IOException ex) {
            log.error("There was an error downloading the URL '{}' ", uriComponents.toUri());
            if (log.isTraceEnabled()) {
                log.trace(ex.getMessage(), ex.getCause());
            }
        }
        return Collections.emptyList();
    }
}
