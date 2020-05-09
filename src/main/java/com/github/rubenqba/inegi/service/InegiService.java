package com.github.rubenqba.inegi.service;

import com.github.rubenqba.inegi.domain.MxLocality;
import com.github.rubenqba.inegi.domain.MxRegion;
import com.github.rubenqba.inegi.domain.MxState;

import java.util.List;

public interface InegiService {

    /**
     * get mexican states
     * @return immutable list of states or else an empty list
     */
    List<MxState> getMxStates();

    /**
     * get mexican municipals by state
     * @param state
     * @return immutable list of municipals or else an empty list
     */
    List<MxRegion> getMxRegions(MxState state);

    /**
     * get localities by municipal
     * @param region
     * @return immutable list of localities or else an empty list
     */
    List<MxLocality> getMxLocalities(MxRegion region);

}
