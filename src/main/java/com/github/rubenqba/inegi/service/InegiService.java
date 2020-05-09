package com.github.rubenqba.inegi.service;

import com.github.rubenqba.inegi.domain.MxLocalty;
import com.github.rubenqba.inegi.domain.MxRegion;
import com.github.rubenqba.inegi.domain.MxState;

import java.util.List;

public interface InegiService {

    List<MxState> getMxStates();

    List<MxRegion> getMxRegions(MxState state);

    List<MxLocalty> getMxLocalties(MxRegion region);

}
