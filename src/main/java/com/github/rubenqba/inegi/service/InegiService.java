package com.github.rubenqba.inegi.service;

import com.github.rubenqba.inegi.domain.MxLocality;
import com.github.rubenqba.inegi.domain.MxMunicipal;
import com.github.rubenqba.inegi.domain.MxState;

import java.util.List;
import java.util.Optional;

/**
 * Service interface to allow access to INEGI data
 * @author Ruben Bresler
 * @since 1.0.0
 */
public interface InegiService {

    /**
     * get mexican states
     * @return immutable list of states or else an empty list
     */
    List<MxState> getMxStates();

    /**
     * get mexican state by id
     * @param state requested state id
     * @return a valid state, or empty otherwise
     * @see Optional#empty()
     */
    Optional<MxState> getMxState(String state);

    /**
     * get mexican municipals by state
     * @param state requested state
     * @return immutable list of municipals or else an empty list
     */
    List<MxMunicipal> getMxMunicipals(MxState state);

    /**
     * get mexican state by id
     * @param state requested state id
     * @param municipal requested municipal id
     * @return a valid muncipal, or empty otherwise
     * @see Optional#empty()
     */
    Optional<MxMunicipal> getMxMunicipal(String state, String municipal);

    /**
     * get localities by municipal
     * @param municipal requested municipal
     * @return immutable list of localities or else an empty list
     */
    List<MxLocality> getMxLocalities(MxMunicipal municipal);

    /**
     * get mexican state by id
     * @param state requested state id
     * @param municipal requested municipal id
     * @param locality requested locality id
     * @return a valid locality, or empty otherwise
     * @see Optional#empty()
     */
    Optional<MxLocality> getMxLocality(String state, String municipal, String locality);

}
