package com.smart.servlet.phase;

import com.smart.servlet.context.SmartContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SmartPhase {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public abstract void execute(SmartContext context);

}
